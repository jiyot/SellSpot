package com.example.sellspot.ui.activities.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sellspot.R
import com.example.sellspot.databinding.ItemCartLayoutBinding
import com.example.sellspot.firebase.FirebaseClass
import com.example.sellspot.model.Cart
import com.example.sellspot.ui.activities.ui.activities.CartListActivity
import com.example.sellspot.utils.Constants
import com.example.sellspot.utils.GlideLoader


/**
 * A adapter class for dashboard items list.
 */
open class CartItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<Cart>,
    private val updateCartItems: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _itemBinding: ItemCartLayoutBinding? = null
    private val itemBinding get() = _itemBinding!!



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        _itemBinding = ItemCartLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(itemBinding.root)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductPicture(model.image, itemBinding.ivCartItemImage)

            itemBinding.tvCartItemTitle.text = model.title
            itemBinding.tvCartItemPrice.text = "$${model.price}"
            itemBinding.tvCartQuantity.text = model.cart_quantity

            if (model.cart_quantity == "0") {
                itemBinding.ibRemoveCartItem.visibility = View.GONE
                itemBinding.ibAddCartItem.visibility = View.GONE

                if (updateCartItems) {
                    itemBinding.ibDeleteCartItem.visibility = View.VISIBLE
                } else {
                    itemBinding.ibDeleteCartItem.visibility  = View.GONE
                }

                itemBinding.tvCartQuantity.text = context.resources.getString(R.string.lbl_out_of_stock)
                itemBinding.tvCartQuantity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorSnackBarError
                    )
                )
            } else {

                // TODO Step 7: Update the UI components as per the param.
                // START
                if (updateCartItems) {
                    itemBinding.ibRemoveCartItem.visibility = View.VISIBLE
                    itemBinding.ibAddCartItem.visibility = View.VISIBLE
                    itemBinding.ibDeleteCartItem.visibility = View.VISIBLE
                } else {
                    itemBinding.ibRemoveCartItem.visibility = View.GONE
                    itemBinding.ibAddCartItem.visibility = View.GONE
                    itemBinding.ibDeleteCartItem.visibility = View.GONE
                }

                itemBinding.tvCartQuantity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorSecondaryText
                    )
                )

            }

            // TODO Step 1: Assign the click event to the ib_remove_cart_item.
            // START
            itemBinding.ibRemoveCartItem.setOnClickListener {
                // TODO Step 6: Call the update or remove function of the Firestore class based on the cart quantity.
                // START
                if (model.cart_quantity == "1") {
                    FirebaseClass().removeItemFromCart(context, model.id)
                } else {
                    val cartQuantity: Int = model.cart_quantity.toInt()

                    val itemHashMap = HashMap<String, Any>()
                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()

                    // Show the progress dialog.
                    if (context is CartListActivity) {
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }

                    FirebaseClass().updateMyCart(context, model.id, itemHashMap)
                }
                // END
            }
            // END

            // TODO Step 7: Assign the click event to the ib_add_cart_item.
            // START
            itemBinding.ibAddCartItem.setOnClickListener {
                // TODO Step 8: Call the update function of the Firestore class based on the cart quantity.
                // START
                val cartQuantity: Int = model.cart_quantity.toInt()

                if (cartQuantity < model.stock_quantity.toInt()) {
                    val itemHashMap = HashMap<String, Any>()
                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity + 1).toString()

                    // Show the progress dialog.
                    if (context is CartListActivity) {
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }

                    FirebaseClass().updateMyCart(context, model.id, itemHashMap)
                } else {
                    if (context is CartListActivity) {
                        context.showErrorSnackBar(
                            context.resources.getString(
                                R.string.msg_for_available_stock,
                                model.stock_quantity
                            ),
                            true
                        )
                    }
                }
                // END
            }
            // END

            itemBinding.ibDeleteCartItem.setOnClickListener {
                when (context) {
                    is CartListActivity -> {
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }
                }
                FirebaseClass().removeItemFromCart(context, model.id)
            }
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