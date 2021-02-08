package com.lc.spring_mirai.application

import com.lc.spring_mirai.util.SpringApplicationContextUtil
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * @Author 19525
 * @Date 2021/2/8 22:20
 */
@Component
class StartSpringMirai: ApplicationListener<ApplicationStartedEvent> {

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('applicationHandle')}")
    protected lateinit var applicationHandle: ApplicationHandle

    /**
     * Handle an application event.
     * @param event the event to respond to
     */
    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        SpringApplicationContextUtil.context = event.applicationContext
        applicationHandle.runApplication()
    }
}