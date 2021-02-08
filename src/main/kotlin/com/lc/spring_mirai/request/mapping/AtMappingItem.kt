package com.lc.spring_mirai.request.mapping

import net.mamoe.mirai.message.data.At

/**
 * At映射项
 * @Author 19525
 * @Date 2021/2/7 18:45
 */
open class AtMappingItem(open val at: At) : TextMappingItem(at.target.toString())