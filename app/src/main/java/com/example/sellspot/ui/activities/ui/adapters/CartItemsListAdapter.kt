package com.example.sellspot.ui.activities.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sellspot.databinding.ItemCartLayoutBinding
import com.example.sellspot.model.Cart
import com.myshoppal.ui.adapters.DashboardItemsListAdapter
import com.myshoppal.utils.GlideLoader


/**
 * A adapter class for dashboard items list.
 */
open class CartItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<Cart>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _itemBinding: ItemCartLayoutBinding? = null
    private val itemBinding get() = _itemBinding!!


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        _itemBinding = ItemCartLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return DashboardItemsListAdapter.MyViewHolder(itemBinding.root)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductPicture(model.image, itemBinding.ivCartItemImage)


            itemBinding.tvCartItemTitle.text = model.title
            itemBinding.tvCartItemPrice.text = "$${model.price}"
            itemBinding.tvCartQuantity.text = model.cart_quantity
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}