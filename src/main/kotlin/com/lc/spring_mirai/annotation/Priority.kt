package com.lc.spring_mirai.annotation

/**
 * 定义组件的优先级，数字越小优先级越高
 * @see PriorityNum 几个优先级常量
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Priority(
    val value: Int = PriorityNum.DEFAULT
)

object PriorityNum {
    /**
     * 最高优先级，如果需要更高优先级可以用0甚至负数，但不推荐
     */
    const val HIGHEST = 1

    /**
     * 主要任务
     */
    const val HIGH = 100

    /**
     * 正常
     */
    const val NORMAL = 1000

    /**
     * 默认
     */
    const val DEFAULT = NORMAL

    /**
     * 低
     */
    const val LOW = 10000

    /**
     * 需要访问数据库的操作
     * 建议较低优先级
     */
    const val DB = LOW

    /**
     * 最低
     */
    const val LOWEST = 100000
}
