package com.example.sellspot.ui.activities.ui.activities

import android.os.Bundle
import android.view.View
import com.example.sellspot.R
import com.example.sellspot.databinding.ActivityProductDetailsBinding
import com.example.sellspot.firebase.FirebaseClass
import com.example.sellspot.model.Product
import com.example.sellspot.utils.Constants
import com.myshoppal.utils.GlideLoader

/**
 * Product Details Screen.
 */

class ProductDetailsActivity : BaseActivity() {

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
        // END

        getProductDetails()
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
    }
}