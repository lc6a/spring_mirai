package com.lc.spring_mirai.controller.ctrl

import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.controller.mapped.IMappedAble
import com.lc.spring_mirai.controller.mapped.IMappedItem

/**
 * 控制器
 */
open class ControllerClass(
    /**
     * 控制器对象
     */
    open val ctrlObj: Any,
    /**
     * bean名称
     */
    open val beanName: String
) : IMappedAble {
    /**
     * 控制器类
     */
    open val clazz = ctrlObj::class

    override lateinit var mappedItems: List<IMappedItem<*>>
    open lateinit var functions: List<Func>

}