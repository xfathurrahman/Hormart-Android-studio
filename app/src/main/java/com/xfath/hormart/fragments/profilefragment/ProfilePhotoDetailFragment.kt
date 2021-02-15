package com.xfath.hormart.fragments.profilefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xfath.hormart.R
import com.xfath.hormart.databinding.DetailProfilePhotoFragmentBinding

class ProfilePhotoDetailFragment : Fragment() {

    var displayMessage: String? = null

    private var _binding: DetailProfilePhotoFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DetailProfilePhotoFragmentBinding.inflate(inflater, container, false)

        displayMessage = arguments?.getString("message")
        binding.tvdetailfoto.text = displayMessage

        binding.btnBack.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(R.id.container_detailfoto, ProfileFragment())
                .commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}