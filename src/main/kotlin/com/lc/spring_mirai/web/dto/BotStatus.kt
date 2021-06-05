package com.lc.spring_mirai.web.dto

import net.mamoe.mirai.Bot

data class BotStatus(
    var qqId: Long,
    var nickName: String?,
    var image: String?,
    var online: Boolean
) {
    constructor(bot: Bot): this(bot.id, bot.nick, bot.avatarUrl, bot.isOnline)
}