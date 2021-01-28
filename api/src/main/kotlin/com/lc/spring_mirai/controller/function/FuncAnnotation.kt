package com.lc.spring_mirai.controller.function

import kotlin.reflect.KClass


/**
 * 指明一个方法注解需要指定方法类(继承于[Func])来预处理控制器方法，同一个方法只有一个这种注解起作用
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@MustBeDocumented
annotation class FuncAnnotation(
    val value: KClass<out Func> = Func::class
)
