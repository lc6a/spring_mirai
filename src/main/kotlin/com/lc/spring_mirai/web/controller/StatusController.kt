package com.lc.spring_mirai.web.controller

import net.mamoe.mirai.Bot
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("status")
class StatusController {

    @RequestMapping("all")
    fun all(): Map<Long, String> {
        val status = mutableMapOf<Long, String>()
        Bot.instances.forEach {
            if (it.isOnline) {
                status[it.id] = "online"
            } else {
                status[it.id] = "offline"
            }
        }
        return status
    }
}