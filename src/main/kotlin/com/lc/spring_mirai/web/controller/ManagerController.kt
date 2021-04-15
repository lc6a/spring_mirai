package com.lc.spring_mirai.web.controller

import com.lc.spring_mirai.util.JsonUtil
import com.lc.spring_mirai.util.SpringApplicationContextUtil
import com.lc.spring_mirai.web.controller.bot.BaseBotController
import com.lc.spring_mirai.web.entity.CtrlExclude
import com.lc.spring_mirai.web.entity.CtrlInclude
import com.lc.spring_mirai.web.service.CtrlService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource

@RestController
@RequestMapping("manager")
class ManagerController {

    @Resource
    private lateinit var jsonUtil: JsonUtil

    @Resource
    private lateinit var ctrlService: CtrlService


    @RequestMapping("main")
    fun main(): String {
        return jsonUtil.toJson(ctrlService.getMap(SpringApplicationContextUtil.context.getBeansOfType(BaseBotController::class.java)))
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