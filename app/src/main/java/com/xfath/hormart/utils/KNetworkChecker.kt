package com.xfath.hormart.utils

import android.app.Application
import com.rezwan.knetworklib.KNetwork

class KNetworkChecker: Application() {
    override fun onCreate() {
        super.onCreate()
        KNetwork.initialize(this)
    }
}