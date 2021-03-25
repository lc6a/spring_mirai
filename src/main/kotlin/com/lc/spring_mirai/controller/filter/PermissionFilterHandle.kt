package com.lc.spring_mirai.controller.filter

import com.lc.spring_mirai.annotation.PermissionFilter
import com.lc.spring_mirai.annotation.Priority
import com.lc.spring_mirai.annotation.PriorityNum
import com.lc.spring_mirai.demo.service.IPermissionService
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @Author 19525
 * @Date 2021/2/9 15:41
 */
@Component
@Priority(PriorityNum.HIGH)
class PermissionFilterHandle: AnnotationFilterHandle<PermissionFilter>(PermissionFilter::class.java) {

    @Autowired
    private lateinit var permissionService: IPermissionService

    override fun filterByAnnotation(annotations: List<PermissionFilter>, data: FilterData): Boolean {
        if (data.event !is MessageEvent) return true
        val sender = data.event.sender
        val group = if (data.event is GroupMessageEvent) data.event.group else null
        return annotations.all { permissionService.havePermission(sender.id, it.value) }
    }

}