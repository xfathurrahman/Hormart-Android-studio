package com.xfath.hormart.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.xfath.hormart.R
import com.xfath.hormart.api.ApiConfig.instanceRetrofit
import com.xfath.hormart.databinding.ActivityDetailProfilePictureBinding
import com.xfath.hormart.helper.SharedPreference
import com.xfath.hormart.utils.Config
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

class DetailProfilePictureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProfilePictureBinding
    private lateinit var s: SharedPreference
    private lateinit var imgFilePpNew: File

    private val permissions = arrayListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfilePictureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        s = SharedPreference(this)

        val user = s.getUser()!!
        user.Id.also { binding.tvUserId.text = it }

        CheckPermission()
        GetProfile()
        btnOnClick()
    }

    private fun btnOnClick() {
        binding.btnCloseDetailPp.setOnClickListener {
            val intent = Intent(this@DetailProfilePictureActivity , MainActivity::class.java)
            startActivity(intent)
        }
        binding.btnEditPp.setOnClickListener {
            EasyImage.openChooserWithGallery(
                this,
                "Pilih",
                1
            )
        }
        binding.btnSimpanPpnew.setOnClickListener {
            uploadnewprofile()
            val intent = Intent(this@DetailProfilePictureActivity, MainActivity::class.java)
            intent.putExtra("resId", imgFilePpNew)
            startActivity(intent)
        }
    }

    private fun CheckPermission() {
        if (!checkPermissions(this, permissions.toString())) {
            ActivityCompat.requestPermissions(
                this,
                permissions.toTypedArray(),
                PERMISSION_REQUEST_CODE)
        }
        checkPermissions(this)
    }

    private fun GetProfile() {

        CoroutineScope(Dispatchers.IO).launch {

            val pid: String = binding.tvUserId.text.toString()

            try {
                val response = instanceRetrofit.getUsers(pid)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Photo Profile User

                        binding.tvPhotoId.text = response.body()?.photos?.Id

                        if (response.body()?.photos == null) {
                            val defaultPp = response.body()?.userPpDefault
                            Picasso.get()
                                    .load(defaultPp)
                                    .error(R.drawable.ic_user)
                                    .into(binding.ivdetailpp)
                        } else {
                            val imgMainAva =
                                    Config.smallphotoprofil + response.body()?.photos?.userPhotoProfile
                            Picasso.get()
                                    .load(imgMainAva)
                                    .error(R.drawable.ic_user)
                                    .into(binding.ivdetailpp)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("ERROR 404", e.toString())
            }

        }
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
                    binding.btnSimpanPpnew.visibility = View.VISIBLE
                    CropImage.activity(Uri.fromFile(imageFile))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setFixAspectRatio(true)
                        .start(this@DetailProfilePictureActivity)
                }

                override fun onImagePickerError(e: Exception, source: EasyImage.ImageSource, type: Int) {
                    super.onImagePickerError(e, source, type)
                    Toast.makeText(this@DetailProfilePictureActivity, "" + e.message, Toast.LENGTH_SHORT).show()
                }

            })

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {

                val uri: Uri = result.uri

                Glide.with(applicationContext)
                    .load(File(uri.path!!))
                    .into(binding.ivdetailpp)

                imgFilePpNew = File(uri.path!!)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val exception = result.error
                Toast.makeText(this, "" + exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun uploadnewprofile() {

        /*// Old style
        pg_edit_pp.visibility = View.VISIBLE

        val photoId: String = tv_photo_id.text.toString()
        val requestBody: RequestBody = tv_user_id.text.toString().trim().toRequestBody("text/plain".toMediaTypeOrNull())
        val requestFiles = imgFilePpNew.asRequestBody("multipart/from-data".toMediaTypeOrNull())
        val photoprofilenew = MultipartBody.Part.createFormData("photoprofile", imgFilePpNew.name, requestFiles)

        instanceRetrofit.updatePhoto(photoId, requestBody, photoprofilenew)
                .enqueue(object : Callback<ResponseModel> {
                    override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                        Toast.makeText(this@DetailProfilePictureActivity, t.message, Toast.LENGTH_SHORT).show()
                        pg_edit_pp.visibility = View.GONE
                    }
                    override fun onResponse(
                            call: Call<ResponseModel>,
                            response: Response<ResponseModel>
                    ) {
                        val respon = response.body()!!
                        if (respon.success == 1) {
                            pg_edit_pp.visibility = View.GONE
                            Toast.makeText(
                                    this@DetailProfilePictureActivity,
                                    "" + respon.message, Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                    this@DetailProfilePictureActivity,
                                    "Error : " + respon.message,
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })*/

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val photoId: String = binding.tvPhotoId.text.toString()
                val requestBody: RequestBody = binding.tvUserId.text.toString().trim().toRequestBody("text/plain".toMediaTypeOrNull())
                val requestFiles = imgFilePpNew.asRequestBody("multipart/from-data".toMediaTypeOrNull())
                val photoprofilenew = MultipartBody.Part.createFormData("photoprofile", imgFilePpNew.name, requestFiles)

                val response = instanceRetrofit.updatePhoto(photoId , requestBody , photoprofilenew)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                                this@DetailProfilePictureActivity,
                                "Sukses Update Profile" , Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                                this@DetailProfilePictureActivity,
                                "Error : " + response.message(),
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (t: Throwable) {
                Log.e("ERROR 404", t.toString())
            }
        }
    }

    private fun checkPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
                ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@DetailProfilePictureActivity , MainActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }

}
