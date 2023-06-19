package com.example.sellspot.ui.activities.ui.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sellspot.R
import com.example.sellspot.databinding.ActivityCartListBinding
import com.example.sellspot.firebase.FirebaseClass
import com.example.sellspot.model.Cart
import com.example.sellspot.model.Product
import com.example.sellspot.ui.activities.ui.adapters.CartItemsListAdapter


// TODO Step 1: Create an CartListActivity.
// START
/**
 * Cart list activity of the application.
 */
class CartListActivity : BaseActivity() {

    private lateinit var binding: ActivityCartListBinding

    private lateinit var mProductsList: ArrayList<Product>
    // END

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_cart_list)

        setupActionBar()
    }

    override fun onResume() {
        super.onResume()

        // TODO Step 5: Replace the function with getCartItemsList with getProductList as before cart list we require the product list.
        // START
        getCartItemsList()

        ///getProductList()
        // END
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarCartListActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarCartListActivity.setNavigationOnClickListener { onBackPressed() }
    }

    // TODO Step 4: Create a function to get product list to compare the current stock with the cart items.
    // START
    /**
     * A function to get product list to compare the current stock with the cart items.
     */
    private fun getProductList() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirebaseClass().getAllProductsList(this@CartListActivity)
    }
    // END

    // TODO Step 2: Create a function to get the success result of product list.
    // START
    /**
     * A function to get the success result of product list.
     *
     * @param productsList
     */
    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        // TODO Step 7: Initialize the product list global variable once we have the product list.
        // START
        mProductsList = productsList
        // END

        // TODO Step 8: Once we have the latest product list from cloud firestore get the cart items list from cloud firestore.
        // START
        getCartItemsList()
        // END
    }
    // END

    /**
     * A function to get the list of cart items in the activity.
     */
    private fun getCartItemsList() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirebaseClass().getCartList(this@CartListActivity)
    }

    /**
     * A function to notify the success result of the cart items list from cloud firestore.
     *
     * @param cartList
     */
    fun successCartItemsList(cartList: ArrayList<Cart>) {

        // Hide progress dialog.
        hideProgressDialog()

        if (cartList.size > 0) {

            binding.rvCartItemsList.visibility = View.VISIBLE
            binding.llCheckout.visibility = View.VISIBLE
            binding.tvNoCartItemFound.visibility = View.GONE

            binding.rvCartItemsList.layoutManager = LinearLayoutManager(this@CartListActivity)
            binding.rvCartItemsList.setHasFixedSize(true)

            val cartListAdapter = CartItemsListAdapter(this@CartListActivity, cartList)
            binding.rvCartItemsList.adapter = cartListAdapter

            var subTotal: Double = 0.0

            for (item in cartList) {

                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()

                    subTotal += (price * quantity)
            }

            binding.tvSubTotal.text = "$$subTotal"
            // Here we have kept Shipping Charge is fixed as $10 but in your case it may cary. Also, it depends on the location and total amount.
            binding.tvShippingCharge.text = "$10.0"

            if (subTotal > 0) {
                binding.llCheckout.visibility = View.VISIBLE

                val total = subTotal + 10
                binding.tvTotalAmount.text = "$$total"
            } else {
                binding.llCheckout.visibility = View.GONE
            }

        } else {
            binding.rvCartItemsList.visibility = View.GONE
            binding.llCheckout.visibility = View.GONE
            binding.tvNoCartItemFound.visibility = View.VISIBLE
        }
    }
    // END
}