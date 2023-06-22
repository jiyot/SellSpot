package com.example.sellspot.ui.activities.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sellspot.R
import com.example.sellspot.databinding.ActivityAddressListBinding
import com.example.sellspot.firebase.FirebaseClass
import com.example.sellspot.model.Address
import com.example.sellspot.ui.activities.ui.adapters.AddressListAdapter
import com.example.sellspot.utils.Constants
import com.example.sellspot.utils.SwipeToDeleteCallback
import com.example.sellspot.utils.SwipeToEditCallback



/**
 * Address list screen.
 */
class AddressListActivity : BaseActivity() {

    private lateinit var binding: ActivityAddressListBinding
    private var mSelectAddress: Boolean = false
    // END


    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityAddressListBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)



        setContentView(view)
        

        // TODO Step 4: Receive the value and initialize the variable to select the address.
        // START
        if (intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS)) {
            mSelectAddress =
                intent.getBooleanExtra(Constants.EXTRA_SELECT_ADDRESS, false)
        }
        // END

        setupActionBar()

        // TODO Step 5: If it is about to select the address then update the title.
        // START
        if (mSelectAddress) {
            binding.tvTitle.text = resources.getString(R.string.title_select_address)
        }
        // END

        binding.tvAddAddress.setOnClickListener {
            val intent = Intent(this@AddressListActivity, AddEditAddressActivity::class.java)

            // TODO Step 12: Now to notify the address list about the latest address added we need to make neccessary changes as below.
            // START
            // startActivity(intent)
            startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
            // END
        }

        getAddressList()
    }

    // TODO Step 14: Override the onActivityResult function and get the latest address list based on the result code.
    // START
    /**
     * Receive the result from a previous call to
     * {@link #startActivityForResult(Intent, int)}.  This follows the
     * related Activity API as described there in
     * {@link Activity#onActivityResult(int, int, Intent)}.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.ADD_ADDRESS_REQUEST_CODE) {

                getAddressList()
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "To add the address.")
        }
    }
    // END

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

        if (addressList.size > 0) {

            binding.rvAddressList.visibility = View.VISIBLE
            binding.tvNoAddressFound.visibility = View.GONE

            binding.rvAddressList.layoutManager = LinearLayoutManager(this@AddressListActivity)
            binding.rvAddressList.setHasFixedSize(true)

            // TODO Step 9: Pass the address selection value.
            // START
            val addressAdapter = AddressListAdapter(this@AddressListActivity, addressList, mSelectAddress)
            // END
            binding.rvAddressList.adapter = addressAdapter

            // TODO Step 7: Don't allow user to edit or delete the address when user is about to select the address.
            // START
            if (!mSelectAddress) {
                val editSwipeHandler = object : SwipeToEditCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                        val adapter = binding.rvAddressList.adapter as AddressListAdapter
                        adapter.notifyEditItem(
                            this@AddressListActivity,
                            viewHolder.adapterPosition
                        )
                    }
                }
                val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
                editItemTouchHelper.attachToRecyclerView(binding.rvAddressList)


                val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                        // Show the progress dialog.
                        showProgressDialog(resources.getString(R.string.please_wait))

                        FirebaseClass().deleteAddress(
                            this@AddressListActivity,
                            addressList[viewHolder.adapterPosition].id
                        )
                    }
                }
                val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
                deleteItemTouchHelper.attachToRecyclerView(binding.rvAddressList)
            }
        } else {
            binding.rvAddressList .visibility = View.GONE
            binding.tvNoAddressFound.visibility = View.VISIBLE
        }
    }

    /**
     * A function notify the user that the address is deleted successfully.
     */
    fun deleteAddressSuccess() {

        // Hide progress dialog.
        hideProgressDialog()

        Toast.makeText(
            this@AddressListActivity,
            resources.getString(R.string.err_your_address_deleted_successfully),
            Toast.LENGTH_SHORT
        ).show()

        getAddressList()
    }
}