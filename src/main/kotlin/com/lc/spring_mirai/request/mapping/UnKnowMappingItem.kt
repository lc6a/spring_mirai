package com.lc.spring_mirai.request.mapping

/**
 * 未知映射项，其他映射项都不符合要求时使用
 * @Author 19525
 * @Date 2021/2/7 19:05
 */
open class UnKnowMappingItem(override val value: String) : TextMappingItem(value)