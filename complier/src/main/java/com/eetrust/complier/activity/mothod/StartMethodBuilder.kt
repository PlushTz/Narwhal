package com.eetrust.complier.activity.mothod

import com.eetrust.complier.activity.ActivityClass
import com.eetrust.complier.activity.ActivityClassBuilder
import com.eetrust.complier.activity.entity.OptionalField
import com.squareup.javapoet.TypeSpec
import java.util.Locale

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/5
 * Email: tao351992257@gmail.com
 */
class StartMethodBuilder(private val activityClass: ActivityClass) {

    fun build(typeBuilder: TypeSpec.Builder) {
        val startMethod = StartMethod(activityClass, ActivityClassBuilder.METHOD_NAME)
        val groupFields = activityClass.fields.groupBy { it is OptionalField }
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
                    }.build(typeBuilder)
            }
        }
    }
}