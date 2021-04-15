package com.lc.spring_mirai.invoke.inject

import com.lc.spring_mirai.annotation.Priority
import com.lc.spring_mirai.annotation.PriorityNum
import com.lc.spring_mirai.invoke.inject.exception.ParamInjectException
import com.lc.spring_mirai.util.SpringApplicationContextUtil
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.message.data.*
import org.springframework.stereotype.Component
import kotlin.reflect.full.isSubclassOf

/**
 * SpringMirai框架中一些类型的注入
 * @Author 19525
 * @Date 2021/2/7 22:46
 */
@Component
@Priority(PriorityNum.LOWEST)
class SpringMiraiInject: ParamInject {

    /**
     * 是否接受注入该参数
     */
    override fun accept(data: ParamInjectData): Boolean {
        return try {
            inject(data)
            true
        } catch (e: Exception) {
            false
        }
    }


    override fun inject(data: ParamInjectData): Any? {
        val clazz = data.param.clazz
        // 1. 可以被Spring注入的
        try {
            return SpringApplicationContextUtil.context.getBean(data.param.kParameter.name!!, clazz.java)
        } catch (e: Exception){}
        try {
            return SpringApplicationContextUtil.context.getBean(clazz.java)
        } catch (e: Exception){}
        // 2. Mirai相关
        when {
            clazz.isSubclassOf(Event::class) -> {
                return data.event
            }
            clazz.isSubclassOf(MessageChain::class) -> {
                if (data.event !is MessageEvent) {
                    throw ParamInjectException("${data.event}不是消息事件")
                }
                return data.event.message
            }
            clazz.isSubclassOf(SingleMessage::class) -> {
                if (data.event !is MessageEvent) {
                    throw ParamInjectException("${data.event}不是消息事件")
                }
                return data.event.message.find { it::class == clazz }
            }
            clazz.isSubclassOf(Bot::class) -> {
                if (data.event !is BotEvent) {
                    throw ParamInjectException("${data.event}不是Bot事件")
                }
                return data.event.bot
            }
            clazz.isSubclassOf(Group::class) -> {
                if (data.event !is GroupEvent) {
                    throw ParamInjectException("${data.event}不是Group事件")
                }
                return data.event.group
            }
            clazz.isSubclassOf(Member::class) -> {
                if (data.event !is GroupOperableEvent) {
                    throw ParamInjectException("${data.event}不是GroupOperable事件")
                }
                return data.event.operatorOrBot
            }
            clazz.isSubclassOf(User::class) -> {
                if (data.event !is UserEvent) {
                    throw ParamInjectException("${data.event}不是User事件")
                }
                return data.event.user
            }
        }
        // 3. argc、args、argv等命名的String类型，表示映射后剩余的内容
        val argcNameList = listOf("argc", "args", "argv")
        if (clazz.isSubclassOf(String::class) && argcNameList.contains(data.param.kParameter.name)) {
            var n = data.funcMappingItems.size
            var content = (data.event as MessageEvent).message.content
            for (i in 0..n) {
                content = content.substringAfter(' ')
            }
            return content
        }
        // 4. 其他
        TODO()
    }
}