package com.example.sellspot.ui.activities.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sellspot.R
import com.example.sellspot.databinding.ItemListLayoutBinding
import com.example.sellspot.model.Order
import com.example.sellspot.ui.activities.ui.activities.MyOrderDetailsActivity
import com.example.sellspot.utils.Constants
import com.myshoppal.utils.GlideLoader

open class MyOrdersListAdapter(
    private val context: Context,
    private var list: ArrayList<Order>
) : RecyclerView.Adapter<MyOrdersListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        holder.binding.apply {
            GlideLoader(context).loadProductPicture(
                model.image,
                ivItemImage
            )

            tvItemName.text = model.title
            tvItemPrice.text = "$${model.total_amount}"

            ibDeleteProduct.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, MyOrderDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_MY_ORDER_DETAILS, model)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(val binding: ItemListLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
