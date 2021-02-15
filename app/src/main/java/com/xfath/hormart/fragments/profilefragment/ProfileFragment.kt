package com.xfath.hormart.fragments.profilefragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.squareup.picasso.Picasso
import com.xfath.hormart.R
import com.xfath.hormart.activity.BaseActivity
import com.xfath.hormart.activity.DetailProfilePictureActivity
import com.xfath.hormart.activity.LoginActivity
import com.xfath.hormart.activity.SettingActivity
import com.xfath.hormart.api.ApiConfig.instanceRetrofit
import com.xfath.hormart.databinding.FragmentProfileBinding
import com.xfath.hormart.fragments.profilefragment.profilechildfragments.childeditprofile.ProfileSheetFragment
import com.xfath.hormart.helper.Communicator
import com.xfath.hormart.helper.SharedPreference
import com.xfath.hormart.utils.Config
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.roundToInt

class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var communicator: Communicator
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val sheet = ProfileSheetFragment()
    private lateinit var s: SharedPreference
    /*Collapse toolbar*/
    private var horizontalToolbarAvatarMargin: Float = 0F
    private var cashCollapseState: Pair<Int, Int>? = null
    private var avatarAnimateStartPointY: Float = 0F
    private var avatarCollapseAnimationChangeWeight: Float = 0F
    private var isCalculated = false
    private var verticalToolbarAvatarMargin = 0F

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        s = SharedPreference(activity!!)

        setData()
        collapseAnimate()
        setDrawer()
        swipeRefresh(view)
        buttonOnClick()

        communicator = activity as Communicator

        return view
    }

    private fun buttonOnClick() {
        binding.appbarlayout.maincontent.btnEditProfile.setOnClickListener {
            sheet.show(childFragmentManager, "BottomSheetFragmentEditProfile")
        }
        binding.appbarlayout.btnsetings.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }
        binding.btnKeluarProfile.setOnClickListener {
            s.setStatusLogin(false)
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            return@setOnClickListener
        }
        binding.appbarlayout.btnshare.setOnClickListener {
            communicator.passDataPp(binding.appbarlayout.tvNamaPengguna.text.toString())
            childFragmentManager.beginTransaction()
                .replace(R.id.container_profile,ProfilePhotoDetailFragment()).commit()
        }
        binding.appbarlayout.mainProfileAvatar.setOnClickListener {
            val intent = Intent(requireContext(), DetailProfilePictureActivity::class.java)
/*            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)*/
            startActivity(intent)
            activity!!.finish()
        }
    }
    private fun swipeRefresh(view: View) {
        val mSwipeRefreshLayout : SwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_profile)
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(
            requireContext(),
            R.color.blue))
        mSwipeRefreshLayout.setColorSchemeColors(Color.WHITE)

        mSwipeRefreshLayout.setOnRefreshListener{
            setData()
            getProfile()
            mSwipeRefreshLayout.isRefreshing = false
        }
    }
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_nav_setting -> {
                avoidDoubleClicks(binding.tvNavSetting)
                val intent = Intent(requireContext(), SettingActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }
    private fun setDrawer() {
        binding.drawerLayout.setScrimColor(Color.TRANSPARENT)
        binding.tvNavSetting.setOnClickListener(this)
    }
    private fun avoidDoubleClicks(view: View) {
        if (!view.isClickable) {
            return
        }
        view.isClickable = false
        view.postDelayed({ view.isClickable = true }, DELAY_IN_MS)
    }
    private fun collapseAnimate() {
        EXPAND_AVATAR_SIZE = resources.getDimension(R.dimen.default_expanded_image_size)
        COLLAPSE_IMAGE_SIZE = resources.getDimension(R.dimen.default_collapsed_image_size)
        horizontalToolbarAvatarMargin = resources.getDimension(R.dimen.activity_margin)
        (binding.appbarlayout.animToolbar.height - COLLAPSE_IMAGE_SIZE) * 2
        binding.appbarlayout.appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
                if (isCalculated.not()) {
                    avatarAnimateStartPointY =
                        abs((appBarLayout.height - (EXPAND_AVATAR_SIZE + horizontalToolbarAvatarMargin)) / appBarLayout.totalScrollRange)
                    avatarCollapseAnimationChangeWeight = 1 / (1 - avatarAnimateStartPointY)
                    verticalToolbarAvatarMargin =
                        (binding.appbarlayout.animToolbar.height - COLLAPSE_IMAGE_SIZE) * 2
                    isCalculated = true
                }
                updateViews(abs(i / appBarLayout.totalScrollRange.toFloat()))
            }
        )
    }
    private fun updateViews(offset: Float) {
        /* apply levels changes*/
        when (offset) {
            in 0.05F..1F -> {
                binding.appbarlayout.tvNamaLengkap.apply {
                    if (visibility != View.VISIBLE) visibility = View.VISIBLE
                    alpha = (1 - offset) * 0.35F
                }
                binding.appbarlayout.tvLocationProfil.apply {
                    if (visibility != View.VISIBLE) visibility = View.VISIBLE
                    alpha = (1 - offset) * 0.35F
                }
                binding.appbarlayout.editBg.apply {
                    if (visibility != View.VISIBLE) visibility = View.VISIBLE
                    alpha = (1 - offset) * 0.35F
                }
            }

            in 0F..0.05F -> {
                binding.appbarlayout.editBg.alpha = (1f)
                binding.appbarlayout.tvLocationProfil.alpha = (1f)
                binding.appbarlayout.tvNamaLengkap.alpha = (1f)
                binding.appbarlayout.mainProfileAvatar.alpha = 1f
            }
        }

        /** collapse - expand switch*/
        when {
            offset < SWITCH_BOUND -> Pair(TO_EXPANDED, cashCollapseState?.second ?: WAIT_FOR_SWITCH)
            else -> Pair(TO_COLLAPSED, cashCollapseState?.second ?: WAIT_FOR_SWITCH)
        }.apply {
            when {
                cashCollapseState != null && cashCollapseState != this -> {
                    when (first) {
                        TO_EXPANDED -> {
                            /* set avatar on start position (center of parent frame layout)*/
                            binding.appbarlayout.mainProfileAvatar.translationX = 0F
                            /**/
                            binding.appbarlayout.flBackground.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.color_transparent
                                )
                            )
                            /* hide top titles on toolbar*/
                            binding.appbarlayout.editBg.visibility = View.VISIBLE
                            binding.appbarlayout.tvNamaPengguna.visibility = View.INVISIBLE
                        }
                        TO_COLLAPSED -> binding.appbarlayout.flBackground.apply {
                            alpha = 0F
                            setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.transparent
                                )
                            )
                            animate().setDuration(250).alpha(1.0F)
                            /* show titles on toolbar with animation*/
                            binding.appbarlayout.tvNamaPengguna.apply {
                                visibility = View.VISIBLE
                                binding.appbarlayout.editBg.visibility = View.GONE
                                alpha = 0F
                                animate().setDuration(500).alpha(1.0f)
                            }
                        }
                    }
                    cashCollapseState = Pair(first, SWITCHED)
                }
                else -> {
                    cashCollapseState = Pair(first, WAIT_FOR_SWITCH)
                }
            }

            /* Collapse avatar img*/
            binding.appbarlayout.mainProfileAvatar.apply {
                when {
                    offset > avatarAnimateStartPointY -> {
                        val avatarCollapseAnimateOffset =
                                (offset - avatarAnimateStartPointY) * avatarCollapseAnimationChangeWeight
                        val avatarSize =
                                EXPAND_AVATAR_SIZE - (EXPAND_AVATAR_SIZE - COLLAPSE_IMAGE_SIZE) * avatarCollapseAnimateOffset
                        this.layoutParams.also {
                            it.height = avatarSize.roundToInt()
                            it.width = avatarSize.roundToInt()
                        }
                        binding.appbarlayout.tvWorkaround.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            offset)

                        /*ubah -2 jadi 2 kalau mau di kanan avatarnya*/
                        this.translationX =
                                ((binding.appbarlayout.appBarLayout.width - horizontalToolbarAvatarMargin - avatarSize) / -2) * avatarCollapseAnimateOffset
                        this.translationY =
                                ((binding.appbarlayout.animToolbar.height - verticalToolbarAvatarMargin - avatarSize) / 2) * avatarCollapseAnimateOffset
                    }
                    else -> this.layoutParams.also {
                        if (it.height != EXPAND_AVATAR_SIZE.toInt()) {
                            it.height = EXPAND_AVATAR_SIZE.toInt()
                            it.width = EXPAND_AVATAR_SIZE.toInt()
                            this.layoutParams = it
                        }
                        translationX = 0f
                    }
                }
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun setData() {
        if (s.getUser() == null) {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        } else {
            getProfile()
        }
    }
    private fun getProfile() {
        // Create Retrofit
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Get User ID dari Shared Pref
                val user = s.getUser()!!
                val uid = user.Id.also { binding.appbarlayout.tvUidProfile.text = it }
                val response = instanceRetrofit.getUsers("$uid")

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // User Name
                        val userName = response.body()?.userName ?: "N/A"
                        Log.d("User Name: ", userName)
                        "@ $userName".also { binding.appbarlayout.tvNamaPengguna.text = it }

                        // User email
                        val userEmail = response.body()?.userEmail ?: "N/A"
                        Log.d("User Email: ", userEmail)
                        binding.appbarlayout.maincontent.tvEmailProfil.text = userEmail

                        // User phone
                        val userPhone = response.body()?.userPhone ?: "N/A"
                        Log.d("User No Hp: ", userPhone)
                        binding.appbarlayout.maincontent.tvNoHpProfil.text = userPhone

                        // Photo Profile User
                        if (response.body()?.photos == null) {
                            val defaultPp = response.body()?.userPpDefault
                            Picasso.get()
                                    .load(defaultPp)
                                    .error(R.drawable.ic_user)
                                    .into(binding.appbarlayout.mainProfileAvatar)
                        } else {
                            val imgMainAva =
                                    Config.smallphotoprofil + response.body()?.photos?.userPhotoProfile
                            Picasso.get()
                                    .load(imgMainAva)
                                    .error(R.drawable.ic_user)
                                    .into(binding.appbarlayout.mainProfileAvatar)
/*                            Picasso.get()
                                    .load(imgMainAva)
                                    .error(R.drawable.ic_user)
                                    .into(binding.ivdetailpp)*/
                        }
                    } else {
                        Log.e("RETROFIT_ERROR", response.code().toString())
                    }
                }
            } catch (e: Exception) {
                Log.e("", e.toString())
            }
        }
    }
    companion object {
        const val SWITCH_BOUND = 0.8f
        const val TO_EXPANDED = 0
        const val TO_COLLAPSED = 1
        const val WAIT_FOR_SWITCH = 0
        const val SWITCHED = 1

        /**Collapse Toolbar*/
        var EXPAND_AVATAR_SIZE: Float = 0F
        var COLLAPSE_IMAGE_SIZE: Float = 0F

        /**Delay drawer*/
        const val DELAY_IN_MS: Long = 900
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}