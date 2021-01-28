package com.lc.spring_mirai.controller

import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.controller.kclass.ControllerClass
import com.lc.spring_mirai.controller.kclass.IsNotControllerException
import com.lc.spring_mirai.controller.parameter.Param
import kotlin.jvm.Throws
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.full.functions

object ControllerFactory{
    /**
     * 获取方法的所有参数
     */
    fun createParams(func: KFunction<*>, ctrl: Any, config: ConfigOfController) : List<Param> {
        val list = mutableListOf<Param>()
        func.parameters.forEach {
            list.add(config.getParamInstance(it, ctrl))
        }
        return list
    }

    /**
     * 获取控制器类的所有控制器方法，非控制器方法不会添加
     */
    fun createFunctions(clazz: KClass<*>, ctrl: Any, config: ConfigOfController): List<Func> {
        val list = mutableListOf<Func>()
        clazz.functions.forEach{
            config.getFunctionInstance(it, ctrl, config)?.let { it1 -> list.add(it1) }
        }
        return list
    }

    /**
     * 创建一个控制器对象
     * @throws IsNotControllerException 如果对象不是控制器
     */
    @Throws(IsNotControllerException::class)
    fun createControllerClass(ctrl: Any, config: ConfigOfController): ControllerClass {
        return config.getControllerClassInstance(ctrl::class, ctrl, config)
            ?: throw IsNotControllerException("${ctrl::class}不是控制器")
    }
}
