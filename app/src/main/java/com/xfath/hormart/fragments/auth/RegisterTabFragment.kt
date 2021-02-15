package com.xfath.hormart.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.xfath.hormart.R
import com.xfath.hormart.activity.UpPhotoProfileActivity
import com.xfath.hormart.api.ApiConfig
import com.xfath.hormart.helper.SharedPreference
import com.xfath.hormart.model.ResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterTabFragment : Fragment() {

    lateinit var s: SharedPreference
    private lateinit var btnReg: Button
    private lateinit var etNama: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etPass: EditText
    private lateinit var pbreg: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register_tab, container, false)
        init(view)

        setUpBtnRegistrasi()

        return view

    }

    private fun setUpBtnRegistrasi() {
        btnReg.setOnClickListener {
            if (etNama.text.isEmpty()) {
                etNama.error = "Kolom Nama tidak boleh kosong"
                etNama.requestFocus()
                return@setOnClickListener
            } else if (etEmail.text.isEmpty()) {
                etEmail.error = "Kolom Email tidak boleh kosong"
                etEmail.requestFocus()
                return@setOnClickListener
            } else if (etPhone.text.isEmpty()) {
                etPhone.error = "Kolom Nomor Handphone tidak boleh kosong"
                etPhone.requestFocus()
                return@setOnClickListener
            } else if (etPass.text.isEmpty()) {
                etPass.error = "Kolom Kata Sandi tidak boleh kosong"
                etPass.requestFocus()
                return@setOnClickListener
            }
            s = SharedPreference(activity!!)
            pbreg.visibility = View.VISIBLE
            ApiConfig.instanceRetrofit.register(
                etNama.text.toString(),
                etEmail.text.toString(),
                etPhone.text.toString(),
                etPass.text.toString()
            ).enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    val respon = response.body()!!
                    if (respon.success == 1) {
                        pbreg.visibility = View.GONE
                        s.setStatusLogin(true)
                        s.setUser(respon.user)
//                        s.setString(s.nama, respon.user.name)
//                        s.setString(s.phone, respon.user.phone)
//                        s.setString(s.email, respon.user.email)

                        val intent = Intent(requireContext(), UpPhotoProfileActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        activity!!.finish()
                        Toast.makeText(
                            requireContext(),
                            "Silahkan Unggah Foto Profile" , Toast.LENGTH_SHORT
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
                    pbreg.visibility = View.GONE
                    Toast.makeText(requireContext(), "Error:" + t.message, Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
    }

    private fun init(view: View) {
        /*fabLeft = view.findViewById(R.id.fab_left)
        fabMid = view.findViewById(R.id.fab_mid)
        fabRight = view.findViewById(R.id.fab_right)*/
        etNama = view.findViewById(R.id.et_nama_reg)
        etEmail = view.findViewById(R.id.et_email_reg)
        etPhone = view.findViewById(R.id.et_phone)
        etPass = view.findViewById(R.id.et_password_reg)
        btnReg = view.findViewById(R.id.btn_register)
        pbreg = view.findViewById(R.id.pbreg)
    }


}