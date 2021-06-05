package com.lc.spring_mirai.demo.controller

import com.lc.spring_mirai.annotation.BotController
import com.lc.spring_mirai.annotation.EventFilter
import com.lc.spring_mirai.annotation.PermissionFilter
import com.lc.spring_mirai.annotation.RequestMapped
import com.lc.spring_mirai.demo.controller.ShutdownController.Companion.SHUTDOWN_PERMISSION
import com.lc.spring_mirai.util.ReplyUtil
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.PlainText
import javax.annotation.Resource
import kotlin.system.exitProcess

/**
 * @Author 19525
 * @Date 2021/2/14 23:24
 */
@BotController
@EventFilter(MessageEvent::class)   //事件类型过滤
@PermissionFilter(SHUTDOWN_PERMISSION)  //权限过滤
@RequestMapped("关机")    //消息路径映射
class ShutdownController {

    companion object{
        const val SHUTDOWN_PERMISSION = "关机"
    }

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('replyUtil')}")
    private lateinit var replyUtil: ReplyUtil

    @RequestMapped
    suspend fun shutdown(event: MessageEvent) {
        replyUtil.reply(event, PlainText("正在关机"))
        event.bot.close()
        exitProcess(0)
    }
}