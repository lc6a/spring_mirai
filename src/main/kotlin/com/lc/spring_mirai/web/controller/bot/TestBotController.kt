package com.lc.spring_mirai.web.controller.bot

import com.lc.spring_mirai.annotation.BotController
import com.lc.spring_mirai.annotation.EventFilter
import com.lc.spring_mirai.annotation.GroupFilter
import com.lc.spring_mirai.annotation.RequestMapped
import com.lc.spring_mirai.web.service.TestService
import net.mamoe.mirai.event.events.GroupMessageEvent
import javax.annotation.Resource

@BotController("test1221")
@EventFilter(GroupMessageEvent::class)
@RequestMapped
class TestBotController: BaseBotController() {

    @Resource
    private lateinit var testService: TestService

    override var showName: String = "测试控制器1221"

    @GroupFilter(includeGroupId = [1])
    @RequestMapped("test")
    public fun test() = testService.test()

}