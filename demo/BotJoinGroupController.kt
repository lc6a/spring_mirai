package com.lc.miraispring.controller

import com.lc.spring_mirai.annotation.EventType
import com.lc.spring_mirai.annotation.NotEnd
import com.lc.spring_mirai.annotation.RequestMapper
import net.mamoe.mirai.event.events.BotJoinGroupEvent
import org.springframework.beans.factory.annotation.Value

@EventType(BotJoinGroupEvent::class)
@RequestMapper("")
class BotJoinGroupController {

    @Value("${'$'}{botGroup}")
    private lateinit var botGroup: String

    @RequestMapper("")
    @NotEnd
    suspend fun onJoinGroup(event: BotJoinGroupEvent){
        event.group.sendMessage("大家好")
        event.group.botAsMember.nameCard = "Spring_Mirai,进群${botGroup}玩耍"
    }
}