package com.lc.spring_mirai.controller.kclass

/**
 * 当被调用者不是控制器时抛出异常
 */
class IsNotControllerException(override val message: String?): Exception(message)