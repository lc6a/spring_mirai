package com.lc.spring_mirai.common.controller

import com.lc.spring_mirai.controller.annotation.RequestMapped
import com.lc.spring_mirai.controller.filter.EventFilter
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.content
import org.springframework.stereotype.Controller

/**
 * @Author 19525
 * @Date 2021/2/8 22:46
 */
@Controller
@RequestMapped
class TestController {

    @RequestMapped
    @EventFilter(MessageEvent::class)
    fun test(event: MessageEvent) = "收到：\n${event.message.content}"
}