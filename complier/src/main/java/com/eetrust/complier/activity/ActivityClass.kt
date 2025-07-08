package com.eetrust.complier.activity

import com.eetrust.complier.base.BasicClass
import javax.lang.model.element.TypeElement

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/5
 * Email: tao351992257@gmail.com
 */
class ActivityClass(typeElement: TypeElement) : BasicClass(typeElement) {
    val builder = ActivityClassBuilder(this)
}