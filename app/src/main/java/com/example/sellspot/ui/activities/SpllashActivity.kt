package com.example.sellspot.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.sellspot.R
import com.example.sellspot.ui.LoginActivity


class SpllashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spllash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed(
            {
                // Launch the Main Activity
                startActivity(Intent(this@SpllashActivity, DashboardActivity::class.java))
                finish() // Call this when your activity is done and should be closed.
            },
            2500
        )

//        val typeface: Typeface = Typeface.createFromAsset(assets, "Montserrat-Bold.ttf")
//       tv_app_name.typeface = typeface

    }//onCreate
}

