package com.lc.spring_mirai.logger

import com.lc.spring_mirai.util.logFilter
import net.mamoe.mirai.utils.PlatformLogger

/**
 * 一个简单的jvm控制台日志类，过滤了心跳包等可以忽略的日志
 */
class SimpleJvmConsoleLogger(
        override val identity: String? = "Spring_Mirai",
        override val output: (String) -> Unit = { logFilter(it) /*过滤不必要的日志 */},
        private val colored: Boolean = false
) : PlatformLogger(identity, output, colored)