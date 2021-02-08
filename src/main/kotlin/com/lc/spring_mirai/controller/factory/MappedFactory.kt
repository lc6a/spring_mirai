package com.lc.spring_mirai.controller.factory

import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.controller.ctrl.ControllerClass
import com.lc.spring_mirai.controller.mapped.*
import com.lc.spring_mirai.util.BeanSortUtil
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * @Author 19525
 * @Date 2021/2/3 22:46
 */
@Component("defaultMappedFactory")
class MappedFactory {

    /**
     * 根据字符串创建匹配项列表
     */
    fun createMapped(string: String, ctrl: ControllerClass, func: Func?): List<IMappedItem<*>> {
        val strList = string.split("/").filter { it.isNotBlank() }
        val iMappedItemList = mutableListOf<IMappedItem<*>>()
        for (i in strList.indices) {
            val item = createMappedItem(strList[i], i, ctrl, func)
            if (item != null)
                iMappedItemList.add(item)
        }
        return iMappedItemList
    }

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('beanSortUtil')}")
    private lateinit var beanSortUtil: BeanSortUtil

    private val mappedHandles by lazy { beanSortUtil.sortBeans(MappedItemHandle::class.java) }

    /**
     * @param string 该层路径
     * @param index 当前注解的第n层路径，从0开始 例如"abc"是注解内容"a/abc/c"的第1层
     * @param func 如果是方法注解，传入方法，否则传入null
     *
     */
    protected fun createMappedItem(string: String, index: Int, ctrl: ControllerClass, func: Func?): IMappedItem<*>? {
        val data = MappedItemHandleData(string, index, ctrl, func)
        for (handle in mappedHandles) {
            if (handle.accept(data))
                return handle.handle(data)
        }
        return null
    }
}