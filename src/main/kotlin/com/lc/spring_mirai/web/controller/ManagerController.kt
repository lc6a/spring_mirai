package com.lc.spring_mirai.web.controller

import com.lc.spring_mirai.util.JsonUtil
import com.lc.spring_mirai.util.SpringApplicationContextUtil
import com.lc.spring_mirai.web.controller.bot.BaseBotController
import com.lc.spring_mirai.web.entity.*
import com.lc.spring_mirai.web.service.CtrlService
import org.springframework.web.bind.annotation.*
import javax.annotation.Resource
import kotlin.math.max
import kotlin.math.min

@RestController
@RequestMapping("manager")
@CrossOrigin
class ManagerController {

    @Resource
    private lateinit var jsonUtil: JsonUtil

    @Resource
    private lateinit var ctrlService: CtrlService


    @RequestMapping("main")
    fun main(@ModelAttribute layuiPage: LayuiPage): String {
        val map = ctrlService.getMap(SpringApplicationContextUtil.context.getBeansOfType(BaseBotController::class.java))
        val fromIndex = max(0, (layuiPage.page - 1) * layuiPage.limit)
        val toIndex = min(layuiPage.page * layuiPage.limit, map.size)
        val oldData  = map.values.toList().subList(fromIndex, toIndex)
        val data = mutableListOf<LayuiCtrlStatus>()
        oldData.forEach {
            data.add(LayuiCtrlStatus.fromCtrlStatus(it))
        }
        val table = LayuiTable(0, "", map.size, data)
        return jsonUtil.toJson(table)
    }

    @RequestMapping("setEnable")
    fun setEnable(@RequestParam enable: Boolean, @RequestParam ctrlName: String) {
        ctrlService.setEnable(enable, ctrlName)
    }

    @RequestMapping("setShowName")
    fun setStatus(@RequestParam showName: String, @RequestParam ctrlName: String) {
        ctrlService.setShowName(showName, ctrlName)
    }

    @RequestMapping("setInclude")
    fun setInclude(@RequestParam include: CtrlInclude) {
        ctrlService.setInclude(include)
    }

    @RequestMapping("setExclude")
    fun setExclude(@RequestParam exclude: CtrlExclude) {
        ctrlService.setExclude(exclude)
    }
}