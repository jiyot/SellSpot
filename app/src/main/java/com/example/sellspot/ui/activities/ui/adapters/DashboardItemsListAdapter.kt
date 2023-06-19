package com.myshoppal.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sellspot.databinding.ItemDashboardLayoutBinding
import com.example.sellspot.model.Product
import com.myshoppal.utils.GlideLoader

open class DashboardItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _itemBinding: ItemDashboardLayoutBinding? = null
    private val itemBinding get() = _itemBinding!!
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        _itemBinding = ItemDashboardLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(itemBinding.root)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture(model.image, itemBinding.ivDashboardItemImage)
            itemBinding.tvDashboardItemTitle.text = model.title
            itemBinding.tvDashboardItemPrice.text = "$${model.price}"
        }

    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is MyViewHolder) {
            // Clear any click listeners or other references here
        }
        super.onViewRecycled(holder)
    }

    /**
     * Gets the number of items in the list
     */

    // TODO Step 3: Create A function for OnClickListener where the Interface is the expected parameter and assigned to the global variable.
    // START
    /**
     * A function for OnClickListener where the Interface is the expected parameter and assigned to the global variable.
     *
     * @param onClickListener
     */
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    // END

    // TODO Step 1: Create an interface for OnClickListener.
    /**
     * An interface for onclick items.
     */
    interface OnClickListener {

        // TODO Step 4: Define a function to get the required params when user clicks on the item view in the interface.
        // START
        fun onClick(position: Int, product: Product)
        // END
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
