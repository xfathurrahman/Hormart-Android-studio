package com.xfath.hormart.fragments.homefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.xfath.hormart.R
import com.xfath.hormart.adapters.ViewPagerAdapter
import com.xfath.hormart.fragments.homefragment.homechildfragments.ChildFavouritesFragment
import com.xfath.hormart.fragments.homefragment.homechildfragments.ChildHomeFragment
import com.xfath.hormart.fragments.homefragment.homechildfragments.ChildMenuFragment


class HomeFragment : Fragment() {

    private lateinit var viewPager : ViewPager
    private lateinit var tabLayout : SmartTabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        init(view)
        setUpTabs()


        return view
    }

    private fun setUpTabs() {

        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(ChildMenuFragment(), "menu")
        adapter.addFragment(ChildHomeFragment(), "home")
        adapter.addFragment(ChildFavouritesFragment(), "favorit")

        viewPager.adapter = adapter
        tabLayout.setViewPager(viewPager)
        viewPager.currentItem = 1
        viewPager.pageMargin = 20

    }

    private fun init(view: View) {
        viewPager = view.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tabs)
    }

    override fun onStop() {
        super.onStop()
        activity!!.finish()
    }

}