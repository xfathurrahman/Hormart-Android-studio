package com.xfath.hormart.fragments.homefragment.homechildfragments

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.xfath.hormart.R
import com.xfath.hormart.adapters.ProductAdapter
import com.xfath.hormart.adapters.SliderAdapter
import com.xfath.hormart.api.ApiConfig
import com.xfath.hormart.helper.SharedPreference
import com.xfath.hormart.model.ImageSliderHome
import com.xfath.hormart.model.Products
import com.xfath.hormart.model.ResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChildHomeFragment : Fragment(){

    private lateinit var s: SharedPreference
    private var listImageSliderHomes: ArrayList<ImageSliderHome> = ArrayList()
    private var listProducts: ArrayList<Products> = ArrayList()

    private lateinit var sliderView: SliderView
    private lateinit var rvProductBaru: RecyclerView
    private lateinit var ivNotif: ImageButton
    private lateinit var uidhome: TextView
    private lateinit var svhome: NestedScrollView
    private lateinit var mSearchView: SearchView
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getSlideImage()
        getProducts()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_child_home, container, false)

        init(view)
        swipeRefresh()

        s = SharedPreference(activity!!)


        return view
    }

    private fun swipeRefresh() {
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(
            requireContext(),
            R.color.red))
        mSwipeRefreshLayout.setColorSchemeColors(Color.WHITE)

        mSwipeRefreshLayout.setOnRefreshListener {
            listProducts.clear()
            listImageSliderHomes.clear()
            getProducts()
            getSlideImage()

            mSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun getSlideImage() {
        val img1 = ImageSliderHome(
            "https://firebasestorage.googleapis.com/v0/b/pasar-ikan.appspot.com/o/sample_images%2Fdisc_1.jpg?alt=media&token=700fc7f8-b377-4553-aebe-08b315cd7f38",
            ""
        )
        val img2 = ImageSliderHome(
            "https://firebasestorage.googleapis.com/v0/b/pasar-ikan.appspot.com/o/sample_images%2Fdisc_2.jpg?alt=media&token=8e5c1ef8-b45d-4e77-8432-d920c1c121c9",
            ""
        )
        val img3 = ImageSliderHome(
            "https://firebasestorage.googleapis.com/v0/b/pasar-ikan.appspot.com/o/sample_images%2Fdisc_3.jpg?alt=media&token=6262fbda-8262-4fdb-88e2-5985ca411adc",
            ""
        )
        val img4 = ImageSliderHome(
            "https://firebasestorage.googleapis.com/v0/b/pasar-ikan.appspot.com/o/sample_images%2Fdisc_4.jpg?alt=media&token=b81eccbd-1c4e-477c-b590-c31c1a6da2d8",
            ""
        )

        listImageSliderHomes.add(img1)
        listImageSliderHomes.add(img2)
        listImageSliderHomes.add(img3)
        listImageSliderHomes.add(img4)
        val adapter = SliderAdapter(listImageSliderHomes)
        sliderView.sliderAdapter = adapter
        //set indicator animation by using SliderLayout.IndicatorAnimations. :
        // WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM)
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        sliderView.scrollTimeInSec = 3
        sliderView.startAutoCycle()
    }

    private fun getProducts() {
        ApiConfig.instanceRetrofit.getproduct().enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                response.body()?.let { respon ->
                    if (respon.success == 1) {
                        listProducts = respon.products
                        displayProduct()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Log.v(TAG, "Error:" + t.message)
            }
        })
    }

    private fun displayProduct() {

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rvProductBaru.layoutManager = layoutManager
        rvProductBaru.adapter = ProductAdapter(listProducts)
    }

    private fun init(view: View) {
        rvProductBaru = view.findViewById(R.id.rv_product_baru)
        ivNotif = view.findViewById(R.id.iv_notifikasi)
        uidhome = view.findViewById(R.id.tv_uid_home)
        svhome = view.findViewById(R.id.sv_child_home)
        mSearchView = view.findViewById(R.id.et_search)
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_home)
        sliderView = view.findViewById(R.id.sliderView)
    }

    override fun onStop() {
        super.onStop()
        activity!!.finish()
    }

}