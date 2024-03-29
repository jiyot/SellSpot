package com.example.sellspot.ui.activities.ui.activities


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sellspot.R
import com.example.sellspot.databinding.ActivityUserProfileBinding
import com.example.sellspot.firebase.FirebaseClass
import com.example.sellspot.utils.Constants
import com.example.sellspot.utils.GlideLoader

import java.io.IOException

/**
 * A user profile screen.
 */
class UserProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUserProfileBinding


    // Instance of User data model class. We will initialize it later on.
    private lateinit var mUserDetails: com.example.sellspot.model.User

    // Add a global variable for URI of a selected image from phone storage.
    private var mSelectedImageFileUri: Uri? = null

    private var mUserProfileImageURL: String = ""

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            // Get the user details from intent as a ParcelableExtra.
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        // TODO Step 2: Once we receive the user details through intent make some changes to the code so user can complete his profile if he is from the login screen. If user is from settings screen he can edit the profile details.
        // START
        // If the profile is incomplete then user is from login screen and wants to complete the profile.
        if (mUserDetails.profileCompleted == 0) {
            // Update the title of the screen to complete profile.
            binding.tvTitle.text = resources.getString(R.string.title_complete_profile)

            // Here, the some of the edittext components are disabled because it is added at a time of Registration.
            binding.etFirstName.isEnabled = false
            binding.etFirstName.setText(mUserDetails.firstName)

            binding.etLastName.isEnabled = false
            binding.etLastName.setText(mUserDetails.lastName)

            binding.etEmail.isEnabled = false
            binding.etEmail.setText(mUserDetails.email)
        } else {

            // Call the setup action bar function.
            setupActionBar()

            // Update the title of the screen to edit profile.
            binding.tvTitle.text = resources.getString(R.string.title_edit_profile)

            // Load the image using the GlideLoader class with the use of Glide Library.
            GlideLoader(this@UserProfileActivity).loadUserPicture(mUserDetails.image, binding.ivUserPhoto)

            // Set the existing values to the UI and allow user to edit except the Email ID.
            binding.etFirstName.setText(mUserDetails.firstName)
            binding.etLastName.setText(mUserDetails.lastName)

            binding.etEmail.isEnabled = false
            binding.etEmail.setText(mUserDetails.email)

            if (mUserDetails.mobile != 0L) {
                binding.etMobileNumber.setText(mUserDetails.mobile.toString())
            }
            if (mUserDetails.gender == Constants.MALE) {
                binding.rbMale.isChecked = true
            } else {
                binding.rbFemale.isChecked = true
            }
        }
        // END

        // Assign the on click event to the user profile photo.
        binding.ivUserPhoto.setOnClickListener(this@UserProfileActivity)
        // Assign the on click event to the SAVE button.
        binding.btnSubmit.setOnClickListener(this@UserProfileActivity)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.iv_user_photo -> {

                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this@UserProfileActivity)
                    } else {
                        /*Requests permissions to be granted to this application. These permissions
                         must be requested in your manifest, they should not be granted to your app,
                         and they should have protection level*/
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_submit -> {
                    if (validateUserProfileDetails()) {

                        // Show the progress dialog.
                        showProgressDialog(resources.getString(R.string.please_wait))

                        if (mSelectedImageFileUri != null) {

                            FirebaseClass().uploadImageToCloudStorage(
                                this@UserProfileActivity,
                                mSelectedImageFileUri,Constants.USER_PROFILE_IMAGE
                            )
                        } else {

                            updateUserProfileDetails()
                        }
                    }
                }
            }
        }
    }

    /**
     * A function to validate the input entries for profile details.
     */

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@UserProfileActivity)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

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
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {

                        // The uri of selected image from phone storage.
                        mSelectedImageFileUri = data.data!!

                        GlideLoader(this@UserProfileActivity).loadUserPicture(
                            mSelectedImageFileUri!!,
                            binding.ivUserPhoto
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    // TODO Step 1: Create a function to setup action bar if the user is about to edit profile.
    // START
    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarUserProfileActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarUserProfileActivity.setNavigationOnClickListener { onBackPressed() }
    }
    // END

    /**
     * A function to validate the input entries for profile details.
     */

    private fun validateUserProfileDetails(): Boolean {
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val mobileNumber = binding.etMobileNumber.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()

        if (TextUtils.isEmpty(firstName)) {
            showErrorSnackBar(getString(R.string.err_msg_enter_first_name), true)
            return false
        }

        if (TextUtils.isEmpty(lastName)) {
            showErrorSnackBar(getString(R.string.err_msg_enter_last_name), true)
            return false
        }

        val phonePattern = "^[0-9]{10}$"
        if (!mobileNumber.matches(Regex(phonePattern))) {
            showErrorSnackBar(getString(R.string.err_msg_invalid_mobile_number), true)
            return false
        }

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!email.matches(Regex(emailPattern))) {
            showErrorSnackBar(getString(R.string.err_msg_invalid_email), true)
            return false
        }

        return true
    }

    private fun updateUserProfileDetails() {
        val userHashMap = HashMap<String, Any>()

        val firstName = binding.etFirstName.text.toString().trim()
        if (firstName != mUserDetails.firstName) {
            userHashMap[Constants.FIRST_NAME] = firstName
        }

        val lastName = binding.etLastName.text.toString().trim()
        if (lastName != mUserDetails.lastName) {
            userHashMap[Constants.LAST_NAME] = lastName
        }

        val mobileNumber = binding.etMobileNumber.text.toString().trim()
        val gender = if (binding.rbMale.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }

        if (mobileNumber.isNotEmpty()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        if (mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        if (gender.isNotEmpty() && gender != mUserDetails.gender) {
            userHashMap[Constants.GENDER] = gender
        }

        if (mUserDetails.profileCompleted == 0) {
            userHashMap[Constants.COMPLETE_PROFILE] = 1
        }

        FirebaseClass().updateUserProfileData(
            this@UserProfileActivity,
            userHashMap
        )
    }



//    private fun validateUserProfileDetails(): Boolean {
//        return when {
//
//            // We have kept the user profile picture is optional.
//            // The FirstName, LastName, and Email Id are not editable when they come from the login screen.
//            // The Radio button for Gender always has the default selected value.
//
//            // Check if the mobile number is not empty as it is mandatory to enter.
//            TextUtils.isEmpty(binding.etMobileNumber.text.toString().trim { it <= ' ' }) -> {
//                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
//                false
//            }
//            else -> {
//                true
//            }
//        }
//    }
//
//
//    /**
//     * A function to update user profile details to the firestore.
//     */
//    private fun updateUserProfileDetails() {
//
//        val userHashMap = HashMap<String, Any>()
//
//        // TODO Step 5: Update the code if user is about to Edit Profile details instead of Complete Profile.
//        // Get the FirstName from editText and trim the space
//        val firstName = binding.etFirstName.text.toString().trim { it <= ' ' }
//        if (firstName != mUserDetails.firstName) {
//            userHashMap[Constants.FIRST_NAME] = firstName
//        }
//
//        // Get the LastName from editText and trim the space
//        val lastName = binding.etLastName.text.toString().trim { it <= ' ' }
//        if (lastName != mUserDetails.lastName) {
//            userHashMap[Constants.LAST_NAME] = lastName
//        }
//
//        // TODO Step 6: Email ID is not editable so we don't need to add it here to get the text from EditText.
//
//        // Here we get the text from editText and trim the space
//        val mobileNumber = binding.etMobileNumber.text.toString().trim { it <= ' ' }
//        val gender = if (binding.rbMale.isChecked) {
//            Constants.MALE
//        } else {
//            Constants.FEMALE
//        }
//
//        if (mobileNumber.isNotEmpty()) {
//            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
//        }
//
//        if (mUserProfileImageURL.isNotEmpty()) {
//            userHashMap[Constants.IMAGE] = mUserProfileImageURL
//        }
//
//        // TODO Step 7: Update the code here if it is to edit the profile.
//        if (mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()) {
//            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
//        }
//
//        if (gender.isNotEmpty() && gender != mUserDetails.gender) {
//            userHashMap[Constants.GENDER] = gender
//        }
//
//        // Here if user is about to complete the profile then update the field or else no need.
//        // 0: User profile is incomplete.
//        // 1: User profile is completed.
//        if (mUserDetails.profileCompleted == 0) {
//            userHashMap[Constants.COMPLETE_PROFILE] = 1
//        }
//        // END
//
//        // call the registerUser function of FireStore class to make an entry in the database.
//        FirebaseClass().updateUserProfileData(
//            this@UserProfileActivity,
//            userHashMap
//        )
//    }

    /**
     * A function to notify the success result and proceed further accordingly after updating the user details.
     */
    fun userProfileUpdateSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@UserProfileActivity,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()


        // TODO Step 8: Redirect it to the DashboardActivity instead of MainActivity.
        // Redirect to the Main Screen after profile completion.
        startActivity(Intent(this@UserProfileActivity, DashboardActivity::class.java))
        finish()
    }

    /**
     * A function to notify the success result of image upload to the Cloud Storage.
     *
     * @param imageURL After successful upload the Firebase Cloud returns the URL.
     */
    fun imageUploadSuccess(imageURL: String) {

        // Hide the progress dialog
//        hideProgressDialog()
//        Toast.makeText(
//            this@UserProfileActivity,
//            "Your img uploaded successfully. Img url is $imageURL",
//            Toast.LENGTH_SHORT
//        ).show()

        mUserProfileImageURL = imageURL

        updateUserProfileDetails()

    }
}
