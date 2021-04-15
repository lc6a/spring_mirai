package com.lc.spring_mirai.web.controller.bot

import com.lc.spring_mirai.web.service.DataService
import kotlin.reflect.KClass

/**
 *
 */
abstract class BaseBotController {

    open var enable: Boolean = true
    abstract var showName: String

}