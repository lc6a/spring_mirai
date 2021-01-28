package com.lc.spring_mirai.controller.parameter

import com.lc.spring_mirai.controller.mapped.IMappedItem
import kotlin.jvm.Throws
import kotlin.reflect.KParameter

/**
 * 绑定路径的参数
 * @param index 绑定路径的第[index]项，从0开始
 */
abstract class PathBindParam(
    open override val kParameter: KParameter,
    open override val ctrlObj: Any
) : Param(kParameter, ctrlObj) {

    /**
     * 绑定一个路径项
     */
    @Throws(ParamBindException::class)
    abstract fun bindMappedItem(items: List<IMappedItem<*>>): IMappedItem<*>
}