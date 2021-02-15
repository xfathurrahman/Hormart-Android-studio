package com.xfath.hormart.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.xfath.hormart.R
import com.xfath.hormart.activity.MainActivity
import com.xfath.hormart.api.ApiConfig
import com.xfath.hormart.helper.SharedPreference
import com.xfath.hormart.model.ResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginTabFragment : Fragment() {

    lateinit var s: SharedPreference
    private lateinit var fabLeft: FloatingActionButton
    private lateinit var fabMid: FloatingActionButton
    private lateinit var fabRight: FloatingActionButton
    private lateinit var btnLogin: Button
    private lateinit var linierEmail: LinearLayout
    private lateinit var linierPass: LinearLayout
    private lateinit var tvLupa: TextView
    private lateinit var etEmailLog: EditText
    private lateinit var etPassLog: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_tab, container, false)
        init(view)


        setUpButtonLogin()
        setUpAnim()

        return view

    }

    private fun setUpButtonLogin() {
        btnLogin.setOnClickListener {
            if (etEmailLog.text.isEmpty()) {
                etEmailLog.error = "Kolom Email tidak boleh kosong"
                etEmailLog.requestFocus()
                return@setOnClickListener
            } else if (etPassLog.text.isEmpty()) {
                etPassLog.error = "Kolom Kata Sandi tidak boleh kosong"
                etPassLog.requestFocus()
                return@setOnClickListener
            }
            s = SharedPreference(activity!!)
//            pblogin.visibility = View.VISIBLE
            ApiConfig.instanceRetrofit.login(etEmailLog.text.toString(), etPassLog.text.toString())
                .enqueue(object :
                    Callback<ResponseModel> {
                    override fun onResponse(
                        call: Call<ResponseModel>,
                        response: Response<ResponseModel>
                    ) {
//                        pblogin.visibility = View.GONE
                        val respon = response.body()!!
                        if (respon.success == 1) {
                            s.setStatusLogin(true)

                            s.setUser(respon.user)

                            val intent = Intent(requireContext(), MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            activity!!.finish()
                            Toast.makeText(
                                requireContext(),
                                "Selamat datang Kembali " + respon.user.userName,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Error : " + respon.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
//                        pblogin.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error:" + t.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }
    }

    private fun setUpAnim() {
        fabLeft.outlineProvider = null
        fabMid.outlineProvider = null
        fabRight.outlineProvider = null

        fabLeft.translationY = 500F
        fabMid.translationY = 300F
        fabRight.translationY = 300F

        fabLeft.animate().translationY(0F).alpha(1F).setDuration(800).setStartDelay(1000).start()
        fabMid.animate().translationY(0F).alpha(1F).setDuration(1000).setStartDelay(1000).start()
        fabRight.animate().translationY(0F).alpha(1F).setDuration(1200).setStartDelay(1000).start()

        btnLogin.translationX = 1000F
        linierEmail.translationX = 1000F
        linierPass.translationX = 1000F
        tvLupa.translationX = 800F

        btnLogin.animate().translationX(0F).alpha(1F).setDuration(1100).setStartDelay(1000).start()
        linierEmail.animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(1000)
            .start()
        linierPass.animate().translationX(0F).alpha(1F).setDuration(1000).setStartDelay(1000)
            .start()
        tvLupa.animate().translationX(0F).alpha(1F).setDuration(1200).setStartDelay(1000).start()
    }

    private fun init(view: View) {
        fabLeft = view.findViewById(R.id.fab_left)
        fabMid = view.findViewById(R.id.fab_mid)
        fabRight = view.findViewById(R.id.fab_right)
        btnLogin = view.findViewById(R.id.btn_login)
        linierEmail = view.findViewById(R.id.linier_email)
        linierPass = view.findViewById(R.id.linier_pass)
        tvLupa = view.findViewById(R.id.lupa_sandi)
        etEmailLog = view.findViewById(R.id.et_email_login)
        etPassLog = view.findViewById(R.id.et_password_login)
    }

}