package com.lc.spring_mirai.util

object IdUtil {
    fun createInt(): Int {
        return System.currentTimeMillis().toInt()
    }
}