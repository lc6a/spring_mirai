package com.lc.spring_mirai.web.custom.filter

import com.lc.spring_mirai.annotation.PermissionFilter
import com.lc.spring_mirai.annotation.Priority
import com.lc.spring_mirai.annotation.PriorityNum
import com.lc.spring_mirai.annotation.Replace
import com.lc.spring_mirai.controller.filter.AnnotationFilterHandle
import com.lc.spring_mirai.controller.filter.FilterData
import com.lc.spring_mirai.controller.filter.PermissionFilterHandle
import com.lc.spring_mirai.web.custom.event.WebCommandEvent
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.permission.Permission
import net.mamoe.mirai.console.permission.PermissionId
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.event.events.MessageEvent
import org.springframework.stereotype.Component

@Component
//使用mirai-console权限系统替换旧权限系统
@Replace(PermissionFilterHandle::class)
@Priority(PriorityNum.DEFAULT)
class MiraiConsolePermissionFilterHandle:
    AnnotationFilterHandle<PermissionFilter>(PermissionFilter::class.java) {
    override fun filterByAnnotation(annotations: List<PermissionFilter>, data: FilterData): Boolean {
        if (data.event !is MessageEvent) return true
        val sender = if (data.event is WebCommandEvent) {
            data.event.toCommandSender()
        }
        else {
            data.event.toCommandSender()
        }
        return annotations.all {
            val per = it.value
            sender.hasPermission(MiraiConsolePermissionAdapter.permissionSm2Mc(per))
        }
    }
}


object MiraiConsolePermissionAdapter {
    private const val NAMESPACE = "com.lc.spring_mirai"

    val ROOT = PermissionService.INSTANCE.register(PermissionId(NAMESPACE, "root"), "Spring mirai根权限")

    /**
     * SpringMirai权限 转 Mirai Console权限
     *
     */
    fun permissionSm2Mc(smPermission: String): Permission {
        return PermissionService.INSTANCE[PermissionId(NAMESPACE, smPermission)]
            ?: PermissionService.INSTANCE.register(PermissionId(NAMESPACE, smPermission), smPermission, ROOT)
    }

}