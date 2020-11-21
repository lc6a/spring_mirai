package com.lc.spring_mirai.annotation

import org.springframework.stereotype.Controller

/**
 * 命令路径映射
 *
 * [value]格式：
 *      单层路径：XXX
 *      多层路径：XXX/XXX/XXX
 * 例如：
 *      @ComMapper("打招呼/说晚安")
 *
 * 注：
 *      可以设置[value]为空串""，这样表示0层路径，其节点直接被绑定到父节点
 *      若某控制器下既有0层路径的方法，又有非0层路径的方法，请将0层路径的方法添加[NotEnd]注解，
 *      到达此控制器的事件都将匹配0层路径的方法，若无[NotEnd]注解其他方法都收不到事件
 *
 *      控制器和方法都是0层路径时，这种特殊情况被称为“事件绑定”，此控制器和方法直接绑定到事件路径树的根节点
 *      凡是该事件都会被“事件绑定”方法匹配到，不会匹配内容（不然其路径为0难道还必须用户发送内容为空不成）
 *      “事件绑定”方法应该设置为[NotEnd]，避免某事件被“事件绑定“方法独占
 *
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Controller
@MustBeDocumented
annotation class RequestMapper(val value: String, val describe: String = "")