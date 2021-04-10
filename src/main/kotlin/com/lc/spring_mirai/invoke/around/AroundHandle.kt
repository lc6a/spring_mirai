package com.lc.spring_mirai.invoke.around

import com.lc.spring_mirai.invoke.after.AfterHandleData
import com.lc.spring_mirai.invoke.before.BeforeHandleData

interface AroundHandle {
    fun before(data: BeforeHandleData)
    fun after(data: AfterHandleData)
}