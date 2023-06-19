package com.example.sellspot.ui.activities.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sellspot.R


// TODO Step 1: Create an CartListActivity.
// START
/**
 * Cart list activity of the application.
 */
class CartListActivity : AppCompatActivity() {
    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_cart_list)
    }
}
// END