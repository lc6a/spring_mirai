package com.lc.spring_mirai.annotation

import com.lc.spring_mirai.controller.factory.MappedFactory
import com.lc.spring_mirai.controller.mapped.IMappedItem
import org.springframework.stereotype.Controller
import kotlin.reflect.KClass

/**
 * 控制器类或方法的路径映射
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Controller
annotation class RequestMapped(
    /**
     * 定义的路径字符串
     */
    val value: String = ""
)
