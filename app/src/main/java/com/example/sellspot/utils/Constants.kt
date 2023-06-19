package com.example.sellspot.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    // Firebase Constants
    // This  is used for the collection name for USERS.
    const val USERS: String = "users"
    const val PRODUCTS: String = "products"

    const val SELLSPOT_PREFERENCES: String = "SellSpotPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"


    //A unique code for asking the Read Storage Permission using this we will be check and identify in the method onRequestPermissionsResult in the Base Activity.
    const val READ_STORAGE_PERMISSION_CODE = 2

    const val EXTRA_PRODUCT_ID: String = "extra_product_id"

    // A unique code of image selection from Phone Storage.
    const val PICK_IMAGE_REQUEST_CODE = 2

    // TODO Step 3: Create constant variables as requirement.
    // Constant variables for Gender
    // END
    const val MALE: String = "Male"
    const val FEMALE: String = "Female"

    // Firebase database field names
    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"

    const val IMAGE: String = "image"
    const val USER_PROFILE_IMAGE:String = "User_Profile_Image"

    const val EXTRA_USER_DETAILS: String = "extra_user_details"

    const val FIRST_NAME: String = "firstName"
    const val LAST_NAME: String = "lastName"

    const val COMPLETE_PROFILE: String = "profileCompleted"

    const val USER_ID: String = "user_id"

    const val PRODUCT_IMAGE: String = "Product_Image"

    /**
     * A function for user profile image selection from phone storage.
     */
    fun showImageChooser(activity: Activity) {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    /**
     * A function to get the image file extension of the selected image.
     *
     * @param activity Activity reference.
     * @param uri Image file uri.
     */
    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        /*
         * MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap.
         *
         * getExtensionFromMimeType: Return the registered extension for the given MIME type.
         *
         * contentResolver.getType: Return the MIME type of the given content URL.
         */
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}
