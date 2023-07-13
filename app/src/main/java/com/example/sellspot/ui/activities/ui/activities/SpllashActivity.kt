package com.example.sellspot.ui.activities.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.sellspot.R
import com.google.firebase.FirebaseApp

class SpllashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spllash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        FirebaseApp.initializeApp(this)

        // Check if the activity is started by a deep link
        val data: Uri? = intent.data
        if (data != null) {
            // Handle the deep link data here
            // You can extract the necessary information from the 'data' URI
            // and perform any required actions

            // For example, you can extract query parameters like this:
            val productId = data.getQueryParameter("productId")
            val userId = data.getQueryParameter("userId")

            // Log the productId and userId
            Log.d("DeepLink", "Product ID: $productId")
            Log.d("DeepLink", "User ID: $userId")

            // Perform the necessary actions based on the extracted data
            // ...

            // Proceed to the main flow of your app
            launchMainActivity()
        } else {
            // If not started by a deep link, proceed to the main flow immediately
            launchMainActivity()
        }
    }

    private fun launchMainActivity() {
        Handler().postDelayed(
            {
                // Launch the Main Activity
                startActivity(Intent(this@SpllashActivity, LoginActivity::class.java))
                finish() // Call this when your activity is done and should be closed.
            },
            2500
        )
    }
}
