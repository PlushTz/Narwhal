package com.eetrust.complier.utils

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/5
 * Email: tao351992257@gmail.com
 */
fun String.camelToUnderline(): String {
    return fold(StringBuilder()) { acc, c ->
        if (c.isUpperCase()) {
            acc.append("_").append(c.toLowerCase())
        } else acc.append(c)
    }.toString()
}

fun String.underlineToCamel(): String {
    var upperNext = false
    return fold(StringBuilder()) { acc, c ->
        if (c == '_') upperNext = true
        else acc.append(if (upperNext) c.toUpperCase() else c)
        acc
    }.toString()
}