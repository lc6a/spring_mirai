package com.lc.spring_mirai.annotation

/**
 * 表示某控制器或者方法需要某种权限才能允许使用
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class PermissionFilter (
        /**
         * 权限的唯一标识，请确保跟其他权限不重复
         */
        val value: String
)