package com.poiesistech.lazy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.poiesistech.lazyfcm.R
import com.poiesistech.lazyfcm.fcm.LazyFCM


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LazyFCM.setupFirebaseMessaging(this, packageName)
    }
}