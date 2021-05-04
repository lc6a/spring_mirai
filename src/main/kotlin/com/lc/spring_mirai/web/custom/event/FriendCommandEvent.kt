package com.lc.spring_mirai.web.custom.event

import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Friend

/**
 * 模拟好友发出的消息
 */
class FriendCommandEvent(
    override val subject: Friend,
    override val time: Int,
    bot: Bot,
    command: String,
) : CommandEvent(bot, command, subject) {

}