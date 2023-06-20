package com.example.sellspot.ui.activities.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sellspot.R
import com.example.sellspot.databinding.ActivityAddEditAddressBinding


// TODO Step 1: Create an empty activity as AddEditAddressActivity.
// START
/**
 * Add edit address screen.
 */
class AddEditAddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditAddressBinding

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // This call the parent constructor
        super.onCreate(savedInstanceState)

        // Initialize the binding
        binding = ActivityAddEditAddressBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // TODO Step 7: Call the setup action bar function.
        // START
        setupActionBar()
        // END
    }

    // TODO Step 6: Create a function to setup action bar.
    // START
    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarAddEditAddressActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarAddEditAddressActivity.setNavigationOnClickListener { onBackPressed() }
    }
    // END
}
// END
