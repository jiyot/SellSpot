package com.example.sellspot.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class SSButton(context: Context, attributesSet: AttributeSet): AppCompatButton(context,attributesSet) {

    init {

        applyFont()

    }

    private fun applyFont(){

        val boldTypeface: Typeface = Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        setTypeface(boldTypeface)
    }
}