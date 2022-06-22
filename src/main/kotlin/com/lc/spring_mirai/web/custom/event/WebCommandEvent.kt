package com.lc.spring_mirai.web.custom.event

import com.lc.spring_mirai.web.controller.WebCommandSender
import kotlinx.coroutines.SupervisorJob
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.action.UserNudge
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.OnlineMessageSource
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import net.mamoe.mirai.utils.MiraiInternalApi
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * 通过Web发送命令，不产生虚拟身份
 */
class WebCommandEvent(bot: Bot, command: String, override val time: Int, private val replyFn: (String) -> Unit)
    : CommandEvent(bot, command, NoUser(replyFn)) {
    /**
     * 消息事件主体.
     *
     * - 对于好友消息, 这个属性为 [Friend] 的实例, 与 [sender] 引用相同;
     * - 对于临时会话消息, 这个属性为 [Member] 的实例, 与 [sender] 引用相同;
     * - 对于陌生人消息, 这个属性为 [Stranger] 的实例, 与 [sender] 引用相同
     * - 对于群消息, 这个属性为 [Group] 的实例, 与 [GroupMessageEvent.group] 引用相同
     * - 对于其他客户端消息, 这个属性为 [OtherClient] 的实例, 与 [OtherClientMessageEvent.client] 引用相同
     *
     * 在回复消息时, 可通过 [subject] 作为回复对象
     */
    override val subject: Contact
        get() = NoUser(replyFn, this)

    fun sendWebMessage(message: String) {
        replyFn(message)
    }

    fun toCommandSender(): CommandSender {
        return WebCommandSender(replyFn)
    }

}

class NoUser(val replyFn: (String) -> Unit, val event: WebCommandEvent? = null): User{
    /**
     * 这个联系对象所属 [Bot].
     */
    override val bot: Bot
        get() = TODO("Not yet implemented")

    /**
     * The context of this scope.
     * Context is encapsulated by the scope and used for implementation of coroutine builders that are extensions on the scope.
     * Accessing this property in general code is not recommended for any purposes except accessing the [Job] instance for advanced usages.
     *
     * By convention, should contain an instance of a [job][Job] to enforce structured concurrency.
     */
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob()

    /**
     * QQ 号码
     */
    override val id: Long
        get() = -1L

    /**
     * 昵称
     */
    override val nick: String
        get() = "Web"

    /**
     * 备注信息
     *
     * 仅 [Bot] 与 [User] 存在好友关系的时候才可能存在备注
     *
     * [Bot] 与 [User] 没有好友关系时永远为空[字符串][String] ("")
     *
     * @see [User.remarkOrNick]
     */
    override val remark: String
        get() = "Web"

    /**
     * 创建一个 "戳一戳" 消息
     *
     * @see Nudge.sendTo 发送这个戳一戳消息
     */
    override fun nudge(): UserNudge {
        TODO("Not yet implemented")
    }

    /**
     * 向这个对象发送消息.
     *
     * 单条消息最大可发送 4500 字符或 50 张图片.
     *
     * @see UserMessagePreSendEvent 发送消息前事件
     * @see UserMessagePostSendEvent 发送消息后事件
     *
     * @throws EventCancelledException 当发送消息事件被取消时抛出
     * @throws MessageTooLargeException 当消息过长时抛出
     * @throws IllegalArgumentException 当消息内容为空时抛出 (详见 [Message.isContentEmpty])
     *
     * @return 消息回执. 可 [引用][MessageReceipt.quote] 或 [撤回][MessageReceipt.recall] 这条消息.
     */
    override suspend fun sendMessage(message: Message): MessageReceipt<User> {
        replyFn(message.contentToString())
        return WebMessageReceipt(event!!, this)
    }

    /**
     * 上传一个 [资源][ExternalResource] 作为图片以备发送.
     *
     * **无论上传是否成功都不会关闭 [resource]. 需要调用方手动关闭资源**
     *
     * 也可以使用其他扩展: [ExternalResource.uploadAsImage] 使用 [File], [InputStream] 等上传.
     *
     * @see Image 查看有关图片的更多信息, 如上传图片
     *
     * @see BeforeImageUploadEvent 图片发送前事件, 可拦截.
     * @see ImageUploadEvent 图片发送完成事件, 不可拦截.
     *
     * @see ExternalResource
     *
     * @throws EventCancelledException 当发送消息事件被取消时抛出
     * @throws OverFileSizeMaxException 当图片文件过大而被服务器拒绝上传时抛出. (最大大小约为 20 MB, 但 mirai 限制的大小为 30 MB)
     */
    override suspend fun uploadImage(resource: ExternalResource): Image {
        TODO("Not yet implemented")
    }

}

@OptIn(MiraiInternalApi::class)
@Suppress("DEPRECATION_ERROR")
class WebMessageReceipt<out C: Contact>(val event: WebCommandEvent, target: C)
    : MessageReceipt<C>(ToWeb(event),
    target
)

@OptIn(MiraiInternalApi::class)
class ToWeb(event: WebCommandEvent): OnlineMessageSource.Outgoing.ToFriend() {
    /**
     * 消息 ids (序列号). 在获取失败时 (概率很低) 为空数组.
     *
     * ### 值域
     * 值的范围约为 [UShort] 的范围.
     *
     * ### 顺序
     * 群消息的 id 由服务器维护. 好友消息的 id 由 mirai 维护.
     * 此 id 不一定从 0 开始.
     *
     * - 在同一个群的消息中此值随每条消息递增 1, 但此行为由服务器决定, mirai 不保证自增顺序.
     * - 在好友消息中无法保证每次都递增 1. 也可能会产生大幅跳过的情况.
     *
     * ### 多 ID 情况
     * 对于单条消息, [ids] 为单元素数组. 对于分片 (一种长消息处理机制) 消息, [ids] 将包含多元素.
     *
     * [internalIds] 与 [ids] 以数组下标对应.
     */
    override val ids: IntArray = intArrayOf()

    /**
     * 内部 ids. **仅用于协议模块使用**
     *
     * 值没有顺序, 也可能为 0, 取决于服务器是否提供.
     *
     * 在事件中和在引用中无法保证同一条消息的 [internalIds] 相同.
     *
     * [internalIds] 与 [ids] 以数组下标对应.
     *
     * @see ids
     */
    override val internalIds: IntArray = ids

    /**
     * 原消息内容.
     *
     * 此属性是 **lazy** 的: 它只会在第一次调用时初始化, 因为需要反序列化服务器发来的整个包, 相当于接收了一条新消息.
     */
    override val originalMessage: MessageChain
        get() = TODO("Not yet implemented")

    /**
     * 发送时间时间戳, 单位为秒.
     *
     * 时间戳可能来自服务器, 也可能来自 mirai, 且无法保证两者时间同步.
     */
    override val time: Int = (Date().time / 1000).toInt()

    /**
     * @see botId
     */
    override val bot: Bot = event.bot
    override val sender: Bot = bot
    override val target: Friend by lazy { event.sender as Friend }
}
