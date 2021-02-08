package com.lc.spring_mirai.util

import com.lc.spring_mirai.annotation.PriorityNum
import com.lc.spring_mirai.annotation.Priority
import com.lc.spring_mirai.annotation.Replace
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component

@Component("defaultBeanSortUtil")
class BeanSortUtil {
    /**
     * 获取所有该类型的bean并进行排序
     * 这里根据[Priority]注解获取优先级
     * 缺少注解默认[PriorityNum.DEFAULT]优先级
     */
    fun<T> sortBeans(clazz: Class<T>): List<T> {
        val beans = SpringApplicationContextUtil.context.getBeansOfType(clazz).values.toMutableList()
        for (bean in beans) {
            val replace = AnnotationUtils.findAnnotation(bean!!::class.java, Replace::class.java)
            if (replace != null) {
                beans.removeIf { it!!::class == replace.value }
            }
        }
        beans.sortBy {AnnotationUtils.findAnnotation(it!!::class.java, Priority::class.java)
            ?.value ?: PriorityNum.DEFAULT}
        return beans
    }
}