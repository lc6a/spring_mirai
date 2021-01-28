package com.lc.spring_mirai.controller.mapped

import com.lc.spring_mirai.request.mapping.IMappingItem

/**
 * 映射文本
 */
abstract class TextMapped(
    open val text: String,
) : AbstractMappedItem<String>()