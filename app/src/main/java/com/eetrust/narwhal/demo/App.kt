package com.eetrust.narwhal.demo

import android.app.Application
import com.eetrust.narwhal.runtime.ActivityBuilder

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/7
 * Email: tao351992257@gmail.com
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ActivityBuilder.INSTANCE.init(this)
    }
}