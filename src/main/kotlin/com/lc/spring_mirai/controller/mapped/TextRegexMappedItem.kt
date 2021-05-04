package com.lc.spring_mirai.controller.mapped

import com.lc.spring_mirai.request.mapping.IMappingItem

/**
 * 正则表达式映射
 */
open class TextRegexMappedItem(text: kotlin.String) : TextMappedItem(text) {

    open val regex = text.toRegex()

    /**
     * 是否可以映射成功
     */
    override fun mapping(item: IMappingItem<out String>): Boolean {
        return item.value.matches(regex)
    }

    override fun toString(): String {
        return "[$regex]"
    }
}