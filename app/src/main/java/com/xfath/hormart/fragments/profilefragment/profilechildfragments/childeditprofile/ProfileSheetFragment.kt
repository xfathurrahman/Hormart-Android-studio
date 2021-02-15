package com.xfath.hormart.fragments.profilefragment.profilechildfragments.childeditprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.xfath.hormart.R
import com.xfath.hormart.adapters.ViewPagerAdapter
import com.xfath.hormart.helper.SharedPreference


class ProfileSheetFragment : BottomSheetDialogFragment() {

    private lateinit var s : SharedPreference

    private lateinit var viewPager : ViewPager
    private lateinit var tabLayout : SmartTabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.child_profile_sheet_fragment, container, false)

        s = SharedPreference(activity!!)
        init(view)
        setUpTabs()


        return view
    }

    private fun setUpTabs() {

        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(ChildEditProfileFragmentUmum(), "Umum")
        adapter.addFragment(ChildEditProfileFragmentPribadi(), "Pribadi")
        viewPager.adapter = adapter
        tabLayout.setViewPager(viewPager)
        viewPager.currentItem = 0

    }

    private fun init(view: View){
        viewPager = view.findViewById(R.id.view_pager_editprofile)
        tabLayout = view.findViewById(R.id.tabs_edit_profile)

    }

}
