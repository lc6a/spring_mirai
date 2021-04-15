package com.lc.spring_mirai.util.beanSort

import com.lc.spring_mirai.annotation.Replace
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

/**
 * 排序bean的替换策略，默认基于[Replace]注解
 */
@Component("defaultBeanReplace")
class BeanReplace {
    fun replace(ctrl: Any): KClass<*>? {
        return AnnotationUtils.findAnnotation(ctrl::class.java, Replace::class.java)?.value
    }
}