package com.eetrust.complier.activity.mothod

import com.eetrust.complier.activity.ActivityClassBuilder
import com.eetrust.complier.activity.entity.OptionalField
import com.eetrust.complier.base.BasicClass
import com.eetrust.complier.base.MethodBuilder
import com.eetrust.complier.prebuilt.INTENT
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import java.util.Locale
import javax.lang.model.element.Modifier

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/5
 * Email: tao351992257@gmail.com
 */
class StartMethodBuilder(basicClass: BasicClass) : MethodBuilder(basicClass) {

    override fun build(typeBuilder: TypeSpec.Builder) {
        val startMethod = StartMethod(basicClass, ActivityClassBuilder.METHOD_NAME)
        val groupFields = basicClass.fields.groupBy { it is OptionalField }
        val requiredFields = groupFields[false] ?: emptyList()
        val optionalFields = groupFields[true] ?: emptyList()

        startMethod.addAllFields(requiredFields)

        val startMethodNoOptional = startMethod.copy(ActivityClassBuilder.METHOD_NAME_NO_OPTIONAL)

        startMethod.addAllFields(optionalFields)
        startMethod.build(typeBuilder)

        if (optionalFields.isEmpty()) {
            startMethodNoOptional.build(typeBuilder)
        }

        if (optionalFields.size < 3) {
            optionalFields.forEach { field ->
                startMethodNoOptional.copy(ActivityClassBuilder.METHOD_NAME_FOR_OPTIONAL + field.name.capitalize(Locale.ROOT))
                    .also {
                        it.addFiled(field)
                    }
                    .build(typeBuilder)
            }
        } else {
            val builderName = basicClass.simpleName + ActivityClassBuilder.POSIX
            val fillIntentMethodBuilder = MethodSpec.methodBuilder("fillIntent")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(INTENT.java, "intent")
            val buildClassName = ClassName.get(basicClass.packageName, builderName)
            optionalFields.forEach { field ->
                typeBuilder.addField(FieldSpec.builder(field.asJavaTypeName(), field.name, Modifier.PRIVATE)
                    .build())
                typeBuilder.addMethod(MethodSpec.methodBuilder(field.name)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(field.asJavaTypeName(), field.name)
                    .addStatement("this.${field.name} = ${field.name}")
                    .addStatement("return this")
                    .returns(buildClassName)
                    .build())
                if (field.isPrimitive) {
                    fillIntentMethodBuilder.addStatement("intent.putExtra(\$S,\$L)", field.name, field.name)
                } else {
                    fillIntentMethodBuilder.beginControlFlow("if (\$L != null)", field.name)
                        .addStatement("intent.putExtra(\$S,\$L)", field.name, field.name)
                        .endControlFlow()
                }
            }
            typeBuilder.addMethod(fillIntentMethodBuilder.build())
            startMethodNoOptional.copy(ActivityClassBuilder.METHOD_NAME_FOR_OPTIONALS)
                .staticMethod(false)
                .build(typeBuilder)
        }
    }
}