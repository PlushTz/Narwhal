package com.eetrust.annotations

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/4
 * Email: tao351992257@gmail.com
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.BINARY)
annotation class Optional(
    val stringValue: String = "",
    val intValue: Int = 0,
    val floatValue: Float = 0f,
    val booleanValue: Boolean = false,
    val doubleValue: Double = 0.0,
    val longValue: Long = 0L
)
