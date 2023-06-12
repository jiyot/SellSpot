package com.example.sellspot.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sellspot.R
import com.example.sellspot.databinding.ActivityLoginBinding
import com.example.sellspot.databinding.ActivityMainBinding
import com.example.sellspot.utils.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO Step 4: Get the stored username from the Android SharedPreferences.
        // START
        // Create an instance of Android SharedPreferences
        val sharedPreferences =
            getSharedPreferences(Constants.SELLSPOT_PREFERENCES, Context.MODE_PRIVATE)

        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!
        // Set the result to the tv_main.
        binding.tvMain.text= "The logged in user is $username."
        // END
    }
}