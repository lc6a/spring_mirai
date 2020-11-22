package com.lc.miraispring.controller

import com.lc.spring_mirai.annotation.EventType
import com.lc.spring_mirai.annotation.NeedPermission
import com.lc.spring_mirai.annotation.RequestMapper
import net.mamoe.mirai.message.MessageEvent

const val shutdownPermission = "关机"

@EventType(MessageEvent::class)
@NeedPermission(shutdownPermission)
@RequestMapper("关机")
class ShutdownController {
    @RequestMapper("")
    suspend fun shutdown(event: MessageEvent){
        event.reply("正在关机")
        event.bot.close(null)
    }
}