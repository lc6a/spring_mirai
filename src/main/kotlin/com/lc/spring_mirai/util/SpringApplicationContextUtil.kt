package com.lc.spring_mirai.util

import org.springframework.boot.context.event.ApplicationContextInitializedEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

/**
 * 用于全局保存Spring的[ApplicationContext]
 */
object SpringApplicationContextUtil{
    lateinit var context: ApplicationContext
}

class SpringContextInit: ApplicationListener<ApplicationContextInitializedEvent> {
    /**
     * Handle an application event.
     * @param event the event to respond to
     */
    override fun onApplicationEvent(event: ApplicationContextInitializedEvent) {
        SpringApplicationContextUtil.context = event.applicationContext
    }

}