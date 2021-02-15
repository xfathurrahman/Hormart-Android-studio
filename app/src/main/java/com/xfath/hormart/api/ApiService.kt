package com.xfath.hormart.api

import com.xfath.hormart.model.Photos
import com.xfath.hormart.model.ResponseModel
import com.xfath.hormart.model.Users
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.lang.reflect.Method


interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("phone") phone: String,
            @Field("password") password: String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("login")
    fun login(
            @Field("email") email: String,
            @Field("password") password: String
    ): Call<ResponseModel>

    @Multipart
    @POST("addproduct")
    fun addproduct(
            @Part("user_id") uid: RequestBody,
            @Part("nama") nama: RequestBody,
            @Part("harga") harga: RequestBody,
            @Part("deskripsi") deskripsi: RequestBody,
            @Part("kategori_id") kategori: RequestBody,
            @Part gambar: MultipartBody.Part
    ): Call<ResponseModel>

    @GET("getproduct")
    fun getproduct(): Call<ResponseModel>

    @Multipart
    @POST("addpp")
    fun addpp(
            @Part("photo_id") pid: RequestBody,
            @Part photoprofile: MultipartBody.Part
    ): Call<ResponseModel>

    @GET("getowner/{id}")
    suspend fun getUsers(@Path("id") UsersId: String): Response<Users>

    @Multipart
    @POST("editprofile/{id}")
    suspend fun updatePhoto(
            @Path("id") photoId: String,
            @Part("photo_id") pid: RequestBody,
            @Part photoprofile: MultipartBody.Part
    ): Response<ResponseBody>


}