package com.lc.spring_mirai.invoke.before

import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.request.EventRequest
import org.springframework.stereotype.Component
import kotlin.reflect.KParameter

/**
 * 控制器方法处理前调用
 * @Author 19525
 * @Date 2021/2/7 21:04
 */
@Component("defaultBeforeManager")
class BeforeManager {
    fun before(request: EventRequest, func: Func, args: Map<KParameter, Any?>) {
        // todo
    }
}