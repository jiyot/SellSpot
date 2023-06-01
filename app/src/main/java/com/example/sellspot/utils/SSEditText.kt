package com.example.sellspot.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class SSEditText (context: Context, attributesSet: AttributeSet): AppCompatTextView(context, attributesSet){

    init {

        applyFont()

    }

    private fun applyFont(){

        val boldTypeface: Typeface = Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        setTypeface(boldTypeface)
    }
}