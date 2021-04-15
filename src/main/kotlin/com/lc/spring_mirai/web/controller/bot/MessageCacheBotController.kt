package com.lc.spring_mirai.web.controller.bot

import com.lc.spring_mirai.annotation.BotController
import com.lc.spring_mirai.annotation.EventFilter
import com.lc.spring_mirai.annotation.RequestMapped
import com.lc.spring_mirai.web.service.MessageCacheService
import net.mamoe.mirai.event.events.MessageEvent
import javax.annotation.Resource

@BotController
@RequestMapped
@EventFilter(MessageEvent::class)
class MessageCacheBotController {

    @Resource
    private lateinit var messageCacheService: MessageCacheService

    @RequestMapped
    fun onMessageEvent(event: MessageEvent) {
        messageCacheService.onMessageEvent(event)
    }
}