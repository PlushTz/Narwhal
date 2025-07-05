package com.eetrust.aptutils

import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/4
 * Email: tao351992257@gmail.com
 */
object AptContext {
    lateinit var types: Types
    lateinit var elements: Elements
    lateinit var messager: Messager
    lateinit var filer: Filer

    fun init(env: ProcessingEnvironment) {
        elements = env.elementUtils
        types = env.typeUtils
        messager = env.messager
        filer = env.filer
    }
}