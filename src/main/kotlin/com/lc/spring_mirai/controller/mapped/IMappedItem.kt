package com.lc.spring_mirai.controller.mapped

import com.lc.spring_mirai.request.mapping.IMappingItem
import kotlin.reflect.KType

/**
 * 一个可以从[IMappingItem]映射的项，
 * [T]为映射的类型
 * 被映射指的是代码中的待映射路径
 * 主动映射方则是发送过来的消息
 */
interface IMappedItem<T> {

    /**
     * 此映射项是否是必须的
     */
    val required: Boolean

    /**
     * 映射失败后的默认值，
     * 必须设置[require]为false才生效
     */
    val defaultValue: T?

    /**
     * 是否可以映射成功，只需要考虑[required]为true的情况
     */
    fun mapping(item: IMappingItem<out T>): Boolean

    override fun toString(): String
}