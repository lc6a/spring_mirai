package com.lc.spring_mirai.config

/**
 * 可以通过代码设置bean名称
 * @Author 19525
 * @Date 2021/2/14 16:55
 */
interface BeanNameConfigHandle {
    /**
     * 可以在这里修改beanNameConfig.get(key)的value
     */
    fun handle(beanNameConfig: BeanNameConfig)
}