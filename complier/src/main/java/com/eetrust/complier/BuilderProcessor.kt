package com.eetrust.complier

import com.eetrust.annotations.Builder
import com.eetrust.annotations.Optional
import com.eetrust.annotations.Required
import com.eetrust.aptutils.AptContext
import com.eetrust.aptutils.logger.Logger
import com.eetrust.aptutils.types.isSubTypeOf
import com.eetrust.complier.activity.ActivityClass
import com.eetrust.complier.activity.entity.Field
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/5
 * Email: tao351992257@gmail.com
 */
class BuilderProcessor : AbstractProcessor() {
    private val supportedAnnotations =
        setOf(Builder::class.java, Optional::class.java, Required::class.java)

    override fun getSupportedSourceVersion() = SourceVersion.RELEASE_8

    override fun init(processorEnv: ProcessingEnvironment) {
        super.init(processorEnv)
        AptContext.init(processorEnv)
    }

    override fun getSupportedAnnotationTypes() =
        supportedAnnotations.mapTo(HashSet<String>(), Class<*>::getCanonicalName)

    override fun process(annotations: Set<TypeElement?>, env: RoundEnvironment): Boolean {
        val activityClasses = HashMap<Element, ActivityClass>()
        env.getElementsAnnotatedWith(Builder::class.java).filter { it.kind.isClass }
            .forEach { element ->
                try {
                    if (element.asType().isSubTypeOf("android.app.Activity")) {
                        activityClasses[element] = ActivityClass(element as TypeElement)
                    } else {
                        Logger.error(element, "Unsupported typeElement: ${element.simpleName}")
                    }
                } catch (e: Exception) {
                    Logger.error(element, Builder::class.java.simpleName, e)
                }
            }
        env.getElementsAnnotatedWith(Required::class.java).filter { it.kind == ElementKind.FIELD }
            .forEach { element ->
                activityClasses[element.enclosingElement]?.fields?.add(Field(element as VariableElement))
                    ?: Logger.error(element, "Field $element annotated as Required while ${element.enclosingElement} not annotated.")
            }
        env.getElementsAnnotatedWith(Optional::class.java).filter { it.kind == ElementKind.FIELD }
            .forEach { element ->
                activityClasses[element.enclosingElement]?.fields?.add(Field(element as VariableElement))
                    ?: Logger.error(element, "Field $element annotated as Required while ${element.enclosingElement} not annotated.")
            }
        activityClasses.values.forEach { activityClass ->
            activityClass.builder.build(AptContext.filer)
        }
        return true
    }
}