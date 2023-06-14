package com.example.sellspot.ui.activities.ui.activities

//import com.example.sellspot.databinding.ActivitySettingsBinding
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.sellspot.R
import com.example.sellspot.databinding.ActivitySettingsBinding
import com.example.sellspot.firebase.FirebaseClass
import com.example.sellspot.model.User
import com.example.sellspot.utils.Constants
import com.example.sellspot.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth

/**
 * Setting screen of the app.
 */
class SettingsActivity : BaseActivity(), View.OnClickListener {

    // A variable for user details which will be initialized later on.
    private lateinit var mUserDetails: User
    private lateinit var binding: ActivitySettingsBinding
    /**
     * This function is auto created by Android when the Activity Class is created.
     */

    // commit it
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //This call the parent constructor
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO Step 3: Call the function to setup action bar.
        // START
        setupActionBar()


        binding.tvEdit.setOnClickListener(this@SettingsActivity)
        binding.btnLogout.setOnClickListener(this@SettingsActivity)
    }

    // TODO Step 11: Override the onResume function and call the getUserDetails function init.
    // START
    override fun onResume() {
        super.onResume()

        getUserDetails()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.tv_edit -> {
                    val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                }

                R.id.btn_logout -> {

                    FirebaseAuth.getInstance().signOut()

                    val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarSettingsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarSettingsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function to get the user details from firestore.
     */
    private fun getUserDetails() {

        // Show the progress dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class to get the user details from firestore which is already created.
        FirebaseClass().getUserDetails(this@SettingsActivity)
    }
    // END

    /**
     * A function to receive the user details and populate it in the UI.
     */
    fun userDetailsSuccess(user: User) {

        mUserDetails = user

        // Hide the progress dialog
        hideProgressDialog()

        // Load the image using the Glide Loader class.
        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, binding.ivUserPhoto)

        binding.tvName.text = "${user.firstName} ${user.lastName}"
        binding.tvGender.text = user.gender
        binding.tvEmail.text = user.email
        binding.tvMobileNumber.text = "${user.mobile}"
        // END
    }
    // END

}