package com.lc.spring_mirai.web.service

import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.MessageChain

interface MessageCacheService {
    fun onMessageEvent(event: MessageEvent)

    fun getMessageChainById(id: Int): MessageChain?
}