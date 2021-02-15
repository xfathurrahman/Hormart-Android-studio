package com.xfath.hormart.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.xfath.hormart.model.Users

class SharedPreference(activity: Activity) {


/*    val nama = "nama"
    val phone = "phone"
    val email = "email"
    val id = "id"*/


    private val login = "Login"
    val user = "user"

    private val mypref = "MAIN_PREF"
    private val sp: SharedPreferences = activity.getSharedPreferences(mypref, Context.MODE_PRIVATE)

    fun setStatusLogin(status: Boolean) {
        sp.edit().putBoolean(login, status).apply()
    }

    fun getStatusLogin(): Boolean {
        return sp.getBoolean(login, false)
    }

    fun setUser(value: Users) {
        val data = Gson().toJson(value, Users::class.java)
        sp.edit().putString(user, data).apply()
    }

    fun getUser(): Users? {
        val data = sp.getString(user, null) ?: return null
        return Gson().fromJson<Users>(data, Users::class.java)
    }

/*    fun setString(key: String, value: String){
        sp.edit().putString(key, value).apply()
    }

    fun getString(key: String): String{
        return sp.getString(key, "")!!
    }*/
}