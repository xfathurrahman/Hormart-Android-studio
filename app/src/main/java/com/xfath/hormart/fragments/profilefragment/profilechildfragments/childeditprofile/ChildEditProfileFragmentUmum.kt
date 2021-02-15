package com.xfath.hormart.fragments.profilefragment.profilechildfragments.childeditprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.xfath.hormart.R
import com.xfath.hormart.helper.SharedPreference


class ChildEditProfileFragmentUmum : Fragment() {

    private lateinit var etNamaPenggunaUmum : TextView
    private lateinit var etPhone : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_child_editprofile_umum, container, false)
        init(view)

        val s = SharedPreference(activity!!)
        val user = s.getUser()!!

        etNamaPenggunaUmum.text = user.userName
        etPhone.text = user.userPhone

        return view
    }

    private fun init(view: View) {
        etNamaPenggunaUmum = view.findViewById(R.id.et_nama_pengguna_sheet)
        etPhone = view.findViewById(R.id.et_no_hp_sheet)
    }

}