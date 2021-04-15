package com.lc.spring_mirai.demo.controller

import com.lc.spring_mirai.annotation.RequestMapped
import com.lc.spring_mirai.annotation.EventFilter
import com.lc.spring_mirai.annotation.PermissionFilter
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.content
import org.springframework.stereotype.Controller

/**
 * @Author 19525
 * @Date 2021/2/8 22:46
 */
@RequestMapped
//@Controller("test123")    // 取消注释使该控制器生效
@PermissionFilter("收到")
class TestController {

    @RequestMapped
    @EventFilter(MessageEvent::class)
    fun test(event: MessageEvent) = "收到：\n${event.message.content}"
}