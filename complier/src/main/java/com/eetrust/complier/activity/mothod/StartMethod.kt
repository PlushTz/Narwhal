package com.eetrust.complier.activity.mothod

import com.eetrust.complier.base.BasicClass
import com.eetrust.complier.base.MethodBuilder
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
class StartMethod(basicClass: BasicClass, private val name: String) : MethodBuilder(basicClass) {

    fun copy(name: String) = StartMethod(basicClass, name).also {
        it.addAllFields(fields)
    }

    override fun build(typeBuilder: TypeSpec.Builder) {
        val methodBuilder = MethodSpec.methodBuilder(name)
            .addModifiers(Modifier.PUBLIC)
            .returns(TypeName.VOID)
            .addParameter(CONTEXT.java, "context")
        methodBuilder.addStatement("\$T intent = new \$T(context, \$T.class)", INTENT.java, INTENT.java, basicClass.typeElement)
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