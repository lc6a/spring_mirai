package com.lc.spring_mirai.controller.filter

import com.lc.spring_mirai.annotation.EventFilter
import com.lc.spring_mirai.annotation.Priority
import com.lc.spring_mirai.annotation.PriorityNum
import org.springframework.stereotype.Component
import kotlin.reflect.full.isSuperclassOf

/**
 * @Author 19525
 * @Date 2021/2/8 23:15
 */
@Component
@Priority(PriorityNum.HIGHEST)
class EventFilterHandle : AnnotationFilterHandle<EventFilter>(EventFilter::class.java) {
    /**
     * 过滤器，返回是否通过要求
     * 如果是注解的过滤器，如果没有该注解，一般返回true
     */
    override fun filterByAnnotation(annotations: List<EventFilter>, data: FilterData): Boolean {
        return annotations.all { it.value.isSuperclassOf(data.event::class) }
    }

}