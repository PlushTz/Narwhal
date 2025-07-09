package com.eetrust.complier.activity

import com.eetrust.complier.activity.mothod.ConstantBuilder
import com.eetrust.complier.activity.mothod.InjectMethodBuilder
import com.eetrust.complier.activity.mothod.SaveStateMethodBuilder
import com.eetrust.complier.activity.mothod.StartKotlinFunctionBuilder
import com.eetrust.complier.activity.mothod.StartMethodBuilder
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import com.squareup.kotlinpoet.FileSpec
import javax.annotation.processing.Filer
import javax.lang.model.element.Modifier
import javax.tools.StandardLocation

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/5
 * Email: tao351992257@gmail.com
 */
class ActivityClassBuilder(private val activityClass: ActivityClass) {
    companion object {
        const val POSIX = "Builder"
        const val METHOD_NAME = "start"
        const val METHOD_NAME_NO_OPTIONAL = METHOD_NAME + "WithoutOptional"
        const val METHOD_NAME_FOR_OPTIONAL = METHOD_NAME + "WithOptional"
        const val METHOD_NAME_FOR_OPTIONALS = METHOD_NAME + "WithOptionals"
    }

    fun build(filer: Filer) {
        val typeBuilder = TypeSpec.classBuilder(activityClass.simpleName + POSIX)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        ConstantBuilder(activityClass).build(typeBuilder)
        StartMethodBuilder(activityClass).build(typeBuilder)
        InjectMethodBuilder(activityClass).build(typeBuilder)
        SaveStateMethodBuilder(activityClass).build(typeBuilder)
        if (activityClass.isKotlin) {
            StartKotlinFunctionBuilder(activityClass).build(FileSpec.builder(activityClass.packageName, activityClass.simpleName + POSIX))
        }
        writeJavaToFile(filer, typeBuilder.build())
    }

    private fun writeKotlinToFile(filer: Filer, fileSpec: FileSpec) {
        val fileObject = filer.createResource(StandardLocation.SOURCE_OUTPUT, activityClass.packageName, fileSpec.name + ".kt")
        try {
            fileObject.openWriter()
                .also(fileSpec::writeTo)
                .close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun writeJavaToFile(filer: Filer, typeSpec: TypeSpec) {
        try {
            JavaFile.builder(activityClass.packageName, typeSpec)
                .build()
                .writeTo(filer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}