package com.lc.spring_mirai.web.controller

import com.lc.spring_mirai.annotation.BotController
import com.lc.spring_mirai.annotation.RequestMapped
import com.lc.spring_mirai.web.controller.bot.BaseBotController
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.utils.MiraiLogger

@BotController
@RequestMapped
class PrintEventNameBotController: BaseBotController() {
    final override var showName: String = "打印事件名称"

    private val logger = MiraiLogger.create(showName)

    @RequestMapped
    fun show(event: Event) {
        logger.info(event.toString())
    }
}

private fun MiraiLogger.Companion.create(showName: String): MiraiLogger {
    return MiraiLogger.Factory.create(MiraiLogger::class, showName);
}
