package com.lc.spring_mirai.request.mapping

/**
 * 可前后迭代的迭代器
 */
interface MappingIterator: ListIterator<IMappingItem<*>> {

    var index: Int

    override fun nextIndex(): Int {
        return index;
    }

    override fun previousIndex(): Int {
        return index - 1;
    }

    override fun next(): IMappingItem<*>

}