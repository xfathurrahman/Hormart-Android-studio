package com.xfath.hormart.fragments.myadsfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import com.rezwan.knetworklib.KNetwork
import com.xfath.hormart.R
import com.xfath.hormart.utils.NetworkConnection

class MyAdsFragment : Fragment() {

    private lateinit var mCroutonMyAds : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_myads, container, false)
        init(view)
        connection()

        return view

    }

    private fun connection() {
        val networkConnection = NetworkConnection(activity!!.applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {
                mCroutonMyAds.visibility = View.GONE
            }
            else {
                mCroutonMyAds.visibility = View.VISIBLE
            }
        })
    }


    private fun init(view: View){
        mCroutonMyAds = view.findViewById(R.id.crouton_myads)
    }

}