package com.xfath.hormart.fragments.profilefragment.profilechildfragments.childeditprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.xfath.hormart.R


class ChildEditProfileFragmentPribadiGantiSandi : Fragment() {

    private lateinit var etSandiSkrg : EditText
    private lateinit var etSandiBaru : EditText
    private lateinit var etSandiUlangi : EditText
    private lateinit var btnKembali : Button
    private lateinit var btnLupaSandi : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_child_editprofile_pribadi_katasandi, container, false)
        init(view)
        kembali()
//        lupasandi()

        return view
    }

/*    private fun lupasandi() {
        btnKembali.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(R.id.containergantisandi, ChildEditProfileFragmentPribadi())
                .commit()
        }
    }*/

    private fun kembali() {
        btnKembali.setOnClickListener {
        childFragmentManager.beginTransaction()
            .replace(R.id.containergantisandi, ChildEditProfileFragmentPribadi())
            .commit()
        }
    }

    private fun init(view: View) {
        etSandiSkrg = view.findViewById(R.id.et_katasandi_skrg_sheet)
        etSandiBaru = view.findViewById(R.id.et_katasandi_baru_sheet)
        etSandiUlangi = view.findViewById(R.id.et_katasandi_ulang_sheet)
        btnKembali = view.findViewById(R.id.kembalidrgantisandi)
/*        btnLupaSandi = view.findViewById(R.id.recovery_sandi)*/
    }

}