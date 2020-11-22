package com.lc.miraispring.controller

import com.lc.spring_mirai.annotation.EventType
import com.lc.spring_mirai.annotation.NotEnd
import com.lc.spring_mirai.annotation.RequestMapper
import net.mamoe.mirai.event.events.MemberJoinEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.at
import net.mamoe.mirai.message.uploadImage
import java.net.URL


@EventType(MemberJoinEvent::class)
@RequestMapper("")
class GroupMemberJoinController {
    @RequestMapper("")
    @NotEnd
    suspend fun onMemberJoin(event: MemberJoinEvent): MessageChain{
        val mcb = MessageChainBuilder()
        mcb.append("欢迎").append(event.member.at()).append("加群")
        mcb.append(event.group.uploadImage(URL(event.member.avatarUrl).openStream()))
        return mcb.asMessageChain()
    }
}