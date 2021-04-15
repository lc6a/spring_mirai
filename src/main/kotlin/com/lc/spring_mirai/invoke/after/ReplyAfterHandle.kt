package com.lc.spring_mirai.invoke.after

import com.lc.spring_mirai.annotation.Priority
import com.lc.spring_mirai.annotation.PriorityNum
import com.lc.spring_mirai.util.ReplyUtil
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.*
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * 将方法返回值作为消息发送回去
 * @Author 19525
 * @Date 2021/2/8 23:39
 */
@Component
@Priority(PriorityNum.HIGHEST)
class ReplyAfterHandle: AfterHandle {

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('replyUtil')}")
    protected lateinit var replyUtil: ReplyUtil

    override fun after(data: AfterHandleData) {
        val event = data.request.event
        val ret = data.ret
        if (ret == Unit || ret == null) return
        if (event !is MessageEvent) return
        val message: Message = when (ret) {
            is String -> PlainText(ret)
            is Message -> ret
            else -> ret as Message
        }
        if (message.isContentEmpty()) {
            return
        }
        runBlocking {
            replyUtil.reply(event, message)
        }

    }

}