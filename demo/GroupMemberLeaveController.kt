package com.lc.miraispring.controller

import com.lc.spring_mirai.annotation.EventType
import com.lc.spring_mirai.annotation.NotEnd
import com.lc.spring_mirai.annotation.RequestMapper
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.MemberLeaveEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.uploadImage
import java.net.URL


@EventType(MemberLeaveEvent::class)
@RequestMapper("")
class GroupMemberLeaveController{
    @RequestMapper("")
    @NotEnd
    suspend fun onMemberLeave(event: MemberLeaveEvent, argc: String): MessageChain{
        val mcb = MessageChainBuilder()
        mcb.append("${event.member.nameCardOrNick}退出群聊了")
        mcb.append(event.group.uploadImage(URL(event.member.avatarUrl).openStream()))
        return mcb.asMessageChain()
    }
}