package com.lc.spring_mirai.web.service

import com.lc.spring_mirai.web.controller.bot.BaseBotController
import com.lc.spring_mirai.web.entity.CtrlExclude
import com.lc.spring_mirai.web.entity.CtrlInclude
import com.lc.spring_mirai.web.entity.CtrlStatus
import com.lc.spring_mirai.web.entity.IdTypes

interface CtrlService {
    fun getMap(ctrlMap: Map<String, BaseBotController>): Map<String, CtrlStatus>

    fun getCtrlStatus(ctrlName: String): CtrlStatus

    fun setEnable(enable: Boolean, ctrlName: String)

    fun setShowName(showName: String, ctrlName: String)

    fun setInclude(include: CtrlInclude)

    fun setExclude(exclude: CtrlExclude)

    fun commonFilter(id: Long, ctrlName: String, idTypes: IdTypes): Boolean

    fun botFilter(botId: Long, ctrlName: String) = commonFilter(botId, ctrlName, IdTypes.bot)

    fun groupFilter(groupId: Long, ctrlName: String) = commonFilter(groupId, ctrlName, IdTypes.group)

    fun friendFilter(friendId: Long, ctrlName: String) = commonFilter(friendId, ctrlName, IdTypes.friend)
}