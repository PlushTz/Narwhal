package com.eetrust.complier.activity.mothod

import com.eetrust.complier.activity.ActivityClass
import com.eetrust.complier.activity.entity.Field
import com.eetrust.complier.prebuilt.ACTIVITY_BUILDER
import com.eetrust.complier.prebuilt.CONTEXT
import com.eetrust.complier.prebuilt.INTENT
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/5
 * Email: tao351992257@gmail.com
 */
class StartMethod(private val activityClass: ActivityClass, private val name: String) {
    private val fields = ArrayList<Field>()
    private var isStaticMethod = true

    fun staticMethod(staticMethod: Boolean): StartMethod {
        this.isStaticMethod = staticMethod
        return this
    }

    fun addAllFields(fields: List<Field>) {
        this.fields.addAll(fields)
    }

    fun addFiled(field: Field) {
        this.fields.add(field)
    }

    fun copy(name: String) = StartMethod(activityClass, name).also {
        it.addAllFields(fields)
    }

    fun build(typeBuilder: TypeSpec.Builder) {
        val methodBuilder = MethodSpec.methodBuilder(name)
            .addModifiers(Modifier.PUBLIC)
            .returns(TypeName.VOID)
            .addParameter(CONTEXT.java, "context")
        methodBuilder.addStatement("\$T intent = new \$T(context, \$T.class)", INTENT.java, INTENT.java, activityClass.typeElement)
        fields.forEach { field ->
            val name = field.name
            methodBuilder.addParameter(field.asJavaTypeName(), name)
                // intent.putExtra("age", age)
                .addStatement("intent.putExtra(\$S, \$L)", name, name)
        }
        if (isStaticMethod) {
            methodBuilder.addModifiers(Modifier.STATIC)
        } else {
            methodBuilder.addStatement("fillIntent(intent)")
        }
        methodBuilder.addStatement("\$T.INSTANCE.startActivity(context, intent)", ACTIVITY_BUILDER.java)
        typeBuilder.addMethod(methodBuilder.build())
    }
}