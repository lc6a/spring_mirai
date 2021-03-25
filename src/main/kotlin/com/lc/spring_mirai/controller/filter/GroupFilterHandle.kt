package com.lc.spring_mirai.controller.filter

import com.lc.spring_mirai.annotation.GroupFilter
import com.lc.spring_mirai.annotation.Priority
import com.lc.spring_mirai.annotation.PriorityNum
import net.mamoe.mirai.event.events.GroupEvent
import org.springframework.stereotype.Component

/**
 * @Author 19525
 * @Date 2021/2/9 16:05
 */
@Component
@Priority(PriorityNum.HIGH)
class GroupFilterHandle: AnnotationFilterHandle<GroupFilter>(GroupFilter::class.java) {
    override fun filterByAnnotation(annotations: List<GroupFilter>, data: FilterData): Boolean {
        if (data.event !is GroupEvent) return true
        val group = data.event.group.id
        return annotations.all {
            val include = it.includeGroupId
            if (include.isNotEmpty()) {
                include.contains(group)
            } else {
                !it.ignoreGroupId.contains(group)
            }
        }
    }

}