package com.lc.spring_mirai.request.mapping

import net.mamoe.mirai.message.code.CodableMessage
import net.mamoe.mirai.message.data.Message

/**
 * @Author 19525
 * @Date 2021/2/7 18:51
 */
open class MiraiCodeMappingItem(message: CodableMessage) : TextMappingItem(message.serializeToMiraiCode())