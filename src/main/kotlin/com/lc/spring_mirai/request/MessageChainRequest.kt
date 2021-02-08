package com.lc.spring_mirai.request

import com.lc.spring_mirai.request.mapping.*
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.code.CodableMessage
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.MessageSource
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.content
import org.springframework.stereotype.Component

/**
 * 消息链请求
 * @param event 消息事件
 * @Author 19525
 * @Date 2021/2/5 0:13
 */
open class MessageChainRequest(override val event: MessageEvent): EventRequest(event) {

     override val mappingItems = run {
        val items = mutableListOf<IMappingItem<*>>()
        for (message in event.message) {
            when (message) {
                is PlainText -> message.content.split(' ').forEach {
                    if (it.isNotEmpty())
                        items.add(TextMappingItem(it))
                }
                is At -> items.add(AtMappingItem(message))
                is CodableMessage -> items.add(MiraiCodeMappingItem(message))
                is MessageSource -> continue
                else -> items.add(UnKnowMappingItem(message.content))
            }
        }
        items
    }
}