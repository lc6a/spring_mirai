package com.lc.spring_mirai.invoke.before

import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.request.EventRequest
import kotlin.reflect.KParameter

/**
 * @Author 19525
 * @Date 2021/2/14 15:26
 */
interface BeforeHandle {
    fun before(data: BeforeHandleData)
}

data class BeforeHandleData(
    val request: EventRequest,
    val func: Func,
    val paramArgs: Map<KParameter, Any?>
)