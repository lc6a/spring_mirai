package com.lc.spring_mirai.controller.mapped

import com.lc.spring_mirai.request.mapping.IMappingItem

/**
 * 通用映射项
 * @param data 附加数据：[MappedItemJson.data]
 */
abstract class UniversalMappedItem<T>(data: String?) : AbstractMappedItem<T>()