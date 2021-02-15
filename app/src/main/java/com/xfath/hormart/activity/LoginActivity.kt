package com.xfath.hormart.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xfath.hormart.adapters.ViewPagerAdapter
import com.xfath.hormart.databinding.ActivityLoginBinding
import com.xfath.hormart.fragments.auth.LoginTabFragment
import com.xfath.hormart.fragments.auth.RegisterTabFragment

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpTabs()
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(LoginTabFragment(), "Masuk")
        adapter.addFragment(RegisterTabFragment(), "Daftar")

        binding.viewPager.adapter = adapter
        binding.smartTabLayout.setViewPager(binding.viewPager)
        binding.viewPager.currentItem = 0

    }

    override fun onBackPressed() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}