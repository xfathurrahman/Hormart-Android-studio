package com.xfath.hormart.fragments.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.xfath.hormart.R

class DisconnectedSheetFragment : SuperBottomSheetFragment() {

    private lateinit var mBtnOpenSetting: Button
    private lateinit var containerdc: LinearLayout

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_sheet_disconnected, container, false)
        init(view)
        OpenSet()


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun OpenSet() {
        mBtnOpenSetting.setOnClickListener {
            val dialogIntent = Intent(Settings.ACTION_SETTINGS)
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(dialogIntent)
            containerdc.visibility = View.GONE
        }
    }

    private fun init(view: View){
        mBtnOpenSetting = view.findViewById(R.id.btn_opn_set_main)
        containerdc = view.findViewById(R.id.containerdc)
    }

}
