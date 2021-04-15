package com.lc.spring_mirai.web.filter

import com.lc.spring_mirai.annotation.Priority
import com.lc.spring_mirai.annotation.PriorityNum
import com.lc.spring_mirai.controller.filter.FilterData
import com.lc.spring_mirai.controller.filter.FilterHandle
import com.lc.spring_mirai.web.controller.bot.BaseBotController
import com.lc.spring_mirai.web.service.CtrlService
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component
@Priority(PriorityNum.HIGH)
class EnableFilterHandle : FilterHandle {

    @Resource
    private lateinit var ctrlService: CtrlService

    /**
     * 过滤器，返回是否通过要求
     * 如果是注解的过滤器，如果没有该注解，一般返回true
     */
    override suspend fun filter(data: FilterData): Boolean {
        val localCt = data.ctrl ?: return true
        if (localCt.ctrlObj !is BaseBotController) return true
        val ct = ctrlService.getCtrlStatus(localCt.beanName)
        return ct.ctrl.enable
    }
}