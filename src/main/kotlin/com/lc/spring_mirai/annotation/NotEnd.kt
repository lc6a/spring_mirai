package com.lc.spring_mirai.annotation


/**
 * 控制器方法默认是事件的终点，被该方法处理后该事件不会被其他方法处理(为了减少计算量，避免每次事件都遍历整个路径树)
 *
 * [NotEnd]表示一个控制器的方法不是某事件的处理终点
 * 此方法返回后此次事件会继续尝试匹配其他控制器方法
 */
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class NotEnd