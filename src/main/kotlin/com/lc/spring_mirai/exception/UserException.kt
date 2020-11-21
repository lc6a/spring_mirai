package com.lc.spring_mirai.exception

import java.lang.RuntimeException

/**
 * 用户操作不对
 */
class UserException(override val message: String?) : RuntimeException(message)