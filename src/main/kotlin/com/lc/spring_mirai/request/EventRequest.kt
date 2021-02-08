package com.lc.spring_mirai.request

import com.lc.spring_mirai.request.mapping.IMappingItem
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.MessageEvent

/**
 * @Author 19525
 * @Date 2021/2/7 20:03
 */
open class EventRequest(open val event: Event): Request {
    override val mappingItems: List<IMappingItem<*>> = when (event) {
        is MessageEvent -> MessageChainRequest(event as MessageEvent).mappingItems
        else -> emptyList()
    }
}