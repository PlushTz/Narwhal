package com.eetrust.narwhal.demo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/8
 * Email: lijt@eetrust.com
 */
@Parcelize
data class User(val name: String, val age: Int, val sex: Char) : Parcelable
