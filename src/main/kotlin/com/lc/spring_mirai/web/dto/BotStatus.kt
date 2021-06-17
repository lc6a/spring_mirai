package com.lc.spring_mirai.web.dto

import net.mamoe.mirai.Bot

data class BotStatus(
    var qqId: Long,
    var nickName: String?,
    var image: String?,
    var online: Boolean
) {
    constructor(bot: Bot): this(bot.id, kotlin.runCatching { bot.nick }.getOrDefault("未知") ,
        kotlin.runCatching { bot.avatarUrl }.getOrDefault("未知") , bot.isOnline)

}