package com.eetrust.complier.activity.mothod

import com.eetrust.complier.activity.ActivityClass
import com.eetrust.complier.activity.ActivityClassBuilder
import com.eetrust.complier.activity.entity.OptionalField
import com.eetrust.complier.base.BasicClass
import com.eetrust.complier.base.MethodBuilder
import com.eetrust.complier.prebuilt.ACTIVITY_BUILDER
import com.eetrust.complier.prebuilt.CONTEXT
import com.eetrust.complier.prebuilt.INTENT
import com.squareup.javapoet.TypeSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.UNIT

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/9
 * Email: lijt@eetrust.com
 */
class StartKotlinFunctionBuilder(private val activityClass: ActivityClass) {


    fun build(fileBuilder: FileSpec.Builder) {
        val name = ActivityClassBuilder.METHOD_NAME + activityClass.simpleName
        val functionBuilder = FunSpec.builder(name)
            .receiver(CONTEXT.kotlin)
            .addModifiers(KModifier.PUBLIC)
            .returns(UNIT)
            .addStatement("val intent = %T(this,%T::class.java)", INTENT.kotlin, activityClass.typeElement)

        activityClass.fields.forEach { field ->
            val fieldName = field.name
            val className = field.asKotlinTypeName()
            if (field is OptionalField) {
                functionBuilder.addParameter(ParameterSpec.builder(fieldName, className)
                    .defaultValue("null")
                    .build())
            } else {
                functionBuilder.addParameter(fieldName, className)
            }

            functionBuilder.addStatement("intent.putExtra(%S,%L)", fieldName, fieldName)
        }

        functionBuilder.addStatement("%T.INSTANCE.startActivity(this,intent)", ACTIVITY_BUILDER.kotlin)
        fileBuilder.addFunction(functionBuilder.build())
    }

}