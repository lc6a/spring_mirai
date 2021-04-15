package com.lc.spring_mirai.web.filter

import com.lc.spring_mirai.annotation.Priority
import com.lc.spring_mirai.annotation.PriorityNum
import com.lc.spring_mirai.annotation.Replace
import com.lc.spring_mirai.controller.filter.FilterData
import com.lc.spring_mirai.controller.filter.FilterHandle
import com.lc.spring_mirai.controller.filter.GroupFilterHandle
import com.lc.spring_mirai.web.controller.bot.BaseBotController
import com.lc.spring_mirai.web.service.CtrlService
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.FriendEvent
import net.mamoe.mirai.event.events.GroupEvent
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component
@Priority(PriorityNum.HIGH)
@Replace(GroupFilterHandle::class)      // 本过滤器基于数据库，取代了基于注解的过滤器
class IncludeExcludeFilterHandle: FilterHandle {

    @Resource
    private lateinit var ctrlService: CtrlService

    /**
     * 过滤器，返回是否通过要求
     * 如果是注解的过滤器，如果没有该注解，一般返回true
     */
    override suspend fun filter(data: FilterData): Boolean {
        val localCt = data.ctrl ?: return true
        if (localCt.ctrlObj !is BaseBotController) return true
        var ok = true
        val event = data.event
        if (event is BotEvent) {
            ok = ctrlService.botFilter(event.bot.id, localCt.beanName)
        }
        if (event is GroupEvent) {
            ok = ctrlService.groupFilter(event.group.id, localCt.beanName)
        }
        if (event is FriendEvent) {
            ok = ctrlService.friendFilter(event.friend.id, localCt.beanName)
        }
        return ok
    }
}