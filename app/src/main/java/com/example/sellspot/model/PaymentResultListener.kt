package com.example.sellspot.model

import android.content.Context
import android.widget.Toast
import com.example.sellspot.ui.activities.ui.activities.CheckoutActivity
import com.razorpay.PaymentResultListener

class PaymentResultListener(private val context: Context) : PaymentResultListener {
    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        // Payment successful. You can implement your own logic here.
        // For example, update the order status and show a success message to the user.

        Toast.makeText(
            context,
            "Payment successful. Payment ID: $razorpayPaymentID",
            Toast.LENGTH_SHORT
        ).show()

        // Call the function to place an order after successful payment
        (context as CheckoutActivity).placeAnOrder()
    }

    override fun onPaymentError(errorCode: Int, response: String?) {
        // Payment failed. You can handle the failure scenario here.
        // For example, show an error message to the user and allow them to retry the payment.

        Toast.makeText(
            context,
            "Payment failed. Error code: $errorCode\nError Response: $response",
            Toast.LENGTH_LONG
        ).show()
    }
}

