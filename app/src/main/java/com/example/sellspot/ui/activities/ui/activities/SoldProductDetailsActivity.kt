package com.example.sellspot.ui.activities.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sellspot.R
import com.example.sellspot.model.SoldProduct
import com.example.sellspot.utils.Constants

// TODO Step 1: Create an empty activity for SoldProductDetails and replace the AppCompatActivity with BaseActivity.
// START
/**
 * A detail screen for the sold product item.
 */
class SoldProductDetailsActivity : BaseActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_sold_product_details)

        // TODO Step 8: Receive the sold product details through intent.
        // START
        var productDetails: SoldProduct = SoldProduct()

        if (intent.hasExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)) {
            productDetails =
                intent.getParcelableExtra<SoldProduct>(Constants.EXTRA_SOLD_PRODUCT_DETAILS)!!
        }
        // END
    }
}