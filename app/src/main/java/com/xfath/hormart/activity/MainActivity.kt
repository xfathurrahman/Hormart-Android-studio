package com.xfath.hormart.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.rezwan.knetworklib.KNetwork
import com.xfath.hormart.R
import com.xfath.hormart.databinding.ActivityMainBinding
import com.xfath.hormart.fragments.*
import com.xfath.hormart.fragments.chatfragment.ChatFragment
import com.xfath.hormart.fragments.homefragment.HomeFragment
import com.xfath.hormart.fragments.myadsfragment.MyAdsFragment
import com.xfath.hormart.fragments.profilefragment.ProfileFragment
import com.xfath.hormart.fragments.profilefragment.ProfilePhotoDetailFragment
import com.xfath.hormart.helper.Communicator
import com.xfath.hormart.helper.SharedPreference
import kotlinx.android.synthetic.main.fragment_child_home.*
import kotlinx.android.synthetic.main.fragment_home.*


class MainActivity : AppCompatActivity(), KNetwork.OnNetWorkConnectivityListener, Communicator {

    private lateinit var binding: ActivityMainBinding
    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var s: SharedPreference

    private var backPressedTime = 0L
    private var active: Fragment = HomeFragment()
    private val homeFragment: Fragment = HomeFragment()
    private val chatFragment: Fragment = ChatFragment()
    private val myadsFragment: Fragment = MyAdsFragment()
    private val profileFragment: Fragment = ProfileFragment()
    private val fm: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        s = SharedPreference(this)

        goJual()
        setUpBottomNav()
        bindKNetwork()



    }

    // KNetwork start
    private fun bindKNetwork() {
        KNetwork.bind(this, lifecycle)
            .setViewGroupResId(R.id.crouton_main)// by default we will show our croutons into top
            .setConnectivityListener(this)
    }

    override fun onNetConnected() {
        Log.i("Main", "onNetConnected")
    }

    override fun onNetDisConnected() {
        Log.i("Main", "onNetDisConnected")
    }
    // KNetwork end

    //button jual
    private fun goJual() {
        binding.fabJual.setOnClickListener {
            if (s.getStatusLogin()) {
                val intent = Intent(this@MainActivity, JualanActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    //BottomNav
    private fun setUpBottomNav() {
        binding.bottomNav.itemIconTintList = null

        fm.beginTransaction().add(R.id.container, homeFragment).show(homeFragment).commit()
        fm.beginTransaction().add(R.id.container, chatFragment).hide(chatFragment).commit()
        fm.beginTransaction().add(R.id.container, myadsFragment).hide(myadsFragment).commit()
        fm.beginTransaction().add(R.id.container, profileFragment).hide(profileFragment).commit()

        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    callFragment(0, homeFragment)
                    view_pager.currentItem = 1
                    if (homeFragment.isAdded){
                        when (item.itemId){
                            R.id.home -> {
                                sv_child_home.fullScroll(NestedScrollView.FOCUS_UP)
                            }
                        }
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.chat -> {
                    if (s.getStatusLogin()) {
                        callFragment(1, chatFragment)
                    } else {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.myads -> {
                    if (s.getStatusLogin()) {
                        callFragment(3, myadsFragment)
                    } else {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    if (s.getStatusLogin()) {
                        callFragment(4, profileFragment)
                    } else {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
        binding.bottomNav.getOrCreateBadge(R.id.chat).apply {
            number = 10
        }
    }

    //callfragments
    private fun callFragment(int: Int, fragment: Fragment) {
        menu = binding.bottomNav.menu
        menuItem = menu.getItem(0)
        menuItem = menu.getItem(int)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
        return
    }

    //onback
    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(
                applicationContext,
                "Tekan kembali sekali lagi untuk keluar",
                Toast.LENGTH_SHORT
            ).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    override fun passDataPp(textfromprofile: CharSequence) {
        val bundle = Bundle()
        bundle.putCharSequence("message", textfromprofile)

        val transaction =  this.supportFragmentManager.beginTransaction()
        val fragmentdetail = ProfilePhotoDetailFragment()
        fragmentdetail.arguments = bundle

        transaction.replace(R.id.container_profile, fragmentdetail)
        transaction.commit()
    }

}
