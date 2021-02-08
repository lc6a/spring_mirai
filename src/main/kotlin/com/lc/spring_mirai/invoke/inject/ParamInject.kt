package com.lc.spring_mirai.invoke.inject

import com.lc.spring_mirai.controller.parameter.Param
import com.lc.spring_mirai.request.mapping.IMappingItem
import net.mamoe.mirai.event.Event

/**
 * 参数注入的一个类型
 * @Author 19525
 * @Date 2021/2/7 21:39
 */
interface ParamInject {
    /**
     * 是否接受注入该参数
     */
    fun accept(data: ParamInjectData): Boolean

    fun inject(data: ParamInjectData): Any?
}

/**
 *
 * @param funcMappingItems 方法路径映射列表
 * @param param 参数
 */
data class ParamInjectData(
    val funcMappingItems: List<IMappingItem<*>>,
    val param: Param,
    val event: Event
)