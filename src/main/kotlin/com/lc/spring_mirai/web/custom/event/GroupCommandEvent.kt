package com.lc.spring_mirai.web.custom.event

import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Member

/**
 * 模拟群消息
 */
class GroupCommandEvent(
    bot: Bot,
    command: String,
    sender: Member,
    override val time: Int
) : CommandEvent(bot, command, sender) {
    override val subject: Contact = sender.group

}