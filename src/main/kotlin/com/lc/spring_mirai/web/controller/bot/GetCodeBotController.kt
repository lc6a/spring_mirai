package com.lc.spring_mirai.web.controller.bot

import com.lc.spring_mirai.annotation.BotController
import com.lc.spring_mirai.annotation.EventFilter
import com.lc.spring_mirai.annotation.RequestMapped
import com.lc.spring_mirai.util.ReplyUtil
import com.lc.spring_mirai.web.custom.event.WebCommandEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.nextMessage
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import javax.annotation.Resource

@BotController
@EventFilter(MessageEvent::class)
@RequestMapped("取码")
@ResponseBody
@RequestMapping("getCode")
@CrossOrigin
class GetCodeBotController: BaseBotController() {

    override var showName = "mirai取码"

    private var waitSeconds = 60

    override var managerUrl: String? = "/ctrl/getCode.html"

    @Resource
    lateinit var replyUtil: ReplyUtil

    @RequestMapped
    suspend fun index(event: MessageEvent): String {
        replyUtil.reply(event, PlainText("请${waitSeconds / 60}分钟内发送消息"))
        if (event is WebCommandEvent) {
            return "\n当前是Web端，无法取码\n"
        }
        return event.nextMessage(waitSeconds * 1000L).serializeToMiraiCode()
    }

    @RequestMapping("waitSeconds")
    fun setWaitSeconds(@RequestParam waitSeconds: Int) {
        this.waitSeconds = waitSeconds
    }

}