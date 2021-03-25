package com.lc.spring_mirai.exception

import java.lang.RuntimeException

/**
 * 权限不足
 */
class PermissionDeniedException(override val message: String?) : SpringMiraiException(message)