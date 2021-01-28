package com.lc.spring_mirai.util

import kotlin.annotation.*
import javax.annotation.processing.Generated
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass


object AnnotationUtil{
    private fun <T: Annotation> getAnnotation(e: KAnnotatedElement, annoClazz: KClass<T>, sets: MutableSet<KClass<out Annotation>>): T?{
        val annos = e.annotations
        loop@ for (anno in annos){
            if (annoClazz.isInstance(anno))
                return anno as T?
            when(anno){
                is Deprecated, is Target, /*is PostConstruct,
                    is PreDestroy, is Resource,
                    is Resources,*/ is Generated,
                    is Retention, is MustBeDocumented
                    -> continue@loop
            }
            if (sets.contains(anno.annotationClass)){
                continue@loop
            }
            val name = anno.toString()
            for (ignore in ignoreAnnoName){
                if (name.contains(ignore))
                    continue@loop
            }
            sets.add(anno.annotationClass)
            val t = getAnnotation(anno.annotationClass, annoClazz)
            if (t != null){
                return t
            }
        }
        return null
    }

    private val ignoreAnnoName = listOf("java.lang")

    /**
     * 获取类clazz是否有注解T
     */
    fun <T: Annotation> getAnnotation(e: KAnnotatedElement, annoClazz: KClass<T>): T?{
        val sets = mutableSetOf<KClass<out Annotation>>()
        return getAnnotation(e, annoClazz, sets)
    }
}