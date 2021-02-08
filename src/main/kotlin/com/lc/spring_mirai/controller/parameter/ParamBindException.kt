package com.lc.spring_mirai.controller.parameter

import java.lang.Exception

/**
 * 参数绑定异常
 */
class ParamBindException(override val message: String?) : Exception(message)