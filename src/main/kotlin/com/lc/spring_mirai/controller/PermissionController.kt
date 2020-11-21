package com.lc.spring_mirai.controller

import com.lc.spring_mirai.service.IPermissionService
import com.lc.spring_mirai.annotation.EventType
import com.lc.spring_mirai.annotation.NeedPermission
import com.lc.spring_mirai.annotation.RequestMapper
import com.lc.spring_mirai.exception.PermissionDeniedException
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.At
import org.springframework.beans.factory.annotation.Autowired

@EventType(MessageEvent::class)
@RequestMapper("权限", describe = "权限系统")
class PermissionController {
    @Autowired
    private lateinit var iPermissionService: IPermissionService

    @RequestMapper("查询", describe = "查询自己的权限")
    fun select(event: MessageEvent): String{
        val permissions = iPermissionService.getAllPermission(event.sender.id)
        val sb = StringBuilder(event.senderName).append((", 你的权限有：\n"))
        for (permission in permissions)
            sb.append("[${permission}]").append('\n')
        return sb.toString()
    }

    @RequestMapper("授权/{permission}", describe = "授予某人权限")
    @NeedPermission(IPermissionService.addPermission)
    fun add(event: MessageEvent, permission: String, argc: String): String{
        val at = event.message[At]
        if (at == null && argc.isEmpty())
            return "使用方法：权限 授权 [权限名] [@某人或某人QQ号]"
        val id: Long
        id = at?.target ?: (argc.toLongOrNull() ?: return "QQ号转换失败")
        return try {
            if (iPermissionService.addPermission(id, permission, event.sender.id))
                "授权成功" else "授权失败"
        }catch (e: PermissionDeniedException){
            e.message?:"权限不足"
        }
    }

    @RequestMapper("移除/{permission}", describe = "移除某人权限")
    @NeedPermission(IPermissionService.delPermission)
    fun del(event: MessageEvent, permission: String, argc: String): String{
        val at = event.message[At]
        if (at == null && argc.isEmpty())
            return "使用方法：权限 移除 [权限名] [@某人或某人QQ号]"
        val id: Long
        id = at?.target ?: (argc.toLongOrNull() ?: return "QQ号转换失败")
        return try {
            if (iPermissionService.addPermission(id, permission, event.sender.id))
                "移除权限成功" else "移除权限失败"
        }catch (e: PermissionDeniedException){
            e.message?:"权限不足"
        }
    }
}