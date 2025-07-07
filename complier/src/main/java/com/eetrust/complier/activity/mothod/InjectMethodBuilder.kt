package com.eetrust.complier.activity.mothod

import com.eetrust.complier.activity.ActivityClass
import com.eetrust.complier.activity.entity.OptionalField
import com.eetrust.complier.prebuilt.ACTIVITY
import com.eetrust.complier.prebuilt.BUNDLE
import com.eetrust.complier.prebuilt.BUNDLE_UTILS
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
class InjectMethodBuilder(private val activityClass: ActivityClass) {
    fun build(typeBuilder: TypeSpec.Builder) {
        val injectMethodBuilder = MethodSpec.methodBuilder("inject")
            .addParameter(ACTIVITY.java, "instance")
            .addParameter(BUNDLE.java, "savedInstanceState")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(TypeName.VOID)
            .beginControlFlow("if (instance instanceof \$T)", activityClass.typeElement)
            .addStatement("\$T typeInstance = (\$T) instance", activityClass.typeElement, activityClass.typeElement)
            .addStatement("\$T extras = savedInstanceState == null ? typeInstance.getIntent().getExtras() : savedInstanceState", BUNDLE.java)
            .beginControlFlow("if (extras != null)")

        activityClass.fields.forEach { field ->
            val fieldName = field.name
            val typeName = field.asJavaTypeName()
            if (field is OptionalField) {
                injectMethodBuilder.addStatement("\$T \$L = \$T.<\$T>get(extras,\$S,\$L)", typeName, fieldName, BUNDLE_UTILS.java, typeName, fieldName, field.defaultValue)
            } else {
                injectMethodBuilder.addStatement("\$T \$L = \$T.<\$T>get(extras,\$S)", typeName, fieldName, BUNDLE_UTILS.java, typeName, fieldName)
            }
            if (field.isPrivate) {
                injectMethodBuilder.addStatement("typeInstance.set\$L(\$L)", fieldName.capitalize(), fieldName)
            } else {
                injectMethodBuilder.addStatement("typeInstance.\$L = \$L", fieldName, fieldName)
            }
        }
        injectMethodBuilder.endControlFlow().endControlFlow()
        typeBuilder.addMethod(injectMethodBuilder.build())
    }
}