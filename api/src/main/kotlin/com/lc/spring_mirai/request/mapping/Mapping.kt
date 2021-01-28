package com.lc.spring_mirai.request.mapping


/**
 * 可前后迭代的映射类
 */
interface Mapping : Iterable<IMappingItem<*>> {
    override fun iterator(): MappingIterator
}

