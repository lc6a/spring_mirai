package com.lc.spring_mirai.controller.kclass

import com.lc.spring_mirai.controller.ControllerFactory
import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.controller.mapped.IMappedAble
import com.lc.spring_mirai.controller.mapped.IMappedItem
import kotlin.reflect.KClass
import kotlin.reflect.full.functions

/**
 * 控制器
 */
abstract class ControllerClass(
    /**
     * 控制器类
     */
    open val clazz: KClass<*>,
    /**
     * 控制器对象
     */
    open val ctrlObj: Any
) : IMappedAble {

    open lateinit var functions: List<Func>

    /**
     * 判断其是否是控制器
     */
    abstract fun isController(): Boolean

    /**
     * 需要此类为控制器，否则抛出异常
     */
    @Throws(IsNotControllerException::class)
    fun requiredController() {
        if (!isController())
            throw IsNotControllerException("${clazz}不是控制器")
    }

}