package com.lc.spring_mirai.web.controller.bot

import com.lc.spring_mirai.annotation.BotController
import com.lc.spring_mirai.annotation.EventFilter
import com.lc.spring_mirai.annotation.RequestMapped
import com.lc.spring_mirai.config.Config
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import org.springframework.beans.factory.annotation.Autowired

@BotController
@EventFilter(NewFriendRequestEvent::class)
class AutoAcceptUserBotController: BaseBotController() {
    override var showName: String = "自动接收加好友申请"

    @Autowired
    private lateinit var config: Config

    @RequestMapped
    suspend fun accept(event: NewFriendRequestEvent) {
        event.accept()
        event.bot.getFriendOrFail(config.rootUserId).sendMessage("${event.fromId}申请添加好友，已自动同意")
    }
}