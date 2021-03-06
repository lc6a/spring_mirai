package com.lc.spring_mirai.controller.filter

import com.lc.spring_mirai.annotation.PermissionFilter
import com.lc.spring_mirai.annotation.Priority
import com.lc.spring_mirai.annotation.PriorityNum
import com.lc.spring_mirai.demo.service.PermissionService
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * @Author 19525
 * @Date 2021/2/9 15:41
 */
@Component
@Priority(PriorityNum.HIGH)
class PermissionFilterHandle: AnnotationFilterHandle<PermissionFilter>(PermissionFilter::class.java) {

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('permissionService')}")
    private lateinit var permissionService: PermissionService

    override fun filterByAnnotation(annotations: List<PermissionFilter>, data: FilterData): Boolean {
        if (data.event !is MessageEvent) return true
        val sender = data.event.sender
        val group = if (data.event is GroupMessageEvent) data.event.group else null
        return annotations.all { permissionService.havePermission(sender.id, it.value) }
    }

}