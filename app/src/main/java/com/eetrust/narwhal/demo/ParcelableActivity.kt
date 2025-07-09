package com.eetrust.narwhal.demo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.eetrust.annotations.Builder
import com.eetrust.annotations.Optional

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/8
 * Email: lijt@eetrust.com
 */
@Builder
class ParcelableActivity : AppCompatActivity() {
    @Optional
    lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ParcelableActivityBuilder.inject(this, savedInstanceState)
        Log.d("TAG", "$user")
    }
}