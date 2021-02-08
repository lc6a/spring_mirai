package com.lc.spring_mirai.controller.mapped

import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.controller.ctrl.ControllerClass

/**
 * 指定一层路径字符串的一种解析方式
 * 将字符串解析成指定类型的IMappedItem
 * @Author 19525
 * @Date 2021/2/7 16:04
 */
interface MappedItemHandle {

    /**
     * 路径字符串mappedStr是否符合本handle的要求
     */
    fun accept(data: MappedItemHandleData): Boolean

    /**
     * 将mappedStr解析成指定IMappedItem
     * 此mappedStr一定是accept为true的
     */
    fun handle(data: MappedItemHandleData): IMappedItem<*>
}

/**
 * @param mappedStr 路径字符串(一层)
 * @param index 路径字符串在注解中的层数，从0开始
 */
data class MappedItemHandleData(
    val mappedStr: String,
    val index: Int,
    val ctrl: ControllerClass,
    val func: Func?
)