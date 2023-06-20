package com.example.sellspot.ui.activities.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sellspot.databinding.ItemListLayoutBinding
import com.example.sellspot.model.Order
import com.myshoppal.utils.GlideLoader

class MyOrdersListAdapter(
    private val context: Context,
    private var list: ArrayList<Order>
) : RecyclerView.Adapter<MyOrdersListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListLayoutBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]
        val binding = holder.binding

        GlideLoader(context).loadProductPicture(
            model.image,
            binding.ivItemImage
        )

        binding.tvItemName.text = model.title
        binding.tvItemPrice.text = "$${model.total_amount}"

        binding.ibDeleteProduct.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(val binding: ItemListLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
