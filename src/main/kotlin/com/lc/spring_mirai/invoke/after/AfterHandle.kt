package com.lc.spring_mirai.invoke.after

import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.request.EventRequest

/**
 * 控制器方法调用后执行
 * @Author 19525
 * @Date 2021/2/8 23:36
 */
interface AfterHandle {
    fun after(data: AfterHandleData)
}

/**
 * 方法调用后数据
 */
data class AfterHandleData(
    /**
     * 请求
     */
    val request: EventRequest,
    /**
     * 执行的方法
     */
    val func: Func,
    /**
     * 返回值
     */
    val ret: Any?
)