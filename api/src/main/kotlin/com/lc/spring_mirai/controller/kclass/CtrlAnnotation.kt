package com.lc.spring_mirai.controller.kclass

import kotlin.reflect.KClass

/**
 * 指明一个控制器注解需要指定控制器类(继承于[ControllerClass])来预处理控制器，同一个控制器只有一个这种注解起作用
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@MustBeDocumented
annotation class CtrlAnnotation(
    val value: KClass<out ControllerClass> = ControllerClass::class
)