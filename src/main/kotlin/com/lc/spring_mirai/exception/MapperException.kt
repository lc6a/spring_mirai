package com.lc.spring_mirai.exception

import java.lang.RuntimeException


class MapperException (override val message: String?) : RuntimeException(message)