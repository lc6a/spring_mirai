package com.lc.spring_mirai.controller.parameter

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
     * 所属控制器对象
     */
    open val ctrlObj: Any
){

    /**
     * 参数类型
     */
    val clazz = kParameter.type.jvmErasure



//    /**
//     * 参数将会赋的值
//     */
//    var value: Any? = null
}