package com.eetrust.complier.base

import com.eetrust.aptutils.types.packageName
import com.eetrust.aptutils.types.simpleName
import com.eetrust.complier.activity.ActivityClassBuilder
import com.eetrust.complier.activity.entity.Field
import java.util.TreeSet
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/8
 * Email: lijt@eetrust.com
 */
abstract class BasicClass(val typeElement: TypeElement) {
    val simpleName: String = typeElement.simpleName()

    val packageName: String = typeElement.packageName()

    val fields = TreeSet<Field>()

    val isAbstract = typeElement.modifiers.contains(Modifier.ABSTRACT)

    val isKotlin = typeElement.getAnnotation(META_DATA) != null

    companion object {
        val META_DATA = Class.forName("kotlin.Metadata") as Class<Annotation>
    }

    override fun toString(): String {
        return "$packageName.$packageName[${fields.joinToString()}]"
    }
}