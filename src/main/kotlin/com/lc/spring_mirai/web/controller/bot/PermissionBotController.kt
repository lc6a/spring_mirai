package com.lc.spring_mirai.web.controller.bot

import com.lc.spring_mirai.annotation.BotController
import com.lc.spring_mirai.annotation.EventFilter
import com.lc.spring_mirai.annotation.RequestMapped
import com.lc.spring_mirai.demo.service.PermissionService
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.event.events.MessageEvent
import org.springframework.beans.factory.annotation.Autowired

private const val ADD_PERM = "addPerm"

@BotController
@EventFilter(MessageEvent::class)
@RequestMapped("权限")
class PermissionBotController: BaseBotController() {
    override var showName: String = "权限管理"

    @Autowired
    private lateinit var permissionService: PermissionService

    @RequestMapped("授权/{perm}/{qqId}")
    fun add(perm: String, qqId: Long , event: MessageEvent): String {
        permissionService.addPermission(qqId, perm, event.sender.id)
        return Return.success
    }

    @RequestMapped("取消授权/{perm}/{qqId}")
    fun del(perm: String, qqId: Long, event: MessageEvent): String {
        permissionService.delPermission(qqId, perm, event.sender.id)
        return Return.success
    }

    @RequestMapped("我的权限")
    fun select(sender: User): String {
        val perms = permissionService.getAllPermission(sender.id)
        val sb = buildString {
            append("你的权限有：\n")
            perms.forEach {
                append(it).append('\n')
            }
        }
        return sb.toString()
    }
}