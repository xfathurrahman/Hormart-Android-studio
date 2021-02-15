package com.xfath.hormart.model

class ResponseModel {

    var success : Int? = null
    val value : Boolean? = null
    var message : String? = null

    lateinit var user : Users

    lateinit var photos: Photos

    var products: ArrayList<Products> = ArrayList()

}

