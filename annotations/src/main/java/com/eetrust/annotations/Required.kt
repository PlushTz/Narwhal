package com.eetrust.annotations

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/5
 * Email: tao351992257@gmail.com
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.BINARY)
annotation class Required(val key: String = "")
