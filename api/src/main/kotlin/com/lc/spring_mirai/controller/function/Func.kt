package com.lc.spring_mirai.controller.function

import com.lc.spring_mirai.controller.kclass.IsNotControllerException
import com.lc.spring_mirai.controller.mapped.IMappedAble
import com.lc.spring_mirai.controller.parameter.Param
import kotlin.jvm.Throws
import kotlin.reflect.*
import kotlin.reflect.full.callSuspendBy


/**
 * 控制器方法
 */
abstract class Func(
    /**
     * 方法
     */
    open val kFunction: KFunction<*>,
    /**
     * 控制器对象
     */
    open val ctrlObj: Any
) : IMappedAble {

    abstract fun isControllerFunc(): Boolean

    @Throws(IsNotControllerException::class)
    fun requiredControllerFunc() {
        if (!isControllerFunc())
            throw IsNotControllerException("${kFunction}不是控制器方法")
    }

    /**
     * 参数列表
     */
    open lateinit var params: List<Param>

    /**
     * 调用此方法
     */
    suspend fun invoke(args: Map<KParameter, Any?>): Any? {
        return if (kFunction.isSuspend)
            kFunction.callSuspendBy(args)
        else
            kFunction.callBy(args)
    }

    /**
     * 调用此方法
     */
    suspend fun invoke(paramHandle: (Param) -> Any?): Any? {
        val map = mutableMapOf<KParameter, Any?>()
        for (p in params) {
            if (p.kParameter.index == 0 && p.clazz == ctrlObj::class){
                map[p.kParameter] = ctrlObj
            } else {
                map[p.kParameter] = paramHandle(p)
            }
        }
        return invoke(map)
    }

}

