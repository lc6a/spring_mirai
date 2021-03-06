package com.lc.spring_mirai.controller.mapped

import com.lc.spring_mirai.request.mapping.IMappingItem

/**
 * 匹配完全相同的文本
 */
open class TextEqualMappedItem(open override val text: String) : TextMappedItem(text) {
    /**
     * 是否可以映射成功
     */
    override fun mapping(item: IMappingItem<out String>): Boolean {
        return item.value == text
    }
}