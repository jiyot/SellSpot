package com.example.sellspot.model

import android.os.Parcelable
import com.example.sellspot.model.Cart
import kotlinx.android.parcel.Parcelize

// TODO Step 1: Create a data model class for Order Items with required fields.
// START
/**
 * A data model class for Order item with required fields.
 */
@Parcelize
data class Order(
    val user_id: String = "",
    val items: ArrayList<Cart> = ArrayList(),
    val address: Address = Address(),
    val title: String = "",
    val image: String = "",
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",

    val order_datetime: Long = 0L,
    // END
    var id: String = ""
) : Parcelable
// END