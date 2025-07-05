package com.eetrust.aptutils.utils

import com.eetrust.aptutils.AptContext
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import com.squareup.kotlinpoet.FileSpec
import java.io.IOException
import javax.tools.StandardLocation

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/4
 * Email: tao351992257@gmail.com
 */
fun FileSpec.writeToFile() {
    try {
        val fileObject = AptContext.filer.createResource(StandardLocation.SOURCE_OUTPUT, packageName,
            "$name.kt"
        )
        val writer = fileObject.openWriter()
        writeTo(writer)
        writer.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun JavaFile.writeToFile(){
    writeTo(AptContext.filer)
}

fun TypeSpec.writeToFile(packageName: String){
    try {
        val file = JavaFile.builder(packageName, this).build()
        file.writeTo(AptContext.filer)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}