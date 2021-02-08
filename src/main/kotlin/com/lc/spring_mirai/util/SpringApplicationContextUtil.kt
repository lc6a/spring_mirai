package com.lc.spring_mirai.util

import org.springframework.context.ApplicationContext

/**
 * 用于全局保存Spring的[ApplicationContext]
 */
object SpringApplicationContextUtil {
    lateinit var context : ApplicationContext
}