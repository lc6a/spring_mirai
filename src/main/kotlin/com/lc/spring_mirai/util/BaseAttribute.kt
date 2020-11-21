package com.lc.spring_mirai.util

/**
 * 可以继承此类来附带数据
 */
open class BaseAttribute {
    protected open val data = mutableMapOf<String, Any>()

    open fun setAttribute(key: String, value: Any){
        data[key] = value
    }

    open fun getAttribute(key: String) = data[key]
}