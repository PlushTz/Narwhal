package com.eetrust.complier.activity.entity

import com.eetrust.annotations.Optional
import com.eetrust.aptutils.types.asTypeMirror
import com.eetrust.aptutils.types.isSameTypeWith
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeKind

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/5
 * Email: tao351992257@gmail.com
 */
class OptionalField(element: VariableElement) : Field(element) {

    var defaultValue: Any? = null
        private set

    override val prefix: String
        get() = "OPTIONAL_"

    init {
        val optional = element.getAnnotation(Optional::class.java)
        when (element.asType().kind) {
            TypeKind.BOOLEAN -> {
                defaultValue = optional.booleanValue
            }

            TypeKind.BYTE, TypeKind.SHORT, TypeKind.INT, TypeKind.LONG, TypeKind.CHAR -> {
                defaultValue = optional.longValue
            }

            TypeKind.FLOAT, TypeKind.DOUBLE -> {
                defaultValue = optional.floatValue
            }

            else -> if (element.asType().isSameTypeWith(String::class)) defaultValue =
                """"${optional.stringValue}""""
        }
    }

    override fun compareTo(other: Field): Int {
        return if (other is OptionalField) {
            return super.compareTo(other)
        } else {
            1
        }
    }
}