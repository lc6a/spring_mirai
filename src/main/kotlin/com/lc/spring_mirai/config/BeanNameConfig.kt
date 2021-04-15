package com.lc.spring_mirai.config

import com.lc.spring_mirai.util.beanSort.BeanSortUtil
import com.lc.spring_mirai.util.SpringApplicationContextUtil
import kotlinx.atomicfu.locks.synchronized
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * SpringMirai所有Bean的名字
 * 默认值为SpringMirai默认实现
 * 可以在application.yml里面提供自己实现的bean名称
 *
 * 此类的属性名一般是bean类首字母变小写，默认值是bean类名前加default
 */
@Component("defaultBeanNameConfig")
@ConfigurationProperties(prefix = "spring-mirai.bean-name")
class BeanNameConfig: HashMap<String, String>() {

    protected var filtered = false

    protected fun filter() {
        val sortUtil = SpringApplicationContextUtil.context.getBean(
            getBeanNameByKey("beanSortUtil"), BeanSortUtil::class.java)
        val filterList = sortUtil.sortBeans(BeanNameConfigHandle::class.java)
        for (filter in filterList) {
            filter.handle(this)
        }
    }

    protected fun getBeanNameByKey(key: String): String {
        return this[key] ?: "default${key[0].toUpperCase()}${key.substring(1)}"
    }

    /**
     * 如果配置文件有，则返回配置文件的值
     * 如果没有，则是default+类名
     */
    fun getBeanName(key: String): String {
        synchronized(this.filtered) {
            if (!filtered) {
                filter()
                filtered = true
            }
        }
        return getBeanNameByKey(key)
    }
}

