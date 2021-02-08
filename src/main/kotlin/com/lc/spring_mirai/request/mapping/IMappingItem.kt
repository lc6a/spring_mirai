package com.lc.spring_mirai.request.mapping
import com.lc.spring_mirai.controller.mapped.IMappedItem

/**
 * 一个可以映射到[IMappedItem]的项
 */
interface IMappingItem<T> {
    /**
     * 实际内容
     */
    val value : T
}