package com.myshoppal.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sellspot.databinding.ItemListLayoutBinding
import com.example.sellspot.model.Product
import com.example.sellspot.ui.activities.ui.activities.ProductDetailsActivity
import com.example.sellspot.ui.activities.ui.fragments.ProductsFragment
import com.example.sellspot.utils.Constants
import com.myshoppal.utils.GlideLoader

open class MyProductsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>,
    private val fragment: ProductsFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _itemBinding: ItemListLayoutBinding? = null
    private val itemBinding get() = _itemBinding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        _itemBinding = ItemListLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(itemBinding.root)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture(model.image, itemBinding.ivItemImage)

            itemBinding.tvItemName.text = model.title
            itemBinding.tvItemPrice.text = "$${model.price}"

                itemBinding.ibDeleteProduct.setOnClickListener{
                    fragment.deleteProduct(model.product_id)
                }

            holder.itemView.setOnClickListener {
                // Launch Product details screen.
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
                // TODO Step 4: Pass the product owner id through intent. The product owner id is basically the user id of the user who have add the product.
                // START
                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, model.user_id)
                // END
                context.startActivity(intent)
            }
        }
    }

//    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
////        if (holder is MyViewHolder) {
////            holder.itemView.ibDeleteProduct.setOnClickListener(null)
////        }
//        super.onViewRecycled(holder)
//    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
