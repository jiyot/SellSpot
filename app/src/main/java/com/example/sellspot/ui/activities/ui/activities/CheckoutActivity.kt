package com.example.sellspot.ui.activities.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sellspot.R
import com.example.sellspot.databinding.ActivityAddEditAddressBinding
import com.example.sellspot.databinding.ActivityAddProductBinding
import com.example.sellspot.databinding.ActivityCheckoutBinding
import com.example.sellspot.firebase.FirebaseClass
import com.example.sellspot.model.Address
import com.example.sellspot.model.Cart
import com.example.sellspot.model.Product
import com.example.sellspot.ui.activities.ui.adapters.CartItemsListAdapter
import com.example.sellspot.utils.Constants

// TODO Step 1: Create a CheckoutActivity.
// START
/**
 * A CheckOut activity screen.
 */
class CheckoutActivity : BaseActivity() {

    private var mAddressDetails: Address? = null
    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartItemsList: ArrayList<Cart>



    private lateinit var binding: ActivityCheckoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the binding
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // TODO Step 7: Call the function to setup the action bar.
        // START
        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)) {
            mAddressDetails =
                intent.getParcelableExtra<Address>(Constants.EXTRA_SELECTED_ADDRESS)!!
        }
        // END

        // TODO Step 5: Set the selected address details to UI that is received through intent.
        // START
        if (mAddressDetails != null) {
            binding.tvCheckoutAddressType.text = mAddressDetails?.type
            binding.tvCheckoutFullName.text = mAddressDetails?.name
            binding.tvCheckoutAddress.text = "${mAddressDetails!!.address}, ${mAddressDetails!!.zipCode}"
            binding.tvCheckoutAdditionalNote.text = mAddressDetails?.additionalNote

            if (mAddressDetails?.otherDetails!!.isNotEmpty()) {
                binding.tvCheckoutOtherDetails.text = mAddressDetails?.otherDetails
            }
            binding.tvCheckoutMobileNumber.text = mAddressDetails?.mobileNumber
        }

        getProductList()

    }

    // TODO Step 6: Create a function to setup the action bar.
    // START
    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarCheckoutActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarCheckoutActivity.setNavigationOnClickListener { onBackPressed() }
    }


    private fun getProductList() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirebaseClass().getAllProductsList(this@CheckoutActivity)
    }
    // END

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        // TODO Step 8: Initialize the global variable of all product list.
        // START
        mProductsList = productsList
        // END

        // TODO Step 10: Call the function to get the latest cart items.
        // START
        getCartItemsList()
        // END
    }
    // END

    // TODO Step 9: Create a function to get the list of cart items in the activity.
    /**
     * A function to get the list of cart items in the activity.
     */
    private fun getCartItemsList() {

        FirebaseClass().getCartList(this@CheckoutActivity)
    }

    // TODO Step 11: Create a function to notify the success result of the cart items list from cloud firestore.
    // START
    /**
     * A function to notify the success result of the cart items list from cloud firestore.
     *
     * @param cartList
     */
    fun successCartItemsList(cartList: ArrayList<Cart>) {

        // Hide progress dialog.
        hideProgressDialog()

        // TODO Step 13: Initialize the cart list.
        // START
        for (product in mProductsList) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {
                    cart.stock_quantity = product.stock_quantity
                }
            }
        }
        // END

        mCartItemsList = cartList

        // TODO Step 2: Populate the cart items in the UI.
        // START
        binding.rvCartListItems.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        binding.rvCartListItems.setHasFixedSize(true)

        // TODO Step 5: Pass the required param.
        val cartListAdapter = CartItemsListAdapter(this@CheckoutActivity, mCartItemsList, false)
        binding.rvCartListItems.adapter = cartListAdapter
        // END

        // TODO Step 9: Calculate the subtotal and Total Amount.
        // START
        var subTotal: Double = 0.0

        for (item in mCartItemsList) {

            val availableQuantity = item.stock_quantity.toInt()

            if (availableQuantity > 0) {
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()

                subTotal += (price * quantity)
            }
        }

        binding.tvCheckoutSubTotal.text = "$$subTotal"
        binding.tvCheckoutShippingCharge.text = "$10.0"

        if (subTotal > 0) {
            binding.llCheckoutPlaceOrder.visibility = View.VISIBLE
            val total = subTotal + 10
            binding.tvCheckoutTotalAmount.text = "$$total"
        } else {
            binding.llCheckoutPlaceOrder.visibility = View.GONE
        }
        // END
    }
}