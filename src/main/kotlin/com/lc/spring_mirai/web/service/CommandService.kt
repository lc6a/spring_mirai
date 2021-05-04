package com.lc.spring_mirai.web.service

import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Member

interface CommandService {

    suspend fun sendFriendCommand(command: String, friend: Friend)

    suspend fun sendGroupCommand(command: String, groupMember: Member)
}