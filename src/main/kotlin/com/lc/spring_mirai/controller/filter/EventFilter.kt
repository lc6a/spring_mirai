package com.lc.spring_mirai.controller.filter

import net.mamoe.mirai.event.Event
import kotlin.reflect.KClass

annotation class EventFilter(
    val value: KClass<out Event> = Event::class
)
