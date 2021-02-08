package com.lc.spring_mirai.controller.filter

import com.lc.spring_mirai.controller.ctrl.ControllerClass
import com.lc.spring_mirai.controller.function.Func
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.EventChannel

/**
 * 过滤器，用于注册事件监听时的事件通道过滤器
 * 一般根据注解来过滤，如果有需求也可根据类或方法本身来过滤
 * 内置注解都有内置过滤器
 * @Author 19525
 * @Date 2021/2/8 21:19
 */
interface FilterHandle {
    /**
     * 过滤器，返回是否通过要求
     * 如果是注解的过滤器，如果没有该注解，一般返回true
     */
    suspend fun filter(data: FilterData): Boolean
}

/**
 * 过滤器数据
 */
data class FilterData(
    /**
     * 事件本身
     */
    val event: Event,
    /**
     * 控制器类或者方法上面的注解，一般可以统一处理
     */
    val annotations: List<Annotation>,
    /**
     * 控制器类，如果当前不是处理到类，为null
     * 如果需要获取注解请用[annotations]属性
     */
    val ctrl: ControllerClass?,
    /**
     * 控制器方法，如果当前不是处理到方法，为null
     * 如果需要获取注解请用[annotations]属性
     */
    val func: Func?
)