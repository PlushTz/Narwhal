package com.eetrust.complier.activity.mothod

import com.eetrust.complier.activity.ActivityClass
import com.eetrust.complier.base.BasicClass
import com.eetrust.complier.base.MethodBuilder
import com.eetrust.complier.prebuilt.ACTIVITY
import com.eetrust.complier.prebuilt.BUNDLE
import com.eetrust.complier.prebuilt.INTENT
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/7
 * Email: tao351992257@gmail.com
 */
class SaveStateMethodBuilder(basicClass: BasicClass) : MethodBuilder(basicClass) {
    override fun build(typeBuilder: TypeSpec.Builder) {
        val methodBuilder = MethodSpec.methodBuilder("saveState")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(TypeName.VOID)
            .addParameter(ACTIVITY.java, "instance")
            .addParameter(BUNDLE.java, "outState")
            .beginControlFlow("if (instance instanceof \$T)", basicClass.typeElement)
            .addStatement("\$T typeInstance = (\$T) instance", basicClass.typeElement, basicClass.typeElement)
            .addStatement("\$T intent = new \$T()", INTENT.java, INTENT.java)
        basicClass.fields.forEach { field ->
            val name = field.name
            if (field.isPrivate) {
                methodBuilder.addStatement("intent.putExtra(\$S, typeInstance.get\$L())", name, name.capitalize())
            } else {
                methodBuilder.addStatement("intent.putExtra(\$S, typeInstance.\$L)", name, name)
            }
        }

        methodBuilder.addStatement("outState.putAll(intent.getExtras())")
            .endControlFlow()
        typeBuilder.addMethod(methodBuilder.build())
    }
}