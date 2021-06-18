package com.lc.spring_mirai.web.controller.bot

import com.lc.spring_mirai.annotation.BotController
import com.lc.spring_mirai.annotation.EventFilter
import com.lc.spring_mirai.annotation.RequestMapped
import com.lc.spring_mirai.config.Config
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import org.springframework.beans.factory.annotation.Autowired

@BotController
@EventFilter(BotInvitedJoinGroupRequestEvent::class)
class AutoAcceptJoinGroupBotController: BaseBotController() {
    override var showName: String = "自动接受加群邀请"

    @Autowired
    private lateinit var config: Config

    @RequestMapped
    suspend fun accept(event: BotInvitedJoinGroupRequestEvent) {
        event.accept()
        event.bot.getFriendOrFail(config.rootUserId).sendMessage("${event.invitorId}邀请加群${event.groupId}，已自动同意")
    }

}