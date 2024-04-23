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
 *  * @Date: 3/6/24, 4:50 PM.
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
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.poiesistech.lazyfcm.R
import java.util.concurrent.atomic.AtomicInteger

class LazyFirebaseMessagingService(): FirebaseMessagingService() {

    companion object {
        private const val TAG = "LazyFirebaseMessagingService"
        private val number = AtomicInteger()
        fun getNextInt(): Int {
            return number.incrementAndGet()
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.data.isNotEmpty()){
            val title = message.data["title"]
            val icon = message.data["icon"]
            val shortDesc = message.data["short_desc"]
            val featureImage = message.data["feature"]
            val packageName = message.data["package"]
            if (icon == null || title == null || shortDesc == null) {
                return
            } else {
                Handler(this.mainLooper).post {
                    createNotification(icon, title, shortDesc, featureImage, packageName)
                }
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }


    private fun createNotification(
        icon: String,
        title: String,
        shortDesc: String,
        image: String?,
        storePackage: String?
    ) {
        //Open PlayStore
        val mIntent = if (storePackage != null && isPackageNameSame(storePackage)) {
            openPlayStoreLink(storePackage)
        } else {
            openLazyApp(packageName)
        }

        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                mIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
            )

        //Make Remote Views For text
        val remoteViews = RemoteViews(packageName, R.layout.notification_layout)
        remoteViews.setTextViewText(R.id.txt_title, title)
        remoteViews.setTextViewText(R.id.txt_short_desc, shortDesc)


        if (storePackage != null && isPackageNameSame(storePackage)) {
            remoteViews.setViewVisibility(R.id.txt_ad, View.VISIBLE)
        }
        if (icon.isEmpty()) {
            remoteViews.setViewVisibility(R.id.img_icon, View.GONE)
        }

        //Notification Parameters
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(remoteViews)
            .setAutoCancel(true)

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.default_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        //Build Notification
        val notificationID = getNextInt()
        notificationManager.notify(notificationID, notificationBuilder.build())

        //Set Images into remoteViews
        try {
            // Load icon image using Glide
            Glide.with(this)
                .asBitmap()
                .load(icon)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        remoteViews.setImageViewBitmap(R.id.img_icon, resource)
                        notificationManager.notify(notificationID, notificationBuilder.build())
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Do nothing
                    }
                })

            if (image != null) {
                // Load feature image using Glide
                Glide.with(this)
                    .asBitmap()
                    .load(image)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            remoteViews.setViewVisibility(R.id.img_feature, View.VISIBLE)
                            remoteViews.setImageViewBitmap(R.id.img_feature, resource)
                            notificationManager.notify(notificationID, notificationBuilder.build())
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            // Do nothing
                        }
                    })
            }
        } catch (ignore: IllegalArgumentException) {
        } catch (ignore: java.lang.Exception) {
        } catch (ignore: IllegalStateException) {
        } catch (ignore: Exception) {
        }
    }



    private fun openLazyApp(storePackage: String): Intent {
        return try {
            packageManager.getLaunchIntentForPackage(storePackage) ?: openPlayStoreLink(storePackage)
        } catch (e: Exception) {
            openPlayStoreLink(storePackage)
        }
    }


    private fun openPlayStoreLink(packagePath: String): Intent {
        return try {
            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packagePath"))
        } catch (e: ActivityNotFoundException) {
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packagePath")
            )
        }
    }

    private fun isPackageNameSame(mPackage: String): Boolean {
        return packageName != mPackage
    }

}
