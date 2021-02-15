package com.xfath.hormart.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xfath.hormart.databinding.ActivityMainBinding
import com.xfath.hormart.helper.SharedPreference

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var s: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        s = SharedPreference(this)
    }

}
