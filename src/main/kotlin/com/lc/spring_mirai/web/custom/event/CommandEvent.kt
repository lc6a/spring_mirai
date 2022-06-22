package com.lc.spring_mirai.web.custom.event

import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.event.AbstractEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.messageChainOf

/**
 * 命令事件，通过Web等途径发送命令，而不是真正的消息
 */

abstract class CommandEvent(override val bot: Bot,
                            val command: String,
                            final override val sender: User
) : MessageEvent, AbstractEvent() {

    /**
     * 事件是否已被拦截.
     *
     * 所有事件都可以被拦截, 拦截后低优先级的监听器将不会处理到这个事件.
     *
     * @see intercept 拦截事件
     */
    override var isIntercepted: Boolean = false

    /**
     * 消息内容.
     *
     * 第一个元素一定为 [MessageSource], 存储此消息的发送人, 发送时间, 收信人, 消息 ids 等数据.
     * 随后的元素为拥有顺序的真实消息内容.
     */
    override val message: MessageChain = messageChainOf(PlainText(command))

    /**
     * 发送人名称
     */
    override val senderName: String = sender.nick

    /**
     * 拦截这个事件
     *
     * 当事件被 [拦截][Event.intercept] 后, 优先级较低 (靠右) 的监听器将不会被调用.
     *
     * 优先级为 [EventPriority.MONITOR] 的监听器不应该调用这个函数.
     *
     * @see EventPriority 查看优先级相关信息
     */
    override fun intercept() {
        this.isIntercepted = true
    }
}

////避免报错临时建的类
//abstract class CommandEvent(bot: Bot, command: String, sender: Contact) {
//    abstract val subject: Contact
//    abstract val time: Int
//    fun broadcast() {}
//}