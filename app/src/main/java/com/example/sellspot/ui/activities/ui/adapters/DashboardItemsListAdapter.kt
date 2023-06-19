package com.myshoppal.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sellspot.databinding.ItemDashboardLayoutBinding
import com.example.sellspot.model.Product
import com.myshoppal.utils.GlideLoader

class DashboardItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>
) : RecyclerView.Adapter<DashboardItemsListAdapter.MyViewHolder>() {

    private lateinit var binding: ItemDashboardLayoutBinding
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ItemDashboardLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        with(binding) {
            GlideLoader(context).loadProductPicture(model.image, ivDashboardItemImage)
            tvDashboardItemTitle.text = model.title
            tvDashboardItemPrice.text = "$${model.price}"

            root.setOnClickListener {
                onClickListener?.onClick(position, model)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListener(listener: OnClickListener) {
        onClickListener = listener
    }

    interface OnClickListener {
        fun onClick(position: Int, product: Product)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
