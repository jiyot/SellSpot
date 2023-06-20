package com.example.sellspot.ui.activities.ui.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.sellspot.databinding.ItemAddressLayoutBinding
import com.example.sellspot.model.Address
import com.example.sellspot.ui.activities.ui.activities.AddEditAddressActivity
import com.example.sellspot.ui.activities.ui.activities.CheckoutActivity
import com.example.sellspot.utils.Constants

/**
 * An adapter class for AddressList adapter.
 */
open class AddressListAdapter(
    private val context: Context,
    private var list: ArrayList<Address>,
    private val selectAddress: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _itemBinding: ItemAddressLayoutBinding? = null
    private val itemBinding get() = _itemBinding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        _itemBinding = ItemAddressLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(itemBinding.root)
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        // Access the views through binding
        if (holder is MyViewHolder) {
        itemBinding.tvAddressFullName.text = model.name
        itemBinding.tvAddressType.text = model.type
        itemBinding.tvAddressDetails.text = "${model.address}, ${model.zipCode}"
        itemBinding.tvAddressMobileNumber.text = model.mobileNumber

            if (selectAddress) {
                holder.itemView.setOnClickListener {
//                    Toast.makeText(
//                        context,
//                        "Selected address : ${model.address}, ${model.zipCode}",
//                        Toast.LENGTH_SHORT
//                    ).show()

                    val intent = Intent(context, CheckoutActivity::class.java)
                    intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS,model)
                    context.startActivity(intent)
                    // END
                }
            }
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    // TODO Step 4: Create a function to function to edit the address details and pass the existing details through intent.
    /**
     * A function to edit the address details and pass the existing details through intent.
     *
     * @param activity
     * @param position
     */
    fun notifyEditItem(activity: Activity, position: Int) {
        val intent = Intent(context, AddEditAddressActivity::class.java)
        // TODO Step 6: Pass the address details through intent to edit the address.
        // START
        intent.putExtra(Constants.EXTRA_ADDRESS_DETAILS, list[position])

        // TODO Step 15: Make it startActivityForResult instead of startActivity.
        // START
        // activity.startActivity (intent)

        activity.startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
        // END
        activity.startActivity(intent)

        notifyItemChanged(position) // Notify any registered observers that the item at position has changed.
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
