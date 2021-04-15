package com.lc.spring_mirai.util.beanSort

import com.lc.spring_mirai.annotation.PriorityNum
import com.lc.spring_mirai.annotation.Priority
import com.lc.spring_mirai.annotation.Replace
import com.lc.spring_mirai.util.SpringApplicationContextUtil
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
import javax.annotation.Resource
import kotlin.reflect.KClass

@Component("defaultBeanSortUtil")
class BeanSortUtil {

    // @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('beanReplace')}") // 会造成循环依赖
    @Resource
    private lateinit var beanReplace: BeanReplace

    /**
     * 获取所有该类型的bean并进行排序
     * 这里根据[Priority]注解获取优先级
     * 缺少注解默认[PriorityNum.DEFAULT]优先级
     */
    fun<T> sortBeans(clazz: Class<T>): List<T> {
        val beans = SpringApplicationContextUtil.context.getBeansOfType(clazz).values.toMutableList()
        val removes = mutableListOf<KClass<*>>()
        for (bean in beans) {
            val replace = beanReplace.replace(bean!!)
            if (replace != null) {
                removes.add(replace)
            }
        }
        beans.removeIf { removes.contains(it!!::class) }
        beans.sortBy {AnnotationUtils.findAnnotation(it!!::class.java, Priority::class.java)
            ?.value ?: PriorityNum.DEFAULT}
        return beans
    }
}