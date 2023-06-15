package com.example.sellspot.ui.activities.ui.activities

import android.os.Bundle
import com.example.sellspot.R

/**
 * Product Details Screen.
 */

class ProductDetailsActivity : BaseActivity() {

    // A global variable for product id.
    private var mProductId: String = ""

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_product_details)
    }
}