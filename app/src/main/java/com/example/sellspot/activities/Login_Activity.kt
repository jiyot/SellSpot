package com.example.sellspot.activities

import BaseActivity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.example.sellspot.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

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

    private fun loginActivity() {
        when {
            TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@LoginActivity,
                    "Please enter email.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@LoginActivity,
                    "Please enter password.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {

                val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
                val password: String = binding.etPassword.text.toString().trim { it <= ' ' }

                // Log-In using FirebaseAuth
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {

                            Toast.makeText(
                                this@LoginActivity,
                                "You are logged in successfully.",
                                Toast.LENGTH_SHORT
                            ).show()

                            /**
                             * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
                             * and send him to Main Screen with user id and email that user have used for registration.
                             */

                            val intent =
                                Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra(
                                "user_id",
                                FirebaseAuth.getInstance().currentUser!!.uid
                            )
                            intent.putExtra("email_id", email)
                            startActivity(intent)
                            finish()
                        } else {

                            // If the login is not successful then show error message.
                            Toast.makeText(
                                this@LoginActivity,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}
