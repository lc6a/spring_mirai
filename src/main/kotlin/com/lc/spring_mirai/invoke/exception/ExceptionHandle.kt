package com.lc.spring_mirai.invoke.exception

import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.request.EventRequest
import com.lc.spring_mirai.request.Request

interface ExceptionHandle {
    fun exception(data: ExceptionHandleData)
}

data class ExceptionHandleData(
    val exception: Exception,
    val request: EventRequest,
    val func: Func
)