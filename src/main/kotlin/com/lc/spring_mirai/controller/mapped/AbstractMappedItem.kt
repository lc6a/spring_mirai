package com.lc.spring_mirai.controller.mapped

/**
 * 默认必选无默认值的映射项
 */
abstract class AbstractMappedItem<T>: IMappedItem<T> {
    override var required: Boolean = true
    override var defaultValue: T? = null
}