/*
 * **************************************************************
 *  * @Author: Tariq Hussain
 *  * @Date: 3/7/24, 1:41 AM.
 *  * @Accounts
 *  *      -> https://github.com/Tariq2518
 *  *      -> https://www.linkedin.com/in/tariqhpk/
 *  *
 *  * All rights reserved.
 *  * Copying and redistributing are not allowed.
 *  **************************************************************
 */

/*
 * **************************************************************
 *  * @Author: Tariq Hussain
 *  * @Date: 3/7/24, 1:38 AM.
 *  * @Accounts
 *  *      -> https://github.com/Tariq2518
 *  *      -> https://www.linkedin.com/in/tariqhpk/
 *  *
 *  * All rights reserved.
 *  * Copying and redistributing are not allowed.
 *  **************************************************************
 */

/*
 * **************************************************************
 *  * @Author: Tariq Hussain
 *  * @Date: 3/6/24, 3:46 PM.
 *  * @Accounts
 *  *      -> https://github.com/Tariq2518
 *  *      -> https://www.linkedin.com/in/tariqhpk/
 *  *
 *  * All rights reserved.
 *  * Copying and redistributing are not allowed.
 *  **************************************************************
 */

package com.poiesistech.lazyfcm.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.poiesistech.lazyfcm.R
import kotlinx.coroutines.runBlocking

class LazyFCM {
    companion object{
        fun setupFirebaseMessaging(context: Context, topic: String){
            runBlocking {
                initializeFirebase(context)
                createChannelForFirebaseMessagingService(context)
                FirebaseMessaging.getInstance().subscribeToTopic(topic)
            }
        }

        private fun createChannelForFirebaseMessagingService(context: Context) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = context.getString(R.string.default_notification_channel_id)
                val channelName = context.getString(R.string.default_notification_channel_name)
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(
                    NotificationChannel(
                        channelId,
                        channelName, NotificationManager.IMPORTANCE_DEFAULT
                    )
                )
            }

        }

        private fun initializeFirebase(context: Context) {
            try {
                FirebaseApp.initializeApp(context)
            } catch (e: Exception) {
                Log.e("LazyFcmLog", "onCreate: ${e.message}")
            }

        }

        fun removeFirebaseMessaging(topic: String){
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
        }
    }
}