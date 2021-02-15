package com.xfath.hormart.model

import java.io.Serializable
import java.math.BigInteger

class Products : Serializable {

    var id: Int = 0
    var user_id: Int = 0
    var kategori_id: Int = 0

    lateinit var nama: String
    lateinit var harga: BigInteger
    lateinit var gambar: String

    lateinit var deskripsi: String
    lateinit var deleted_at: String
    lateinit var created_at: String
    lateinit var updated_at: String

    /** Produk mempunyai banyak User dan Photo Profile */

    lateinit var users : Users
    var photos: Photos = Photos()

}
