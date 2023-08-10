//package com.myshoppal.ui.adapters
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.sellspot.databinding.ItemDashboardLayoutBinding
//import com.example.sellspot.model.Product
//import com.example.sellspot.utils.GlideLoader
//
//
///**
// * A adapter class for dashboard items list.
// */
//open class DashboardItemsListAdapter(
//    private val context: Context,
//    var list: ArrayList<Product>
//) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    private var _itemBinding: ItemDashboardLayoutBinding? = null
//    private val itemBinding get() = _itemBinding!!
//
//    // A global variable for OnClickListener interface.
//    private var onClickListener: OnClickListener? = null
//
//    /**
//     * Inflates the item views which is designed in xml layout file
//     *
//     * create a new
//     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
//     */
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        _itemBinding = ItemDashboardLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
//        return MyViewHolder(itemBinding.root)
//    }
//
//    /**
//     * Binds each item in the ArrayList to a view
//     *
//     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
//     * an item.
//     *
//     * This new ViewHolder should be constructed with a new View that can represent the items
//     * of the given type. You can either create a new View manually or inflate it from an XML
//     * layout file.
//     */
//    @SuppressLint("SetTextI18n")
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val model = list[position]
//        Log.d("DashboardItemsListAdapter", "Binding position: $position, Product: $model")
//        if (holder is MyViewHolder) {
//
//            GlideLoader(context).loadProductPicture(
//                model.image,
//                itemBinding.ivDashboardItemImage
//            )
//            itemBinding.tvDashboardItemTitle.text = model.title
//            itemBinding.tvDashboardItemPrice.text = "$${model.price}"
//
//            holder.itemView.setOnClickListener {
//                if (onClickListener != null) {
//                    onClickListener!!.onClick(position, model)
//                }
//            }
//        }
//    }
//
//    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
//        if (holder is MyViewHolder) {
//            // Clear any click listeners or other references here
//        }
//        super.onViewRecycled(holder)
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    /**
//     * A function for OnClickListener where the Interface is the expected parameter and assigned to the global variable.
//     *
//     * @param onClickListener
//     */
//    fun setOnClickListener(onClickListener: OnClickListener) {
//        this.onClickListener = onClickListener
//    }
//
//    /**
//     * An interface for onclick items.
//     */
//    interface OnClickListener {
//
//        fun onClick(position: Int, product: Product)
//    }
//
//    /**
//     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
//     */
//    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
//}

package com.example.sellspot.ui.activities.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sellspot.databinding.ItemDashboardLayoutBinding
import com.example.sellspot.model.Product

/**
 * A adapter class for dashboard items list.
 */
open class DashboardItemsListAdapter(
    private val context: Context,
    var list: ArrayList<Product>
) : RecyclerView.Adapter<DashboardItemsListAdapter.MyViewHolder>() {

    // A global variable for OnClickListener interface.
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemDashboardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        holder.bind(model)
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, model)
            }
        }
    }

    override fun onViewRecycled(holder: MyViewHolder) {
        holder.clear()
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A function for OnClickListener where the Interface is the expected parameter and assigned to the global variable.
     *
     * @param onClickListener
     */
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    /**
     * An interface for onclick items.
     */
    interface OnClickListener {
        fun onClick(position: Int, product: Product)
    }

    /**m
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    inner class MyViewHolder(private val binding: ItemDashboardLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            Glide.with(context)
                .load(product.image)
                .into(binding.ivDashboardItemImage)
            binding.tvDashboardItemTitle.text = product.title
            binding.tvDashboardItemPrice.text = "$${product.price}"
        }

        fun clear() {
            // Clear any resources or references related to the item view
            Glide.with(context).clear(binding.ivDashboardItemImage)
        }
    }
}