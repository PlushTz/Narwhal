package com.eetrust.aptutils.types

import com.eetrust.aptutils.AptContext
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.BYTE
import com.squareup.kotlinpoet.CHAR
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.SHORT
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ArrayType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/4
 * Email: tao351992257@gmail.com
 */
object TypeUtils {

    internal fun doubleErasure(elementType: TypeMirror): String {
        var name = AptContext.types.erasure(elementType).toString()
        val typeParamStart = name.indexOf('<')
        if (typeParamStart != -1) {
            name = name.substring(0, typeParamStart)
        }
        return name
    }

    internal fun getTypeFromClassName(className: String) =
        AptContext.elements.getTypeElement(className)?.asType()
}

fun TypeElement.packageName(): String {
    var element = this.enclosingElement
    while (element != null && element.kind != ElementKind.PACKAGE) {
        element = element.enclosingElement
    }
    return element?.asType()?.toString()
        ?: throw IllegalArgumentException("$this does not have an enclosing element of package.")
}

fun Element.simpleName(): String = simpleName.toString()
fun TypeElement.canonicalName(): String = qualifiedName.toString()

fun TypeMirror.simpleName() = TypeUtils.doubleErasure(this)
    .let { name -> name.substring(name.lastIndexOf(".") + 1) }

fun TypeMirror.erasure() = AptContext.types.erasure(this)

//region subType
fun TypeMirror.isSubTypeOf(className: String): Boolean {
    val type = TypeUtils.getTypeFromClassName(className) ?: return false
    return AptContext.types.isSubtype(this.erasure(), type.erasure())
}

fun TypeMirror.isSubTypeOf(cls: Class<*>): Boolean {
    return cls.canonicalName?.let { className ->
        isSubTypeOf(className)
    } ?: false
}

fun TypeMirror.isSubTypeOf(cls: KClass<*>) = isSubTypeOf(cls.java)

fun TypeMirror.isSubTypeOf(typeMirror: TypeMirror): Boolean {
    return AptContext.types.isSubtype(this.erasure(), typeMirror.erasure())
}
//endregion

//region sameType
fun TypeMirror.isSameTypeWith(typeMirror: TypeMirror): Boolean {
    return AptContext.types.isSameType(this, typeMirror)
}

fun TypeMirror.isSameTypeWith(cls: Class<*>): Boolean {
    return cls.canonicalName?.let { className ->
        isSameTypeWith(className)
    } ?: false
}

fun TypeMirror.isSameTypeWith(cls: KClass<*>) = isSameTypeWith(cls.java)

fun TypeMirror.isSameTypeWith(className: String): Boolean {
    return TypeUtils.getTypeFromClassName(className)
        ?.let { isSameTypeWith(it) } ?: false
}
//endregion

//region Class/KClass
fun Class<*>.asTypeMirror(): TypeMirror {
    return AptContext.elements.getTypeElement(canonicalName).asType()
}

fun Class<*>.asJavaTypeName() = this.asTypeMirror().asJavaTypeName()
fun Class<*>.asKotlinTypeName() = this.asTypeMirror().asKotlinTypeName()
fun Class<*>.asElement() = this.asTypeMirror().asElement()

fun KClass<*>.asTypeMirror(): TypeMirror {
    return AptContext.elements.getTypeElement(qualifiedName/* == java.canonicalName*/).asType()
}

fun KClass<*>.asJavaTypeName() = this.asTypeMirror().asJavaTypeName()
fun KClass<*>.asKotlinTypeName() = this.asTypeMirror().asKotlinTypeName()
fun KClass<*>.asElement() = this.asTypeMirror().asElement()
//endregion

//region TypeMirror
fun TypeMirror.asElement() = AptContext.types.asElement(this)

fun TypeMirror.asJavaTypeName() = com.squareup.javapoet.TypeName.get(this)!!

fun TypeMirror.asKotlinTypeName(): TypeName {
    return when (kind) {
        TypeKind.BOOLEAN -> BOOLEAN
        TypeKind.BYTE -> BYTE
        TypeKind.SHORT -> SHORT
        TypeKind.INT -> INT
        TypeKind.LONG -> LONG
        TypeKind.CHAR -> CHAR
        TypeKind.FLOAT -> FLOAT
        TypeKind.DOUBLE -> DOUBLE
        TypeKind.ARRAY -> {
            val arrayType = this as ArrayType
            when (arrayType.componentType.kind) {
                TypeKind.BOOLEAN -> BOOLEAN_ARRAY
                TypeKind.BYTE -> BYTE_ARRAY
                TypeKind.SHORT -> SHORT_ARRAY
                TypeKind.INT -> INT_ARRAY
                TypeKind.LONG -> LONG_ARRAY
                TypeKind.CHAR -> CHAR_ARRAY
                TypeKind.FLOAT -> FLOAT_ARRAY
                TypeKind.DOUBLE -> DOUBLE_ARRAY
                else -> if (toString() == "java.lang.String[]") STRING_ARRAY else asTypeName()
            }
        }

        else -> if (toString() == "java.lang.String") STRING else asTypeName()
    }
}


private val STRING: ClassName = ClassName("kotlin", "String")
private val STRING_ARRAY = ClassName("kotlin", "Array").parameterizedBy(STRING)
private val LONG_ARRAY: ClassName = ClassName("kotlin", "LongArray")
private val INT_ARRAY: ClassName = ClassName("kotlin", "IntArray")
private val SHORT_ARRAY: ClassName = ClassName("kotlin", "ShortArray")
private val BYTE_ARRAY: ClassName = ClassName("kotlin", "ByteArray")
private val CHAR_ARRAY: ClassName = ClassName("kotlin", "CharArray")
private val BOOLEAN_ARRAY: ClassName = ClassName("kotlin", "BooleanArray")
private val FLOAT_ARRAY: ClassName = ClassName("kotlin", "FloatArray")
private val DOUBLE_ARRAY: ClassName = ClassName("kotlin", "DoubleArray")
//endregion