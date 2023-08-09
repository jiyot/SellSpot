package com.example.sellspot.model

import android.content.Context
import android.widget.Toast
import com.example.sellspot.ui.activities.ui.activities.CheckoutActivity
import com.razorpay.PaymentResultListener

class PaymentResultListener(private val context: Context) : PaymentResultListener {

    override fun onPaymentSuccess(razorpayPaymentId: String) {
        // Payment successful, call your function to place the order
        (context as CheckoutActivity).placeAnOrder()

        Toast.makeText(context, "Payment successful! Order placed.", Toast.LENGTH_SHORT).show()
    }


    override fun onPaymentError(errorCode: Int, response: String?) {
        // Payment failed. You can handle the failure scenario here.
        // For example, show an error message to the user and allow them to retry the payment.

        Toast.makeText(
            context,
            "Payment failed in class. Error code: $errorCode\nError Response: $response",
            Toast.LENGTH_LONG
        ).show()
    }
}

