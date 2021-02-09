package com.lc.spring_mirai.controller.filter

import com.lc.spring_mirai.annotation.PermissionFilter
import com.lc.spring_mirai.annotation.Priority
import com.lc.spring_mirai.annotation.PriorityNum
import org.springframework.stereotype.Component

/**
 * @Author 19525
 * @Date 2021/2/9 15:41
 */
@Component
@Priority(PriorityNum.HIGH)
class PermissionFilterHandle: AnnotationFilterHandle<PermissionFilter>(PermissionFilter::class.java) {
    override fun filterByAnnotation(annotations: List<PermissionFilter>, data: FilterData): Boolean {
        TODO()
    }

}