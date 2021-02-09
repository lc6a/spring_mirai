package com.lc.spring_mirai.application

import com.lc.spring_mirai.controller.manager.CtrlManager
import net.mamoe.mirai.BotFactory
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * 管理spring_mirai生命周期
 */
@Component("defaultApplicationHandle")
class ApplicationHandle {

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('ctrlManager')}")
    protected lateinit var ctrlManager: CtrlManager

    /**
     * 运行spring_mirai
     */
    fun runApplication() {
        ctrlManager.loadAllCtrl()
        // 后期有能力可能加上jar热加载
    }
}