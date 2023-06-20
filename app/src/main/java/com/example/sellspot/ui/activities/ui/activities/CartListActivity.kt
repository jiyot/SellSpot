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

    // TODO Step 1: Create a global variable for the cart list items.
    // START
    // A global variable for the cart list items.
    private lateinit var mCartListItems: ArrayList<Cart>
    // END

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        binding = ActivityCartListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
    }

    override fun onResume() {
        super.onResume()

        getProductList()
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

    /**
     * A function to get product list to compare the current stock with the cart items.
     */
    private fun getProductList() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirebaseClass().getAllProductsList(this@CartListActivity)
    }

    /**
     * A function to get the success result of product list.
     *
     * @param productsList
     */
    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        mProductsList = productsList

        getCartItemsList()
    }

    /**
     * A function to get the list of cart items in the activity.
     */
    private fun getCartItemsList() {

        // TODO Step 2: Comment the show progress dialog as it is already displayed in the getProductList function.
        // START
        // Show the progress dialog.
        // showProgressDialog(resources.getString(R.string.please_wait))
        // END

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

        // TODO Step 3: Compare the product id of product list with product id of cart items list and update the stock quantity in the cart items list from the latest product list.
        // START
        for (product in mProductsList) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {

                    cart.stock_quantity = product.stock_quantity

                    if (product.stock_quantity.toInt() == 0){
                     cart.cart_quantity = product.stock_quantity
                    }
                }
            }
        }
        // END

        // TODO Step 5: Initialize the global variable of cart list items.
        // START
        mCartListItems = cartList
        // END

        // TODO Step 6: Now onwards use the global variable of the cart list items as mCartListItems instead of cartList.
        if (mCartListItems.size > 0) {

            binding.rvCartItemsList.visibility = View.VISIBLE
            binding.llCheckout.visibility = View.VISIBLE
            binding.tvNoCartItemFound.visibility = View.GONE

            binding.rvCartItemsList.layoutManager = LinearLayoutManager(this@CartListActivity)
            binding.rvCartItemsList.setHasFixedSize(true)

            val cartListAdapter = CartItemsListAdapter(this@CartListActivity, mCartListItems)
            binding.rvCartItemsList.adapter = cartListAdapter

            var subTotal: Double = 0.0

            for (item in mCartListItems) {

                // TODO Step 7: Calculate the subtotal based on the stock quantity.
                // START
                val availableQuantity = item.stock_quantity.toInt()

                if (availableQuantity > 0) {
                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()

                    subTotal += (price * quantity)
                }
                // END
            }

            binding.tvSubTotal.text = "$$subTotal"
            // Here we have kept Shipping Charge is fixed as $10 but in your case it may vary. Also, it depends on the location and total amount.
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