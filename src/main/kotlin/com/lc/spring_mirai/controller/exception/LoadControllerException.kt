package com.lc.spring_mirai.controller.exception

import com.lc.spring_mirai.exception.SpringMiraiException

/**
 * 加载控制器异常
 * @Author 19525
 * @Date 2021/2/4 23:31
 */
open class LoadControllerException(override val message: String?) : SpringMiraiException(message)