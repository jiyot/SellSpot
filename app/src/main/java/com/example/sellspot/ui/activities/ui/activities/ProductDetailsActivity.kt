package com.example.sellspot.ui.activities.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.sellspot.R
import com.example.sellspot.databinding.ActivityProductDetailsBinding
import com.example.sellspot.firebase.FirebaseClass
import com.example.sellspot.model.Cart
import com.example.sellspot.model.Product
import com.example.sellspot.utils.Constants
import com.myshoppal.utils.GlideLoader

/**
 * Product Details Screen.
 */

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mProductDetails: Product

    // A global variable for product id.
    private var mProductId: String = ""

    private lateinit var binding: ActivityProductDetailsBinding

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId =
                intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }

        var productOwnerId: String = ""

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            productOwnerId =
                intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }
        // END

        setupActionBar()

        if (FirebaseClass().getCurrentUserID() == productOwnerId) {
            binding.btnAddToCart.visibility = View.GONE
        } else {
            binding.btnAddToCart.visibility  = View.VISIBLE
        }

        binding.btnAddToCart.setOnClickListener(this)
        // TODO Step 4: Assign a click even to the GoToCart button.
        // START
        binding.btnAddToCart.setOnClickListener(this)
        // END

        getProductDetails()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.btn_add_to_cart -> {
                    addToCart()
                }
            }
        }
    }

    /**
     * A function to prepare the cart item to add it to the cart in cloud firestore.
     */
    private fun addToCart() {

        val addToCart = Cart(
            FirebaseClass().getCurrentUserID(),
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )

        // Show the progress dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        FirebaseClass().addCartItems(this@ProductDetailsActivity, addToCart)
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarProductDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarProductDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function to call the firestore class function that will get the product details from cloud firestore based on the product id.
     */
    private fun getProductDetails() {

        // Show the product dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of FirestoreClass to get the product details.
        FirebaseClass().getProductDetails(this@ProductDetailsActivity, mProductId)
    }

    /**
     * A function to notify the success result of the product details based on the product id.
     *
     * @param product A model class with product details.
     */
    fun productDetailsSuccess(product: Product) {

        mProductDetails = product

        // TODO Step 10: Don't hide the progress dialog here. Please remove it from here. We have already taken care of it later.
        // START
        // Hide Progress dialog.
        hideProgressDialog()

        // Populate the product details in the UI.
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            binding.ivProductDetailImage
        )

        binding.tvProductDetailsTitle.text = product.title
        binding.tvProductDetailsPrice.text = "$${product.price}"
        binding.tvProductDetailsDescription.text = product.description
        binding.tvProductDetailsAvailableQuantity.text = product.stock_quantity

        // TODO Step 9: Call the function to check the product exist in the cart or not from the firestore class.
        // START
        // There is no need to check the cart list if the product owner himself is seeing the product details.
        if (FirebaseClass().getCurrentUserID() == product.user_id) {
            // Hide Progress dialog.
            hideProgressDialog()
        } else {
            FirebaseClass().checkIfItemExistInCart(this@ProductDetailsActivity, mProductId)
        }
        // END
    }

    // TODO Step 7: Create a function to notify the success result of item exists in the cart.
    // START
    /**
     * A function to notify the success result of item exists in the cart.
     */
    fun productExistsInCart() {

        // Hide the progress dialog.
        hideProgressDialog()

        // Hide the AddToCart button if the item is already in the cart.
        binding.btnAddToCart.visibility = View.GONE
        // Show the GoToCart button if the item is already in the cart. User can update the quantity from the cart list screen if he wants.
        binding.btnAddToCart.visibility = View.VISIBLE
    }
    // END

    /**
     * A function to notify the success result of item added to the to cart.
     */
    fun addToCartSuccess() {
        // Hide the progress dialog.
        hideProgressDialog()

        Toast.makeText(
            this@ProductDetailsActivity,
            resources.getString(R.string.success_message_item_added_to_cart),
            Toast.LENGTH_SHORT
        ).show()

        // TODO Step 11: Change the buttons visibility once the item is added to the cart.
        // Hide the AddToCart button if the item is already in the cart.
        binding.btnAddToCart.visibility = View.GONE
        // Show the GoToCart button if the item is already in the cart. User can update the quantity from the cart list screen if he wants.
        binding.btnAddToCart.visibility = View.VISIBLE
    }
}