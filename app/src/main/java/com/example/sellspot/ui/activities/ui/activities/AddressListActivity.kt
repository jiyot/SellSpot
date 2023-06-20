package com.example.sellspot.ui.activities.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sellspot.R
import com.example.sellspot.databinding.ActivityAddressListBinding
import com.example.sellspot.firebase.FirebaseClass
import com.example.sellspot.model.Address
import com.example.sellspot.ui.activities.ui.adapters.AddressListAdapter
import com.example.sellspot.utils.SwipeToEditCallback


/**
 * Address list screen.
 */
class AddressListActivity : BaseActivity() {

    private lateinit var binding: ActivityAddressListBinding

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // This call the parent constructor
        // Initialize the binding
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)



        setContentView(view)

        setupActionBar()

        // TODO Step 8: Assign the click event for the Add Address and launch the AddEditAddressActivity.
        // START
        binding.tvAddAddress.setOnClickListener {
            val intent = Intent(this@AddressListActivity, AddEditAddressActivity::class.java)
            startActivity(intent)
        }

        getAddressList()
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

    /**
     * A function to get the list of address from cloud firestore.
     */
    private fun getAddressList() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirebaseClass().getAddressesList(this@AddressListActivity)
    }

    /**
     * A function to get the success result of address list from cloud firestore.
     *
     * @param addressList
     */
    fun successAddressListFromFirestore(addressList: ArrayList<Address>) {

        // Hide the progress dialog
        hideProgressDialog()

        // TODO Step 4: Remove the for loop which is used to print the result in log.
        // START
        // Print all the list of addresses in the log with name.
        for (i in addressList) {
            Log.i("Name and Address", "${i.name} ::  ${i.address}")
        }
        // END

        // TODO Step 5: Populate the address list in the UI.
        // START
        if (addressList.size > 0) {
            binding.rvAddressList.visibility = View.VISIBLE
            binding.tvNoAddressFound.visibility = View.GONE

            binding.rvAddressList.layoutManager = LinearLayoutManager(this@AddressListActivity)
            binding.rvAddressList.setHasFixedSize(true)

            val addressAdapter = AddressListAdapter(this@AddressListActivity, addressList)
            binding.rvAddressList.adapter = addressAdapter

            // TODO Step 3: Add the swipe to edit feature.
            // START
            val editSwipeHandler = object : SwipeToEditCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    // TODO Step 7: Call the notifyEditItem function of the adapter class.
                    // START
                    val adapter = binding.rvAddressList.adapter as AddressListAdapter
                    adapter.notifyEditItem(
                        this@AddressListActivity,
                        viewHolder.adapterPosition
                    )
                    // END
                }
            }
            val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
            editItemTouchHelper.attachToRecyclerView(binding.rvAddressList)
            // END
        } else {
            binding.rvAddressList .visibility = View.GONE
            binding.tvNoAddressFound.visibility = View.VISIBLE
        }
    }
}
