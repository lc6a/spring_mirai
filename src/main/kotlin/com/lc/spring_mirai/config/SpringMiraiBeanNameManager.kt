package com.lc.spring_mirai.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * spring_mirai核心bean的名称管理类
 * 此类不能被自定义，固定为这种实现
 */
@Component("springMiraiBeanNameManager")
open class SpringMiraiBeanNameManager {
    /**
     * spring_mirai所有可自定义bean的名称配置类
     * 可以继承BeanNameConfig并从配置文件指定自定义bean名称配置(主要用于扩展)
     */
    @Resource(name = "${'$'}{spring-mirai.name-config:defaultBeanNameConfig}")
    lateinit var beanNameConfig: BeanNameConfig

}