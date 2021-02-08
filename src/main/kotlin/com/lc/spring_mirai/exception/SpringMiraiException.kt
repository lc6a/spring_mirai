package com.lc.spring_mirai.exception

import kotlin.Exception

/**
 * 本库自定义异常的基类
 * @Author 19525
 * @Date 2021/2/4 23:31
 */
open class SpringMiraiException(override val message: String?) : Exception(message)