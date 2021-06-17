package com.lc.spring_mirai.controller.filter

import com.lc.spring_mirai.annotation.AtMeFilter
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.At
import org.springframework.stereotype.Component

@Component
class AtMeFilterHandle: AnnotationFilterHandle<AtMeFilter>(AtMeFilter::class.java) {
    override fun filterByAnnotation(annotations: List<AtMeFilter>, data: FilterData): Boolean {
        val event = data.event
        if (event !is MessageEvent) return true
        return event.message.any { it is At && it.target == event.bot.id }
    }
}