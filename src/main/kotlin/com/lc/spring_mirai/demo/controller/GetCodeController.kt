package com.lc.spring_mirai.demo.controller

import com.lc.spring_mirai.annotation.BotController
import com.lc.spring_mirai.annotation.EventFilter
import com.lc.spring_mirai.annotation.RequestMapped
import com.lc.spring_mirai.util.ReplyUtil
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.nextMessage
import org.springframework.stereotype.Controller
import javax.annotation.Resource

@BotController
@EventFilter(MessageEvent::class)
@RequestMapped("取码")
class GetCodeController {

    @Resource
    lateinit var replyUtil: ReplyUtil

    @RequestMapped
    suspend fun index(event: MessageEvent): String {
        replyUtil.reply(event, PlainText("请1分钟内发送消息"))
        return event.nextMessage(60 * 1000).serializeToMiraiCode()
    }
}