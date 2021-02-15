package com.xfath.hormart.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.xfath.hormart.R
import com.xfath.hormart.api.ApiConfig
import com.xfath.hormart.databinding.ActivityUpPhotoProfileBinding
import com.xfath.hormart.helper.SharedPreference
import com.xfath.hormart.model.ResponseModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UpPhotoProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpPhotoProfileBinding
    private lateinit var imgFilePp: File
    private lateinit var s: SharedPreference
    private var backPressedTime = 0L
    private var textSelamat = "Selamat Foto Profil \n Berhasil Diperbaharui !"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpPhotoProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        s = SharedPreference(this)

        lewati()
        getPid()
        uploadfoto()

        binding.simpanpp.setOnClickListener {

            val name = s.getUser()!!
            Toast.makeText(
                this@UpPhotoProfileActivity,"Selamat Datang " + name.userName, Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this@UpPhotoProfileActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()

        }

    }

    private fun getPid() {
        val user = s.getUser()!!
        binding.pId.text = user.Id.toString()
    }

    private fun uppp() {
        binding.pgUppp.visibility = View.VISIBLE

        val requestFiles = imgFilePp.asRequestBody("multipart/from-data".toMediaTypeOrNull())
        val photoprofile = MultipartBody.Part.createFormData("photoprofile", imgFilePp.name, requestFiles)

        val pid: RequestBody =
            binding.pId.text.toString().trim().toRequestBody("text/plain".toMediaTypeOrNull())

        ApiConfig.instanceRetrofit.addpp(pid, photoprofile)
            .enqueue(object : Callback<ResponseModel> {
                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(this@UpPhotoProfileActivity, t.message, Toast.LENGTH_SHORT).show()
                    binding.pgUppp.visibility = View.GONE
                }
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    binding.tvLewatiUpload.visibility = View.GONE
                    binding.layoutavaup.isEnabled = false
                    binding.imgPlus.visibility = View.GONE
                    binding.pgUppp.visibility = View.GONE
                    binding.rvSimpan.visibility = View.VISIBLE
                    binding.lottieProfile.visibility = View.GONE
                    binding.lottieCongrats.visibility = View.VISIBLE
                    binding.lottieCheck.visibility = View.VISIBLE
                    binding.lottieConfetti.visibility = View.VISIBLE
                    binding.tvAmbilorgaleri.text = textSelamat
                }

            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            this,
            object : DefaultCallback() {

                override fun onImagePicked(imageFile: File, source: EasyImage.ImageSource, type: Int) {
                    CropImage.activity(Uri.fromFile(imageFile))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setFixAspectRatio(true)
                        .start(this@UpPhotoProfileActivity)
                }

                override fun onImagePickerError(e: Exception, source: EasyImage.ImageSource, type: Int) {
                    super.onImagePickerError(e, source, type)
                    Toast.makeText(this@UpPhotoProfileActivity, "" + e.message, Toast.LENGTH_SHORT).show()
                }

            })

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {

                val uri: Uri = result.uri

                Glide.with(applicationContext)
                    .load(File(uri.path!!))
                    .into(binding.avatarup)

                imgFilePp = File(uri.path!!)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val exception = result.error
                Toast.makeText(this, "" + exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadfoto(){
        binding.layoutavaup.setOnClickListener {
            EasyImage.openChooserWithGallery(
                this@UpPhotoProfileActivity,
                "Pilih",
                1
            )
        }

        binding.btnUploadFoto.setOnClickListener {
            if (binding.avatarup.drawable == null) {
                val snackbar: Snackbar = Snackbar.make(
                    binding.layoutavaup,
                    "Tambahkan Foto Profile Terlebih dahulu",
                    Snackbar.LENGTH_LONG
                )
                snackbar.setBackgroundTint(Color.RED)
                val textView = snackbar.view.findViewById(R.id.snackbar_action) as TextView
                textView.isAllCaps = false
                textView.isSingleLine = true
                val imgWarning = ImageView(applicationContext)
                imgWarning.scaleType = ImageView.ScaleType.CENTER_INSIDE
                val layImageParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                imgWarning.setImageResource(R.drawable.ic_warning)
                (textView.parent as SnackbarContentLayout).addView(imgWarning, layImageParams)
                imgWarning.setOnClickListener { snackbar.dismiss() }
                snackbar.show()
            } else {
                uppp()
            }
        }

    }

    private fun lewati() {
        binding.tvLewatiUpload.setOnClickListener {
            intent = Intent(this@UpPhotoProfileActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(
                applicationContext,
                "Tekan kembali sekali lagi untuk keluar",
                Toast.LENGTH_SHORT
            ).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}