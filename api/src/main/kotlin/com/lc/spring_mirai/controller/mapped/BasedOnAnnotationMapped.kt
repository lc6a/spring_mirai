package com.lc.spring_mirai.controller.mapped

import com.lc.spring_mirai.controller.annotation.Mapping
import com.lc.spring_mirai.controller.function.Func

object BasedOnAnnotationMapped {
    /**
     * 获取控制器类或者方法的映射路径项
     * @param annotation [Mapping]注解
     * @param func 控制器方法，如果是控制器类传入null
     */
    fun getMappedItems(annotation: Mapping, func: Func?): List<IMappedItem<*>> {
        val pathItems = getStringItems(annotation.value)
        val items = mutableListOf<IMappedItem<*>>()
        pathItems.forEach{ items.add(DefaultMappedItemFactory.getMappedItem(it, func))}
        return items
    }

    /**
     * 分割路径，默认按['/']分割
     */
    private fun getStringItems(path: String): List<String> {
        return path.split('/').filter { it.isNotBlank()}
    }
}