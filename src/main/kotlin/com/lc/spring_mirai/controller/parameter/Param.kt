package com.lc.spring_mirai.controller.parameter

import com.lc.spring_mirai.controller.function.Func
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure


/**
 * 控制器方法的一个参数
 */
open class Param(
    /**
     * 参数本身
     */
    open val kParameter: KParameter,
    /**
     * 所属控制器方法
     */
    open val func: Func
){

    /**
     * 参数类型
     */
    val clazz by lazy { kParameter.type.jvmErasure }

}