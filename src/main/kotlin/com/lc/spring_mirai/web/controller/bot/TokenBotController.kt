package com.lc.spring_mirai.web.controller.bot

import com.lc.spring_mirai.annotation.BotController
import com.lc.spring_mirai.annotation.RequestMapped
import com.lc.spring_mirai.web.token.TokenUtil
import net.mamoe.mirai.event.events.MessageEvent
import javax.annotation.Resource

@BotController
@RequestMapped
class TokenBotController: BaseBotController() {
    override var showName: String = "Token分配器"

    @Resource
    private lateinit var tokenUtil: TokenUtil

    @RequestMapped("token")
    fun token(event: MessageEvent): String {
        return tokenUtil.createToken(event.sender.id)
    }
}