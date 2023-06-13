package com.example.sellspot.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    // Firebase Constants
    // This  is used for the collection name for USERS.
    const val USERS: String = "users"

    const val SELLSPOT_PREFERENCES: String = "SellSpotPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"

    // Intent extra constants.
    const val EXTRA_USER_DETAILS: String = "extra_user_details"

    //A unique code for asking the Read Storage Permission using this we will be check and identify in the method onRequestPermissionsResult in the Base Activity.
    const val READ_STORAGE_PERMISSION_CODE = 2

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
    // END

//    const val USER_PROFILE_IMAGE:String = "User_Profile_Image"

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

//
//    // START
//    /**
//     * A function to get the image file extension of the selected image.
//     *
//     * @param contentResolver ContentResolver instance.
//     * @param uri Image file uri.
//     */
//    fun getFileExtension(context: Context, uri: Uri): String? {
//        return if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
//            val mime = MimeTypeMap.getSingleton()
//            mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
//        } else {
//            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
//            if (fileExtension != null) {
//                fileExtension
//            } else {
//                // Fallback to manual extraction if MimeTypeMap method fails
//                val path = uri.path
//                path?.substring(path.lastIndexOf(".") + 1)
//            }
//        }
//    }
    // END
}
