package com.lc.spring_mirai.util

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageSource
import org.springframework.stereotype.Component

/**
 * @Author 19525
 * @Date 2021/2/8 23:43
 */
@Component("defaultReplyUtil")
class ReplyUtil {
    /**
     * 这里设置成var，方便更改（例如动态全局设置成匿名发送、回复等）
     */
    var reply: suspend (MessageEvent, Message) -> MessageReceipt<Contact>
            = { event: MessageEvent, message: Message ->
        event.subject.sendMessage(message)
    }
}