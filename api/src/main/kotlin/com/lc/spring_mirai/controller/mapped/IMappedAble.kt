package com.lc.spring_mirai.controller.mapped

import com.lc.spring_mirai.controller.kclass.IsNotControllerException
import com.lc.spring_mirai.request.mapping.IMappingItem
import com.lc.spring_mirai.request.mapping.MappingIterator

/**
 * 可由[MappingIterator]映射
 */
interface IMappedAble {
    /**
     * 返回是否映射成功，需要先保证[mappedItems]不为[null]
     */
    fun mapping(iterator: MappingIterator): Boolean {
        for (item in mappedItems!!) {
            if (iterator.hasNext()) {
                if (!item.mapping(iterator.next() as IMappingItem<Nothing>)) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * 获取其[IMappedItem]列表，如果0层路径返回[emptyList]
     */
    val mappedItems: List<IMappedItem<*>>
}