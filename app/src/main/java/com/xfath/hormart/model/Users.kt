package com.xfath.hormart.model

import com.google.gson.annotations.SerializedName

data class Users(

    @SerializedName("id")
    var Id: String? = null,

    @SerializedName("name")
    var userName: String? = null,

    @SerializedName("email")
    var userEmail: String? = null,

    @SerializedName("phone")
    var userPhone: String? = null,

    @SerializedName("profile_photo_url")
    var userPpDefault: String? = null,

    var photos: Photos

)