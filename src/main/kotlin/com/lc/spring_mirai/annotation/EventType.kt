package com.lc.spring_mirai.annotation

import net.mamoe.mirai.event.Event
import org.springframework.stereotype.Controller
import kotlin.reflect.KClass

/**
 * 事件类型，可以设置只接受类型为[clazz]的事件
 */
@Target(AnnotationTarget.CLASS)
@Controller
@MustBeDocumented
annotation class EventType(val clazz: KClass<out Event>)