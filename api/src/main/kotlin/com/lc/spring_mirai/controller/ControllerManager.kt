package com.lc.spring_mirai.controller

import com.lc.spring_mirai.controller.kclass.ControllerClass
import com.lc.spring_mirai.controller.kclass.IsNotControllerException
import kotlinx.coroutines.GlobalScope

abstract class ControllerManager(private val config: ConfigOfController) {
    private val controllers = mutableListOf<ControllerClass>()

    abstract fun loadController(obj: Any): Boolean

    abstract fun removeController(obj: Any): Boolean

    abstract fun loadAll()
}