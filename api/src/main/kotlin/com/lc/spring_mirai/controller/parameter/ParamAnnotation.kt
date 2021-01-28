package com.lc.spring_mirai.controller.parameter

import kotlin.reflect.KClass

/**
 * 指明一个参数注解需要指定参数类(继承于[Param])来预处理控制器方法的参数，同一个参数只有一个这种注解起作用
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@MustBeDocumented
annotation class ParamAnnotation(
    val value: KClass<out Param> = Param::class
)
