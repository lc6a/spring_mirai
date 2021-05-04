package com.lc.spring_mirai.web.service.impl

import com.lc.spring_mirai.web.custom.event.FriendCommandEvent
import com.lc.spring_mirai.web.custom.event.GroupCommandEvent
import com.lc.spring_mirai.web.service.CommandService
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.broadcast
import java.util.*

class CommandServiceImpl: CommandService {
    override suspend fun sendFriendCommand(command: String, friend: Friend) {
        Bot.instances.forEach {
            if (it.isOnline) {
                FriendCommandEvent(friend, (Date().time / 1000).toInt(), it,command).broadcast()
            }
        }

    }

    override suspend fun sendGroupCommand(command: String, groupMember: Member) {
        Bot.instances.forEach {
            if (it.isOnline) {
                GroupCommandEvent(it, command, groupMember, (Date().time / 1000).toInt()).broadcast()
            }
        }
    }
}