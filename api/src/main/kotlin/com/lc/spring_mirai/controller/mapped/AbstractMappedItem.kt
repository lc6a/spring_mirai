package com.lc.spring_mirai.controller.mapped

/**
 * 默认必选无默认值的映射项
 */
abstract class AbstractMappedItem<T>: IMappedItem<T> {
    open override var required: Boolean = true
    open override var defaultValue: T? = null
}