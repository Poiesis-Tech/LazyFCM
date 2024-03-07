/*
 * **************************************************************
 *  * @Author: Tariq Hussain
 *  * @Date: 3/7/24, 11:52 AM.
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
 *  * @Date: 3/7/24, 11:52 AM.
 *  * @Accounts
 *  *      -> https://github.com/Tariq2518
 *  *      -> https://www.linkedin.com/in/tariqhpk/
 *  *
 *  * All rights reserved.
 *  * Copying and redistributing are not allowed.
 *  **************************************************************
 */

package com.poiesistech.lazyfcm

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