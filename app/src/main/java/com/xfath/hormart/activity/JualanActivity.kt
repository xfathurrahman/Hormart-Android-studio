package com.xfath.hormart.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.bumptech.glide.Glide
import com.irozon.sneaker.Sneaker
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.xfath.hormart.api.ApiConfig
import com.xfath.hormart.databinding.ActivityJualanBinding
import com.xfath.hormart.fragments.bottomsheet.DisconnectedSheetFragment
import com.xfath.hormart.helper.SharedPreference
import com.xfath.hormart.model.ResponseModel
import com.xfath.hormart.utils.MyFunction
import com.xfath.hormart.utils.NetworkConnection
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.EasyImage.ImageSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class JualanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJualanBinding
    private lateinit var imgFile: File
    private lateinit var s: SharedPreference

    private val sheetdc : SuperBottomSheetFragment = DisconnectedSheetFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJualanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        s = SharedPreference(this)

        binding.etDeskripsiProduk.isSingleLine = false

        getUid()
        jual()
        back()
        connection()
    }

    private fun connection() {
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {
                if (sheetdc.isAdded){
                    sheetdc.dismiss()
                }
            }
            else {
                if (sheetdc.isAdded){
                    sheetdc.dismiss()
                } else {
                    sheetdc.apply {
                        show(supportFragmentManager, "CustomBottomSheetDialogFragment")
                    }
                }
            }
        })
    }


    private fun getUid() {
        val user = s.getUser()!!
        binding.tvUid.text = user.Id.toString()
    }

    private fun jual() {
        binding.btnSubmitJual.setOnClickListener {
            val nama = binding.etNamaProduk.text.toString().trim()
            val harga = binding.etHargaProduk.text.toString().trim()
            val deskripsi = binding.etDeskripsiProduk.text.toString().trim()
            val kategori = binding.etKategoriProduk.text.toString().trim()

            if (nama.isEmpty() || nama.length < 10) {
                binding.etNamaProduk.error = "Kolom Nama Produk tidak boleh kosong & minimal 10 huruf"
                binding.etNamaProduk.requestFocus()
                return@setOnClickListener
            } else if (harga.isEmpty()) {
                binding.etHargaProduk.error = "Kolom Harga tidak boleh kosong"
                binding.etHargaProduk.requestFocus()
                return@setOnClickListener
            } else if (deskripsi.isEmpty() || deskripsi.length < 20) {
                binding.etDeskripsiProduk.error = "Kolom Deskripsi tidak boleh kosong & minimal 20 huruf "
                binding.etDeskripsiProduk.requestFocus()
                return@setOnClickListener
            } else if (kategori.isEmpty()) {
                binding.etKategoriProduk.error = "Kolom Kategori tidak boleh kosong"
                binding.etKategoriProduk.requestFocus()
                return@setOnClickListener
            } else if (binding.ivImageUp.drawable == null) {
                Sneaker.with(this)
                    .setTitle("Peringatan !!")
                    .setCornerRadius(5, 5)
                    .setMessage("Upload Minimal 1 Gambar")
                    .sneakError()
            }
            else {
                kirimData()
                val loading = MyFunction.createLoadingDialog(this)
                loading.show()
            }
        }

        binding.ivImageUp.setOnClickListener {
            EasyImage.openChooserWithGallery(
                this@JualanActivity,
                "Pilih",
                3
            )
        }
    }

    private fun kirimData() {

        val requestFile = imgFile.asRequestBody("multipart/from-data".toMediaTypeOrNull())
        val gambar = MultipartBody.Part.createFormData("gambar", imgFile.name, requestFile)

        val uid: RequestBody =
            binding.tvUid.text.toString().trim().toRequestBody("text/plain".toMediaTypeOrNull())

        val nama: RequestBody =
            binding.etNamaProduk.text.toString().trim().toRequestBody("text/plain".toMediaTypeOrNull())

        val harga: RequestBody =
            binding.etHargaProduk.text.toString().trim().toRequestBody("text/plain".toMediaTypeOrNull())

        val deskripsi: RequestBody = binding.etDeskripsiProduk.text.toString().trim()
            .toRequestBody("text/plain".toMediaTypeOrNull())

        val kategori: RequestBody = binding.etKategoriProduk.text.toString().trim()
            .toRequestBody("text/plain".toMediaTypeOrNull())

        ApiConfig.instanceRetrofit.addproduct(uid, nama, harga, deskripsi, kategori, gambar)
            .enqueue(object : Callback<ResponseModel> {
                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    val errorDialog =
                        MyFunction.createErrorDialogAddProduct(this@JualanActivity, contentText = "Produk gagal ditambahkan, Cek Koneksi Internet anda")
                    errorDialog.show()
                    Log.e("ERROR", t.message.toString())
                    binding.btnSubmitJual.visibility = View.VISIBLE
                }
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    val successDialog = MyFunction.createSuccessDialog(
                        context = this@JualanActivity,
                        titleText = "Success",
                        contentText = "Produk telah ditambahkan"
                    )
                    successDialog.show()
                    Log.v("SUKSES", response.body()!!.message.toString())
                    val intent = Intent(this@JualanActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    binding.etNamaProduk.text.clear()
                    binding.etHargaProduk.text.clear()
                    binding.etDeskripsiProduk.text.clear()
                    binding.etKategoriProduk.text.clear()
                }
        }   )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            this,
            object : DefaultCallback() {

                override fun onImagePicked(imageFile: File, source: ImageSource, type: Int) {
                    CropImage.activity(Uri.fromFile(imageFile))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setFixAspectRatio(true)
                        .start(this@JualanActivity)
                }

                override fun onImagePickerError(e: Exception, source: ImageSource, type: Int) {
                    super.onImagePickerError(e, source, type)
                    Toast.makeText(this@JualanActivity, "" + e.message, Toast.LENGTH_SHORT).show()
                }

            })

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {

                val uri: Uri = result.uri

                Glide.with(applicationContext)
                    .load(File(uri.path!!))
                    .into(binding.ivImageUp)

                imgFile = File(uri.path!!)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val exception = result.error
                Toast.makeText(this, "" + exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun back() {
        binding.kembalidrinput.setOnClickListener {
            val intent = Intent(this@JualanActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@JualanActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}