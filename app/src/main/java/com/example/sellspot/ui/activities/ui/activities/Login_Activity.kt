package com.example.sellspot.ui.activities.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.CheckBox
import com.example.sellspot.R
import com.example.sellspot.databinding.ActivityLoginBinding
import com.example.sellspot.firebase.FirebaseClass
import com.example.sellspot.model.User
import com.example.sellspot.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var rememberMeCheckbox: CheckBox
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        rememberMeCheckbox = findViewById(R.id.checkbox_remember_me)
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        // Load the "Remember Me" preference
        val rememberMe = sharedPreferences.getBoolean("rememberMe", false)
        rememberMeCheckbox.isChecked = rememberMe

        // Load the stored email and password if "Remember Me" is checked
        if (rememberMe) {
            val storedEmail = sharedPreferences.getString("email", "")
            val storedPassword = sharedPreferences.getString("password", "")
            binding.etEmail.setText(storedEmail)
            binding.etPassword.setText(storedPassword)
        }

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
                        // Save the "Remember Me" preference in SharedPreferences
                        val rememberMeChecked = rememberMeCheckbox.isChecked
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("rememberMe", rememberMeChecked)
                        editor.apply()

                        // Save the email and password if "Remember Me" is checked
                        if (rememberMeChecked) {
                            editor.putString("email", email)
                            editor.putString("password", password)
                            editor.apply()
                        } else {
                            // Clear the stored email and password
                            editor.remove("email")
                            editor.remove("password")
                            editor.apply()
                        }

                        FirebaseClass().getUserDetails(this@LoginActivity)
                    } else {
                        // Hide the progress dialog
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

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
