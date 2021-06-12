package com.lc.spring_mirai.web.custom.filter

import com.lc.spring_mirai.annotation.Priority
import com.lc.spring_mirai.annotation.PriorityNum
import com.lc.spring_mirai.annotation.Replace
import com.lc.spring_mirai.controller.filter.FilterData
import com.lc.spring_mirai.controller.filter.FilterHandle
import com.lc.spring_mirai.controller.filter.GroupFilterHandle
import com.lc.spring_mirai.web.service.CtrlService
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component
@Priority(PriorityNum.DEFAULT)
@Replace(GroupFilterHandle::class)      // 本过滤器基于数据库，取代了基于注解的过滤器
class IncludeExcludeFilterHandle: FilterHandle {

    @Resource
    private lateinit var ctrlService: CtrlService

    /**
     * 过滤器，返回是否通过要求
     */
    override suspend fun filter(data: FilterData): Boolean {
//        val localCt = data.ctrl ?: return true
//        if (localCt.ctrlObj !is BaseBotController) return true
//        val event = data.event
//        if (event is BotEvent) {
//            if (!ctrlService.botFilter(event.bot.id, localCt.beanName)) return false
//        }
//        if (event is GroupEvent) {
//            if (!ctrlService.groupFilter(event.group.id, localCt.beanName)) return false
//        }
//        if (event is FriendEvent) {
//            if (!ctrlService.friendFilter(event.friend.id, localCt.beanName)) return false
//        }
        return true
    }
}