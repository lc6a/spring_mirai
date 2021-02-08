package com.lc.spring_mirai.request

import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.MessageEvent
import org.springframework.stereotype.Component

/**
 * @Author 19525
 * @Date 2021/2/7 19:07
 */
@Component("defaultRequestFactory")
class RequestFactory {
    fun createRequest(event: Event): Request {
        return when (event) {
            is MessageEvent -> MessageChainRequest(event)
            else -> EventRequest(event)
        }
    }
}