package com.example.sellspot.ui.activities.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sellspot.R
import com.example.sellspot.databinding.ActivityAddressListBinding


/**
 * Address list screen.
 */
class AddressListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddressListBinding

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // This call the parent constructor
        super.onCreate(savedInstanceState)

        // Initialize the binding
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupActionBar()

        // TODO Step 8: Assign the click event for the Add Address and launch the AddEditAddressActivity.
        // START
        binding.tvAddAddress.setOnClickListener {
            val intent = Intent(this@AddressListActivity, AddEditAddressActivity::class.java)
            startActivity(intent)
        }
        // END
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarAddressListActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarAddressListActivity.setNavigationOnClickListener { onBackPressed() }
    }
}
