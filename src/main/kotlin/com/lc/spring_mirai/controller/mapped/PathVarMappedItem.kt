package com.lc.spring_mirai.controller.mapped

import com.lc.spring_mirai.request.mapping.IMappingItem
import kotlin.reflect.KClass

/**
 * 路径变量映射
 * @param clazz 变量类型
 * @param text 路径变量名
 */
open class PathVarMappedItem(
    override val text: String,
    open val clazz: KClass<*>
) : TextMappedItem(text) {
    /**
     * 是否可以映射成功
     */
    override fun mapping(item: IMappingItem<out String>): Boolean {
        return true
    }

    override fun toString(): String {
        return "{$text}"
    }
}