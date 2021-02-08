package com.lc.spring_mirai.controller.parameter

import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.controller.mapped.IMappedItem
import kotlin.jvm.Throws
import kotlin.reflect.KParameter

/**
 * 绑定路径的参数
 * @param index 绑定路径的第[index]项，从0开始
 */
open class PathBindParam(
    open override val kParameter: KParameter,
    open override val func: Func,
    open val index: Int
) : Param(kParameter, func)