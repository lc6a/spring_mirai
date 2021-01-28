package com.lc.spring_mirai.controller.annotation

import com.lc.spring_mirai.controller.mapped.IMappedItem

/**
 * 控制器类或方法的路径映射
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Mapping(
    val value: String = ""
)
