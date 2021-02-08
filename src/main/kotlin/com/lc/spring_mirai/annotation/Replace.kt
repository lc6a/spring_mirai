package com.lc.spring_mirai.annotation

import kotlin.reflect.KClass

/**
 * 对于需要排序的bean(传给beanSortUtil的参数)
 * 用此注解来替换某个bean
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Replace(
    /**
     * 被替换的bean的类
     */
    val value: KClass<*>
)
