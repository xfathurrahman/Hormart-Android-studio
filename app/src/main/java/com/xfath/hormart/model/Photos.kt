package com.xfath.hormart.model

import com.google.gson.annotations.SerializedName

data class Photos(

    @SerializedName("id")
    var Id: String? = null,

    @SerializedName("photo_id")
    var photoId: String? = null,

    @SerializedName("photoprofile")
    var userPhotoProfile: String? = null,

    )
