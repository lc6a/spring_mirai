package com.lc.spring_mirai.demo.controller

import com.lc.spring_mirai.annotation.EventFilter
import com.lc.spring_mirai.annotation.PermissionFilter
import com.lc.spring_mirai.annotation.RequestMapped
import com.lc.spring_mirai.util.ReplyUtil
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.PlainText
import org.springframework.stereotype.Controller
import javax.annotation.Resource

/**
 * @Author 19525
 * @Date 2021/2/14 23:24
 */
const val SHUTDOWN_PERMISSION = "关机"
@Controller
@EventFilter(MessageEvent::class)
@PermissionFilter(SHUTDOWN_PERMISSION)
@RequestMapped("关机")
class ShutdownController {

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('replyUtil')}")
    private lateinit var replyUtil: ReplyUtil

    @RequestMapped
    suspend fun shutdown(event: MessageEvent) {
        replyUtil.reply(event, PlainText("正在关机"))
        event.bot.close()
    }
}