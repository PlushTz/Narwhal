package com.eetrust.complier.base

import com.eetrust.complier.activity.entity.Field
import com.squareup.javapoet.TypeSpec

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/8
 * Email: lijt@eetrust.com
 */
abstract class MethodBuilder(val basicClass: BasicClass) {
    val fields = ArrayList<Field>()
    var isStaticMethod = true

    fun staticMethod(staticMethod: Boolean): MethodBuilder {
        this.isStaticMethod = staticMethod
        return this
    }

    fun addAllFields(fields: List<Field>) {
        this.fields.addAll(fields)
    }

    fun addFiled(field: Field) {
        this.fields.add(field)
    }

    abstract fun build(typeBuilder: TypeSpec.Builder)
}