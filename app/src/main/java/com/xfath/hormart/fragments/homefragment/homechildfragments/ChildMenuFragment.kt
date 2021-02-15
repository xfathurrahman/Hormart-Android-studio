package com.xfath.hormart.fragments.homefragment.homechildfragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import com.xfath.hormart.R
import com.xfath.hormart.activity.LoginActivity
import com.xfath.hormart.api.ApiService
import com.xfath.hormart.databinding.FragmentChildMenuBinding
import com.xfath.hormart.helper.SharedPreference
import com.xfath.hormart.utils.Config
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ChildMenuFragment : Fragment(){

    private var _binding: FragmentChildMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var s : SharedPreference
    private lateinit var btnlogin : Button
    private lateinit var btnkluar : Button
    private lateinit var tvNama : TextView
    private lateinit var tvEmail : TextView
    private lateinit var tvPhone : TextView
    private lateinit var tvUidMenu : TextView
    private lateinit var tvSilahkan : TextView
    private lateinit var ivAvaMenu : CircleImageView
    private lateinit var svMenu : NestedScrollView
    private lateinit var mSwipeRefreshLayout : SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChildMenuBinding.inflate(inflater, container, false)
        val view = binding.root

        init(view)
        s = SharedPreference(activity!!)
        setData()
        setBtnLogin()
        setBtnKeluar()
        swipeRefresh()

        return view
    }

    private fun swipeRefresh() {
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(
            requireContext(),
            R.color.red))
        mSwipeRefreshLayout.setColorSchemeColors(Color.WHITE)

        mSwipeRefreshLayout.setOnRefreshListener{
            setData()
            parsePhotoProfile()

            mSwipeRefreshLayout.isRefreshing = false
        }

    }

    private fun setBtnKeluar() {
        btnkluar.setOnClickListener {
            s.setStatusLogin(false)
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            return@setOnClickListener
        }
    }

    private fun setBtnLogin() {
        btnlogin.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData(){
        if (s.getStatusLogin()) {

            parsePhotoProfile()

            val user = s.getUser()!!
            tvNama.text = user.userName
            tvEmail.text = user.userEmail
            tvPhone.text = user.userPhone

            tvSilahkan.visibility = View.GONE
            btnlogin.visibility = View.GONE

            tvEmail.visibility = View.VISIBLE
            tvNama.visibility = View.VISIBLE
            tvPhone.visibility = View.VISIBLE
            btnkluar.visibility = View.VISIBLE

        } else {
            tvNama.text = null
            tvEmail.text = null
            tvPhone.text = null
        }
    }

    private fun parsePhotoProfile(){
        // Create Retrofit
        val retrofit = Retrofit.Builder()
                .baseUrl(Config.BASE_URL + "api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Create Service
        val service = retrofit.create(ApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {

            try {

                // Get User ID dari Shared Pref
                val user = s.getUser()!!
                val uid = user.Id.also { tvUidMenu.text = it }
                val response = service.getUsers("$uid")

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        // Convert raw JSON to pretty JSON using GSON library
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val prettyJson = gson.toJson(response.body())

                        Log.d("Pretty Printed JSON :", prettyJson)

                        // Photo Profile User
                        if (response.body()?.photos == null) {
                            val defaultPp = response.body()?.userPpDefault
                            Picasso.get()
                                    .load(defaultPp)
                                    .error(R.drawable.ic_user)
                                    .into(ivAvaMenu)
                        } else {
                            val imgMainAva =
                                    Config.smallphotoprofil + response.body()?.photos?.userPhotoProfile
                            Picasso.get()
                                    .load(imgMainAva)
                                    .error(R.drawable.ic_user)
                                    .into(ivAvaMenu)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("ERROR 404", e.toString())
            }

        }
    }

    private fun init(view: View) {
        tvUidMenu = view.findViewById(R.id.tv_uid_menu)
        tvEmail = view.findViewById(R.id.tv_email_menu)
        tvNama = view.findViewById(R.id.tv_nama_menu)
        tvSilahkan = view.findViewById(R.id.tv_silahkanmasukordaftar)
        tvPhone = view.findViewById(R.id.tv_no_hp)
        btnlogin = view.findViewById(R.id.btn_login_menu)
        btnkluar = view.findViewById(R.id.btn_keluar_menu)
        ivAvaMenu = view.findViewById(R.id.iv_ava_menu)
        svMenu = view.findViewById(R.id.sv_child_menu)
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_menu)
    }

}
