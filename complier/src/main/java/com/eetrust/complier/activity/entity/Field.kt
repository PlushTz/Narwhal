package com.eetrust.complier.activity.entity

import com.eetrust.aptutils.types.asKotlinTypeName
import com.squareup.javapoet.ClassName as JavaTypeName
import com.squareup.kotlinpoet.ClassName as KotlinTypeName
import javax.lang.model.element.Modifier
import javax.lang.model.element.VariableElement
import javax.lang.model.type.PrimitiveType

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/5
 * Email: tao351992257@gmail.com
 */
open class Field(val element: VariableElement) : Comparable<Field> {

    val name = element.simpleName.toString()

    open val prefix = "REQUIRED_"

    val isPrivate = Modifier.PRIVATE in element.modifiers

    val isPrimitive = element.asType() is PrimitiveType

    override fun compareTo(other: Field): Int {
        return name.compareTo(other.name)
    }

    fun asJavaTypeName() = JavaTypeName.get(element.asType())
    open fun asKotlinTypeName() = element.asType().asKotlinTypeName().copy(nullable = true)

    override fun toString(): String {
        return "$name:${element.asType()}"
    }
}