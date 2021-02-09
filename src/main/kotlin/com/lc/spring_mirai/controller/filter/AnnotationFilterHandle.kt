package com.lc.spring_mirai.controller.filter

import com.lc.spring_mirai.annotation.EventFilter
import kotlin.reflect.KClass

/**
 * 基于注解的过滤器处理类
 * @param clazz 注解的class
 * @Author 19525
 * @Date 2021/2/9 15:42
 */
abstract class AnnotationFilterHandle<T: Annotation>(val clazz: Class<T>): FilterHandle {

    override suspend fun filter(data: FilterData): Boolean {
        val annotations = data.annotations.filterIsInstance(clazz)
        if (annotations.isEmpty()) return true
        return filterByAnnotation(annotations, data)
    }

    abstract fun filterByAnnotation(annotations: List<T>, data: FilterData): Boolean
}