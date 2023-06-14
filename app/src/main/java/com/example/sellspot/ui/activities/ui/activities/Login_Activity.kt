package com.example.sellspot.ui.activities.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import com.example.sellspot.model.User
import com.example.sellspot.R
import com.example.sellspot.databinding.ActivityLoginBinding
import com.example.sellspot.firebase.FirebaseClass
import com.example.sellspot.ui.activities.*
import com.google.firebase.auth.FirebaseAuth
import com.example.sellspot.utils.Constants


class LoginActivity : BaseActivity()  {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {

            loginActivity()
        }
        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }


    }

    /**
     * A function to validate the login entries of a user.
     */
    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }

    /**
     * A function to Log-In. The user will be able to log in using the registered email and password with Firebase Authentication.
     */
    private fun loginActivity() {

        if (validateLoginDetails()) {

            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Get the text from editText and trim the space
            val email = binding.etEmail.text.toString().trim { it <= ' ' }
            val password = binding.etPassword.text.toString().trim { it <= ' ' }

            // Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        FirebaseClass().getUserDetails(this@LoginActivity)
                    } else {
                        // Hide the progress dialog
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

//    private fun loginActivity() {
//        when {
//            TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' ' }) -> {
//                Toast.makeText(
//                    this@LoginActivity,
//                    "Please enter email.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
//                Toast.makeText(
//                    this@LoginActivity,
//                    "Please enter password.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            else -> {
//
//                val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
//                val password: String = binding.etPassword.text.toString().trim { it <= ' ' }
//
//                // Log-In using FirebaseAuth
//                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
//                    .addOnCompleteListener { task ->
//
//                        if (task.isSuccessful) {
//
//                            Toast.makeText(
//                                this@LoginActivity,
//                                "You are logged in successfully.",
//                                Toast.LENGTH_SHORT
//                            ).show()
//
//                            /**
//                             * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
//                             * and send him to Main Screen with user id and email that user have used for registration.
//                             */
//
//                            val intent =
//                                Intent(this@LoginActivity, MainActivity::class.java)
//                            intent.flags =
//                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                            intent.putExtra(
//                                "user_id",
//                                FirebaseAuth.getInstance().currentUser!!.uid
//                            )
//                            intent.putExtra("email_id", email)
//                            startActivity(intent)
//                            finish()
//                        } else {
//
//                            // If the login is not successful then show error message.
//                            Toast.makeText(
//                                this@LoginActivity,
//                                task.exception!!.message.toString(),
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//            }
//        }
//    }

    /**
     * A function to notify user that logged in success and get the user details from the FireStore database after authentication.
     */
    fun userLoggedInSuccess(user: User) {

        // Hide the progress dialog.
        hideProgressDialog()

        // Print the user details in the log as of now.
        user.firstName?.let { Log.i("First Name: ", it) }
        user.lastName?.let { Log.i("Last Name: ", it) }
        user.email?.let { Log.i("Email: ", it) }

        // TODO Step 7: Redirect the user to the UserProfile screen if it is incomplete otherwise to the Main screen.
        // START
        if (user.profileCompleted == 0) {
            // If the user profile is incomplete then launch the UserProfileActivity.
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            // Redirect the user to Main Screen after log in.
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }
        finish()
        // END
    }

}