package com.eetrust.complier.activity.mothod

import com.eetrust.complier.activity.ActivityClass
import com.eetrust.complier.utils.camelToUnderline
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/5
 * Email: tao351992257@gmail.com
 */
class ConstantBuilder(private val activityClass: ActivityClass) {
    fun build(typeBuilder: TypeSpec.Builder) {
        activityClass.fields.forEach { field ->
            val name = field.prefix + field.name.camelToUnderline().uppercase()
            // 1. 对于类型，直接使用 JavaPoet 的 TypeName 获取方式
            val fieldType = TypeName.get(String::class.java) // 或者其他需要的类型

            // 2. 对于修饰符，使用 javax.lang.model.element.Modifier
            val fieldSpec = FieldSpec.builder(String::class.java, name, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("\$S", field.name)
                .build() // 构建 FieldSpec 对象
            typeBuilder.addField(fieldSpec) // 将构建好的 FieldSpec 添加到 TypeSpec.Builder

        }
    }
}