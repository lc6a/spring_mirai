package com.lc.spring_mirai.request

import com.lc.spring_mirai.request.mapping.IMappingItem
import com.lc.spring_mirai.request.mapping.MappingList

/**
 * 消息请求
 * @Author 19525
 * @Date 2021/2/5 0:12
 */
interface Request {
    /**
     * 匹配项
     */
    val mappingItems : List<IMappingItem<*>>
}