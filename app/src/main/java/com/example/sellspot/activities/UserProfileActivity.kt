package com.example.sellspot.activities

import BaseActivity
import com.example.sellspot.R
import com.example.sellspot.databinding.ActivityUserProfileBinding
import com.example.sellspot.utils.GlideLoader
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
import com.example.sellspot.Firebase.FirebaseClass
import com.example.sellspot.utils.Constants
import com.google.firebase.firestore.auth.User
import java.io.IOException

/**
 * A user profile screen.
 */
class UserProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var mUserDetails: com.example.sellspot.Model.User
//    private var mSelectedImageFileUri: Uri? = null

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        binding.etFirstName.isEnabled = false
        binding.etFirstName.setText(mUserDetails.firstName)

        binding.etLastName.isEnabled = false
        binding.etLastName.setText(mUserDetails.lastName)

        binding.etEmail.isEnabled = false
        binding.etEmail.setText(mUserDetails.email)

        binding.ivUserPhoto.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_user_photo -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_submit -> {

                    showProgressDialog(resources.getString(R.string.please_wait))

//                    FirebaseClass().uploadImageToCloudStorage(
//                        this@UserProfileActivity,
//                        mSelectedImageFileUri
//                    )

                    if (validateUserProfileDetails()) {
                        val userHashMap = HashMap<String, Any>()

                        val mobileNumber = binding.etMobileNumber.text.toString().trim()

                        val gender = if (binding.rbMale.isChecked) {
                            Constants.MALE
                        } else {
                            Constants.FEMALE
                        }

                        if (mobileNumber.isNotEmpty()) {
                            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
                        }

                        userHashMap[Constants.GENDER] = gender

                        showProgressDialog(getString(R.string.please_wait))

                        FirebaseClass().updateUserProfileData(this, userHashMap)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        val selectedImageFileUri = data.data!!
                        GlideLoader(this).loadUserPicture(selectedImageFileUri!!, binding.ivUserPhoto)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this,
                            getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etMobileNumber.text.toString().trim()) -> {
                showErrorSnackBar(getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> true
        }
    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()

        Toast.makeText(
            this,
            getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

//    fun imageUploadSuccess(imageURL: String) {
//
//        // Hide the progress dialog
//        hideProgressDialog()
//
//        Toast.makeText(
//            this@UserProfileActivity,
//            "Your image is uploaded successfully. Image URL is $imageURL",
//            Toast.LENGTH_SHORT
//        ).show()
//    }
}
