package com.example.sellspot.Firebase

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.sellspot.Model.User
import com.example.sellspot.activities.LoginActivity
import com.example.sellspot.activities.RegisterActivity
import com.example.sellspot.activities.UserProfileActivity
import com.example.sellspot.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class FirebaseClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        userInfo.id?.let {
            mFireStore.collection("users")
                .document(it)
                .set(userInfo, SetOptions.merge())
                .addOnSuccessListener {
                    activity.userRegistrationSuccess()
                }
                .addOnFailureListener { e ->
                    activity.hideProgressDialog()
                    Log.e(
                        activity.javaClass.simpleName,
                        "Error while registering the user.",
                        e
                    )
                }
        }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity: Activity) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)!!

                    val sharedPreferences =
                        activity.getSharedPreferences(
                            Constants.SELLSPOT_PREFERENCES,
                            Context.MODE_PRIVATE
                        )

                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString(
                        Constants.LOGGED_IN_USERNAME,
                        "${user.firstName} ${user.lastName}"
                    )
                    editor.apply()

                    when (activity) {
                        is LoginActivity -> {
                            activity.userLoggedInSuccess(user)
                        }
                    }
                } else {
                    when (activity) {
                        is LoginActivity -> {
                            activity.hideProgressDialog()
                        }
                    }
                    Log.e(activity.javaClass.simpleName, "User document does not exist.")
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName, "Error while getting user details.", e)
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        // Collection Name
        mFireStore.collection(Constants.USERS)
            // Document ID against which the data to be updated. Here the document id is the current logged in user id.
            .document(getCurrentUserID())
            // A HashMap of fields which are to be updated.
            .update(userHashMap)
            .addOnSuccessListener {

                // TODO Step 9: Notify the success result to the base activity.
                // START
                // Notify the success result.
                when (activity) {
                    is UserProfileActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userProfileUpdateSuccess()
                    }
                }
                // END
            }
            .addOnFailureListener { e ->

                when (activity) {
                    is UserProfileActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }

    // START
    // A function to upload the image to the cloud storage.
//    fun uploadImageToCloudStorage(activity: Activity, imageFileUri: Uri?) {
//        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
//            "USER_IMAGE" + System.currentTimeMillis() + "."
//                    + Constants.getFileExtension(activity, imageFileUri!!)
//        )
//
//        val uploadTask = sRef.putFile(imageFileUri)
//        uploadTask.continueWithTask { task ->
//            if (!task.isSuccessful) {
//                task.exception?.let {
//                    throw it
//                }
//            }
//            sRef.downloadUrl
//        }.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val downloadUri = task.result
//                Log.e("Downloadable Image URL", downloadUri.toString())
//                // Call the function of the base activity to notify the success result of image upload.
//                (activity as UserProfileActivity).imageUploadSuccess(downloadUri.toString())
//            } else {
//                Toast.makeText(
//                    activity,
//                    "Failed to upload image",
//                    Toast.LENGTH_LONG
//                ).show()
//                (activity as UserProfileActivity).hideProgressDialog()
//            }
//        }
//    }
}
