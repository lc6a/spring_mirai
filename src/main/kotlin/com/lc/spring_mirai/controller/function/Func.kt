package com.lc.spring_mirai.controller.function

import com.lc.spring_mirai.controller.ctrl.ControllerClass
import com.lc.spring_mirai.controller.mapped.IMappedAble
import com.lc.spring_mirai.controller.mapped.IMappedItem
import com.lc.spring_mirai.controller.parameter.Param
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.Listener
import kotlin.reflect.*
import kotlin.reflect.full.callSuspendBy


/**
 * 控制器方法
 */
open class Func(
    /**
     * 方法
     */
    open val kFunction: KFunction<*>,
    /**
     * 控制器对象
     */
    open val ctrl: ControllerClass
) : IMappedAble {

    override lateinit var mappedItems: List<IMappedItem<*>>

    /**
     * 参数列表
     */
    open lateinit var params: MutableList<Param>

    /**
     * 监听事件后的listener
     */
    open lateinit var listener: Listener<Event>

    /**
     * 调用此方法
     */
    suspend fun invoke(args: Map<KParameter, Any?>): Any? {
        return if (kFunction.isSuspend)
            kFunction.callSuspendBy(args)
        else
            kFunction.callBy(args)
    }

    open fun invokeNotSuspend(args: Map<KParameter, Any?>): Any? {
        return runBlocking {
            return@runBlocking invoke(args)
        }
    }

    /**
     * 调用此方法
     */
    suspend fun invoke(paramHandle: (Param) -> Any?): Any? {
        val map = mutableMapOf<KParameter, Any?>()
        for (p in params) {
            if (p.kParameter.index == 0 && p.clazz == ctrl.ctrlObj::class){
                map[p.kParameter] = ctrl.ctrlObj
            } else {
                map[p.kParameter] = paramHandle(p)
            }
        }
        return invoke(map)
    }

}

