package com.example.sellspot.ui.activities.ui.activities


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sellspot.R
import com.example.sellspot.databinding.ActivityCheckoutBinding
import com.example.sellspot.firebase.FirebaseClass
import com.example.sellspot.model.Address
import com.example.sellspot.model.Cart
import com.example.sellspot.model.Order
import com.example.sellspot.model.Product
import com.example.sellspot.ui.activities.ui.adapters.CartItemsListAdapter
import com.example.sellspot.utils.Constants
import com.razorpay.Checkout
import org.json.JSONObject


// TODO Step 1: Create a CheckoutActivity.
// START
/**
 * A CheckOut activity screen.
 */
class CheckoutActivity : BaseActivity() {

    private var mAddressDetails: Address? = null
    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartItemsList: ArrayList<Cart>

    // A global variable for the SubTotal Amount.
    private var mSubTotal: Double = 0.0

    // A global variable for the Total Amount.
    private var mTotalAmount: Double = 0.0

    // TODO Step 6: Create a global variable for Order details.
    // START
    // A global variable for Order details.
    private lateinit var mOrderDetails: Order
    // END

    private val RAZORPAY_REQUEST_CODE = 123

    private lateinit var binding: ActivityCheckoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the binding
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize the Razorpay client
        Checkout.preload(applicationContext)

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

        // TODO Step 11: Assign a click event to the btn place order and call the function.
        // START
        binding.btnPlaceOrder.setOnClickListener {
//            placeAnOrder()
            startPayment()
        }
        // END

        getProductList()
    }

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

        for (item in mCartItemsList) {

            val availableQuantity = item.stock_quantity.toInt()

            if (availableQuantity > 0) {
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()

                mSubTotal += (price * quantity)
            }
        }

        binding.tvCheckoutSubTotal.text = "$$mSubTotal"
        binding.tvCheckoutShippingCharge.text = "$10.0"

        if (mSubTotal > 0) {
            binding.llCheckoutPlaceOrder.visibility = View.VISIBLE
            mTotalAmount = mSubTotal + 10.0
            binding.tvCheckoutTotalAmount.text = "$$mTotalAmount"
        } else {
            binding.llCheckoutPlaceOrder.visibility = View.GONE
        }
        // END
    }

    // TODO Step 2: Create a function to prepare the Order details to place an order.
    // START
    /**
     * A function to prepare the Order details to place an order.
     */
    fun placeAnOrder() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        mOrderDetails = Order(
            FirebaseClass().getCurrentUserID(),
            mCartItemsList,
            mAddressDetails!!,
            "My order ${System.currentTimeMillis()}",
            mCartItemsList[0].image,
            mSubTotal.toString(),
            "10.0", // The Shipping Charge is fixed as $10 for now in our case.
            mTotalAmount.toString(),
            System.currentTimeMillis()
        )
        // END

        // TODO Step 10: Call the function to place the order in the cloud firestore.
        // START
        FirebaseClass().placeOrder(this@CheckoutActivity, mOrderDetails)
        // END
    }
    // END


    // TODO Step 8: Create a function to notify the success result of the order placed.
    // START
    /**
     * A function to notify the success result of the order placed.
     */
    fun  orderPlacedSuccess() {

//        hideProgressDialog()
//
//        Toast.makeText(this@CheckoutActivity, "Your order placed successfully.", Toast.LENGTH_SHORT)
//            .show()
//
//        val intent = Intent(this@CheckoutActivity, DashboardActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)
//        finish()
        FirebaseClass().updateAllDetails(this@CheckoutActivity, mCartItemsList, mOrderDetails)
    }

    // TODO Step 3: Create a function to notify the success result after updating all the required details.
    // START
    /**
     * A function to notify the success result after updating all the required details.
     */
    fun allDetailsUpdatedSuccessfully() {

        // TODO Step 6: Move the piece of code from OrderPlaceSuccess to here.
        // START
        // Hide the progress dialog.
        hideProgressDialog()
///*******************************
        showErrorSnackBar("Your order placed successfully.", true)
//        Toast.makeText(this@CheckoutActivity, "Your order placed successfully.", Toast.LENGTH_SHORT)
//            .show()

        val intent = Intent(this@CheckoutActivity, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
        // END
    }

    private fun startPayment() {
        val razorpay = Checkout()

        // Set your Razorpay API key here
        razorpay.setKeyID("rzp_test_GGHfhzskNR20i7")

        try {
            // Prepare the order details in JSON format
            val options = JSONObject()
            options.put("name", "Your Company Name")
            options.put("description", "Test Payment")
            options.put("image", "https://your-company-logo-url.png")
            options.put("currency", "USD") // Use USD for US Dollar
            options.put("amount", (mTotalAmount * 100).toLong()) // Convert dollar amount to cents

            // Start the payment process
            razorpay.open(this, options)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error in payment: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // Implement the onPaymentSuccess method
    fun onPaymentSuccess(paymentId: String) {
        // Handle the payment success here
        placeAnOrder()
    }

    // Implement the onPaymentError method
    fun onPaymentError(errorCode: Int, response: String?) {
        // Handle the payment failure here
        showErrorSnackBar("Payment failed. Please Try again. \nError : $response ", true)
//        Toast.makeText(this, "*****Payment failed. Error code: $errorCode\nError Response: $response", Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RAZORPAY_REQUEST_CODE) {
            val paymentID = data?.getStringExtra("payment_id") // Replace with the actual key for payment ID
            val signature = data?.getStringExtra("signature") // Replace with the actual key for signature

            if (resultCode == RESULT_OK && signature != null) {
                // Handle successful payment
                placeAnOrder()
            } else {
                // Handle payment failure
                Toast.makeText(this, "Payment failed.", Toast.LENGTH_LONG).show()


            }
        }
    }

}