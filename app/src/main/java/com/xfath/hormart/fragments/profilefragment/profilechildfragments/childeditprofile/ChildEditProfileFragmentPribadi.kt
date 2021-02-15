package com.xfath.hormart.fragments.profilefragment.profilechildfragments.childeditprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.xfath.hormart.R
import com.xfath.hormart.helper.SharedPreference


class ChildEditProfileFragmentPribadi : Fragment() {

    private lateinit var btnGantiSandi : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_child_editprofile_pribadi, container, false)
        init(view)
        btnGantiSandi()

/*        val s = SharedPreference(activity!!)
        val user = s.getUser()!!*/

        return view
    }

    private fun btnGantiSandi(){
        btnGantiSandi.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(R.id.containerpribadi, ChildEditProfileFragmentPribadiGantiSandi())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun init(view: View) {
        btnGantiSandi = view.findViewById(R.id.btn_ganti_sandi)
    }

}