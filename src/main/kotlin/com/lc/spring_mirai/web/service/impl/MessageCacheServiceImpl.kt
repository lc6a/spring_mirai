package com.lc.spring_mirai.web.service.impl

import com.lc.spring_mirai.web.service.MessageCacheService
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.ids
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.BlockingDeque
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.LinkedBlockingDeque

@Service
class MessageCacheServiceImpl: MessageCacheService {

    private val messageCacheSize: Int = 100

    private val cache = arrayOfNulls<MessageChain>(messageCacheSize)

    private var pos = 0


    override fun onMessageEvent(event: MessageEvent) {
        synchronized(this) {
            cache[pos] = event.message
            pos = (pos + 1) % messageCacheSize
        }
    }

    override fun getMessageChainById(id: Int): MessageChain? {
        return cache.find { it != null && it.ids.contains(id) }
    }
}