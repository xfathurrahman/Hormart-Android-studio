package com.xfath.hormart.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.xfath.hormart.R
import com.xfath.hormart.model.Products
import com.xfath.hormart.utils.Config
import de.hdodenhof.circleimageview.CircleImageView
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ProductAdapter(var data: ArrayList<Products>) :
    RecyclerView.Adapter<ProductAdapter.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaProduct: TextView = view.findViewById(R.id.tv_nama_product)
        val tvHargaProduct: TextView = view.findViewById(R.id.tv_harga_product)
        val tvPenjualProduct: TextView = view.findViewById(R.id.tv_nama_penjual)
        val imgProduct: ImageView = view.findViewById(R.id.iv_product)
        val imgLogoSmall: CircleImageView = view.findViewById(R.id.img_logo)
        val tvLocation: TextView = view.findViewById(R.id.tv_location)
        val tvTimeProduk: TextView = view.findViewById(R.id.tv_time_product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvPenjualProduct.isSingleLine = true
        holder.tvPenjualProduct.isSelected = true
        holder.tvNamaProduct.isSelected = true
        holder.tvHargaProduct.isSingleLine = true
        holder.tvLocation.isSingleLine = true
        holder.tvLocation.isSelected = true
        holder.tvTimeProduk.isSingleLine = true
        holder.tvTimeProduk.isSelected = true

        val timeProduct = data[position].created_at
        val timeAgo : String = formatTimeAgo(timeProduct)
        holder.tvTimeProduk.text = timeAgo

        holder.tvPenjualProduct.text = data[position].users.userName
        holder.tvNamaProduct.text = data[position].nama
        holder.tvHargaProduct.text = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            .format(data[position].harga)

        @Suppress("SENSELESS_COMPARISON")
        if (data[position].photos == null) {
            val downloadUrl = data[position].users.userPpDefault
            Picasso.get()
                .load(downloadUrl)
                .into(holder.imgLogoSmall)
        } else {
            val imgsmall = Config.smallphotoprofil + data[position].photos.userPhotoProfile
            Picasso.get()
                .load(imgsmall)
                .into(holder.imgLogoSmall)
        }

        val image = Config.gambarproduct + data[position].gambar
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.notavailable)
            .into(holder.imgProduct)
    }

    @SuppressLint("SimpleDateFormat")
    private fun formatTimeAgo(date1: String): String {
        var conversionTime =""

        try{
            val format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

            val sdf = SimpleDateFormat(format)
            sdf.timeZone = TimeZone.getTimeZone("GMT")

            val datetime= Calendar.getInstance()
            val date2= sdf.format(datetime.time).toString()

            val Past = sdf.parse(date1)
            val Now = sdf.parse(date2)
            val diff = Now.time - Past.time

            val oneSec = 1000L
            val oneMin: Long = 60 * oneSec
            val oneHour: Long = 60 * oneMin
            val oneDay: Long = 24 * oneHour
            val oneMonth: Long = 30 * oneDay
            val oneYear: Long = 365 * oneDay

            val diffMin: Long = diff / oneMin
            val diffHours: Long = diff / oneHour
            val diffDays: Long = diff / oneDay
            val diffMonths: Long = diff / oneMonth
            val diffYears: Long = diff / oneYear

            when {
                diffYears > 0 -> {
                    conversionTime += " $diffYears Tahun yang lalu"
                }
                diffMonths > 0 && diffYears < 1 -> {
                    conversionTime += " ${(diffMonths - diffYears / 12)} Bulan yang lalu"
                }
                diffDays > 0 && diffMonths < 1 -> {
                    conversionTime += " ${(diffDays - diffMonths / 30)} Hari yang lalu "
                }
                diffHours > 0 && diffDays < 1 -> {
                    conversionTime += " ${(diffHours - diffDays * 24)} Jam yang lalu "
                }
                diffMin > 0 && diffHours < 1 -> {
                    conversionTime += " ${(diffMin - diffHours * 60)} Menit yang lalu "
                }
                diffMin < 1 -> {
                    conversionTime += " Baru saja "
                }
            }

        }catch (ex:java.lang.Exception){
            Log.e("formatTimeAgo",ex.toString())
        }
        return conversionTime
    }

    override fun getItemCount(): Int {
        return data.size
    }

}