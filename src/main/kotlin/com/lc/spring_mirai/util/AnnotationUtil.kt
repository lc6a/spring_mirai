package com.lc.spring_mirai.util

import javax.annotation.*
import javax.swing.text.Document
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

object AnnotationUtil{
    fun <T: Annotation> getAnnotation(clazz: KClass<*>, annoClazz: KClass<T>, sets: MutableSet<KClass<out Annotation>>): T?{
        val annos = clazz.annotations
        loop@ for (anno in annos){
            if (annoClazz.isInstance(anno))
                return anno as T?
            when(anno){
                is Deprecated, is Target, is PostConstruct,
                    is PreDestroy, is Resource,
                    is Resources, is Generated,
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
            val t = getAnnotation<T>(anno.annotationClass, annoClazz)
            if (t != null){
                return t
            }
        }
        return null
    }

    val ignoreAnnoName = listOf("java.lang")

    fun <T: Annotation> getAnnotation(clazz: KClass<*>, annoClazz: KClass<T>): T?{
        val sets = mutableSetOf<KClass<out Annotation>>()
        return getAnnotation(clazz, annoClazz, sets)
    }

    fun <T: Annotation> getAnnotation(func: KFunction<*>, annoClazz: KClass<T>): T?{
        for (anno in func.annotations){
            if (annoClazz.isInstance(anno)){
                return anno as T
            }
        }
        for (anno in func.annotations){
            val ret = getAnnotation(anno::class, annoClazz)
            if (ret != null)
                return ret
        }
        return null
    }
}