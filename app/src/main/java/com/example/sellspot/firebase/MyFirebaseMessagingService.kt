package com.example.sellspot.firebase


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.sellspot.R
import com.example.sellspot.ui.activities.ui.activities.SpllashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "com.example.sellspot.firebase"

class MyFirebaseMessagingService : FirebaseMessagingService() {
//// generate the notification
//// attach the notification created with the custom layout
////   show the notification
//
//
//    override fun onMessageReceived (remoteMessage: RemoteMessage) {
//        Log.d("TAGN", "Message received: ${remoteMessage.notification?.title} - ${remoteMessage.notification?.body}")
//        if (remoteMessage.notification != null) {
//            generateNotification(
//                remoteMessage.notification!!.title!!,
//                remoteMessage.notification!!.body!!
//            )
//        }
//    }
//
//    @Suppress
//    fun getRemoteView(title: String, message: String): RemoteViews{
//    val remoteView = RemoteViews("com.example.sellspot.firebase", R.layout.notification_reminder)
//        remoteView.setTextViewText(R.id.notification_title,title)
//        remoteView.setTextViewText(R.id.notification_message, message)
//        remoteView.setImageViewResource(R.id.app_logo,R.drawable.sellspot_icon)
//    return remoteView
//}
//
//    fun generateNotification(title: String, message: String) {
//        val intent = Intent(this, SpllashActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent =
//            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
//        //channel id, channel name
//
//        // channel id, channel name
//        var builder: NotificationCompat.Builder =
//            NotificationCompat.Builder(applicationContext, channelId)
//                .setSmallIcon(R.drawable.sellspot_icon)
//                .setAutoCancel(true)
//                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
//                .setOnlyAlertOnce(true)
//                .setContentIntent(pendingIntent)
//
//        builder = builder.setContent(getRemoteView(title, message))
//
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
//            notificationManager.createNotificationChannel(notificationChannel)
//            notificationManager.notify( 0, builder.build())
//
//
//        }
//
//    }
//}

//


    // Override onNewToken to get new token
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    // Override onMessageReceived() method to extract the
    // title and
    // body from the message passed in FCM
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // First case when notifications are received via
        // data event
        // Here, 'title' and 'message' are the assumed names
        // of JSON
        // attributes. Since here we do not have any data
        // payload, This section is commented out. It is
        // here only for reference purposes.
        /*if(remoteMessage.getData().size()>0){
            showNotification(remoteMessage.getData().get("title"),
                          remoteMessage.getData().get("message"));
        }*/

        // Second case when notification payload is
        // received.

        Log.d("TAGN", "Message received: ***** ${remoteMessage.notification?.title} - ${remoteMessage.notification?.body}")
        val notification = remoteMessage.notification
        if (notification != null && notification.title != null && notification.body != null) {
            showNotification(notification.title!!, notification.body!!)
        }
    }

    // Method to get the custom Design for the display of
    // notification.
    private fun getCustomDesign(
        title: String,
        message: String
    ): RemoteViews {
        val remoteViews = RemoteViews(
            applicationContext.getPackageName(),
            R.layout.notification_reminder
        )

        remoteViews.setTextViewText(R.id.notification_title, title)
        remoteViews.setTextViewText(R.id.notification_message, message)
        remoteViews.setImageViewResource(
            R.id.app_logo,
            R.drawable.sellspot_icon
        )
        return remoteViews
    }

    // Method to display the notifications
    fun showNotification(
        title: String,
        message: String
    ) {
        // Pass the intent to switch to the MainActivity
        val intent = Intent(this, SpllashActivity::class.java)
        // Assign channel ID
        val channel_id = channelId

        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // Pass the intent to PendingIntent to start the
        // next Activity
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )


        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            channel_id
        )

            .setSmallIcon(R.drawable.sellspot_icon)
            .setAutoCancel(true)
            .setVibrate(
                longArrayOf(
                    1000, 1000, 1000,
                    1000, 1000
                )
            )
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.
        builder = builder.setContent(
            getCustomDesign(title, message)
        )
        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT
            >= Build.VERSION_CODES.O
        ) {
            val notificationChannel = NotificationChannel(
                channel_id, "web_app",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager!!.createNotificationChannel(
                notificationChannel
            )
        }
        notificationManager!!.notify(0, builder.build())
    }
}