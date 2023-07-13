package com.example.sellspot.ui.activities.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
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
    private var mProductOwnerId: String = ""
    private var mProductId: String = ""
    private lateinit var binding: ActivityProductDetailsBinding

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            mProductOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }

        setupActionBar()
        setupShareButton()

        if (FirebaseClass().getCurrentUserID() == mProductOwnerId) {
            binding.btnAddToCart.visibility = View.GONE
            binding.btnGoToCart.visibility = View.GONE
        } else {
            binding.btnAddToCart.visibility = View.VISIBLE
        }

        binding.btnAddToCart.setOnClickListener(this)
        binding.btnGoToCart.setOnClickListener(this)

        getProductDetails()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarProductDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.setTitle(R.string.title_product_details)

            // Add share button
            val shareButton = binding.toolbarProductDetailsActivity.findViewById<ImageButton>(R.id.btn_share)
            shareButton.visibility = View.VISIBLE

            shareButton.setOnClickListener {
                shareProduct()
            }
        }

        binding.toolbarProductDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupShareButton() {
        val shareButton = binding.toolbarProductDetailsActivity.findViewById<ImageButton>(R.id.btn_share)
        shareButton.visibility = View.VISIBLE
        shareButton.setOnClickListener {
            shareProduct()
        }
    }


//    private fun shareProduct() {
//        val shareIntent = Intent(Intent.ACTION_SEND)
//        shareIntent.type = "text/plain"
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this product")
//        shareIntent.putExtra(
//            Intent.EXTRA_TEXT,
//            "I found this amazing product: ${mProductDetails.title}\n\n${mProductDetails.description}"
//        )
//        startActivity(Intent.createChooser(shareIntent, "Share via"))
//    }


//    private fun shareProduct() {
//        val message = "I found this amazing product: ${mProductDetails.title}\n\n${mProductDetails.description}"
//
//        // Create a share intent with the message
//        val shareIntent = Intent(Intent.ACTION_SEND)
//        shareIntent.type = "text/plain"
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this product")
//        shareIntent.putExtra(Intent.EXTRA_TEXT, "$message\n\nOpen in SellSpot: sellspotapp://example.com?productId=$mProductId&userId=$mProductOwnerId")
//
//        // Create a chooser intent to give the user the option to share via the app or other apps
//        val chooserIntent = Intent.createChooser(shareIntent, "Share via")
//        startActivity(chooserIntent)
//    }

    private fun shareProduct() {
        val message = "I found this amazing product: ${mProductDetails.title}\n\n${mProductDetails.description}"

        // Create the deep link URL
        val deepLinkUrl = "sellspotapp://example.com?productId=$mProductId&userId=$mProductOwnerId"

        // Create a share intent with the message as HTML
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/html"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this product")
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            Html.fromHtml("$message<br><br>Open in SellSpot: \n <a href=\"$deepLinkUrl\">$deepLinkUrl</a>", Html.FROM_HTML_MODE_LEGACY)
        )

        // Create a chooser intent to give the user the option to share via the app or other apps
        val chooserIntent = Intent.createChooser(shareIntent, "Share via")

        // Check if there are any apps available to handle the share intent
        if (shareIntent.resolveActivity(packageManager) != null) {
            startActivity(chooserIntent)
        } else {
            // Fallback: Open a toast message indicating that the deep link is not supported
            Toast.makeText(this, "App not installed or deep linking not supported", Toast.LENGTH_SHORT).show()
        }
    }




    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.btn_add_to_cart -> {
                    addToCart()
                }
                R.id.btn_go_to_cart -> {
                    startActivity(Intent(this@ProductDetailsActivity, CartListActivity::class.java))
                }
            }
        }
    }

    private fun addToCart() {
        val addToCart = Cart(
            FirebaseClass().getCurrentUserID(),
            mProductOwnerId,
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )

        showProgressDialog(resources.getString(R.string.please_wait))
        FirebaseClass().addCartItems(this@ProductDetailsActivity, addToCart)
    }

    private fun getProductDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirebaseClass().getProductDetails(this@ProductDetailsActivity, mProductId)
    }

    fun productDetailsSuccess(product: Product) {
        mProductDetails = product

        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            binding.ivProductDetailImage
        )

        binding.tvProductDetailsTitle.text = product.title
        binding.tvProductDetailsPrice.text = "$${product.price}"
        binding.tvProductDetailsDescription.text = product.description
        binding.tvProductDetailsAvailableQuantity.text = product.stock_quantity

        if (FirebaseClass().getCurrentUserID() != product.user_id) {
            FirebaseClass().checkIfItemExistInCart(this@ProductDetailsActivity, mProductId)
        } else {
            hideProgressDialog()
        }
    }

    fun productExistsInCart() {
        hideProgressDialog()
        binding.btnAddToCart.visibility = View.GONE
        binding.btnGoToCart.visibility = View.VISIBLE
    }

    fun addToCartSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this@ProductDetailsActivity,
            resources.getString(R.string.success_message_item_added_to_cart),
            Toast.LENGTH_SHORT
        ).show()
        binding.btnAddToCart.visibility = View.GONE
        binding.btnGoToCart.visibility = View.VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
