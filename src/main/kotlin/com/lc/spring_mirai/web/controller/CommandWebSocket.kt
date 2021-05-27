package com.lc.spring_mirai.web.controller

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.PermitteeId
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.CoroutineScopeUtils.childScopeContext
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.content
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CancellationException
import javax.websocket.server.ServerEndpoint
import kotlin.coroutines.CoroutineContext
import java.util.concurrent.ConcurrentHashMap

import java.util.concurrent.atomic.AtomicInteger

import java.io.IOException
import java.io.PipedReader
import java.io.PipedWriter
import java.lang.Exception
import java.util.*
import javax.websocket.*

import javax.websocket.server.PathParam
import kotlin.jvm.Throws


@ServerEndpoint("/command/{username}")
@Component
class CommandWebSocket {


    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private val onlineNum = AtomicInteger()

    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    private val sessionPools: ConcurrentHashMap<String, Session> = ConcurrentHashMap<String, Session>()

    //发送消息
    @Throws(IOException::class)
    fun sendMessage(session: Session?, message: String) {
        if (session != null) {
            synchronized(session) {
                session.basicRemote.sendText(message)
            }
        }
    }

    //给指定用户发送信息
    fun sendInfo(userName: String, message: String) {
        val session: Session? = sessionPools[userName]
        try {
            sendMessage(session, message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 群发消息
    fun broadcast(message: String) {
        for (session in sessionPools.values) {
            try {
                sendMessage(session, message)
            } catch (e: Exception) {
                e.printStackTrace()
                continue
            }
        }
    }

    //建立连接成功调用
    @OnOpen
    fun onOpen(session: Session, @PathParam(value = "username") userName: String) {
        sessionPools[userName] = session
        addOnlineCount()
        sendMessage(session, lineStart)
    }

    //关闭连接时调用
    @OnClose
    fun onClose(@PathParam(value = "username") userName: String) {
        sessionPools.remove(userName)
        subOnlineCount()
    }

    var buffer = StringBuffer()
    val lineStart = "mirai console # "

    @OnMessage
    @Throws(IOException::class)
    fun onMessage(message: String, session: Session?) {
        if (message[0].toInt() == 127) {        // 退格键
            if (buffer.isEmpty())
                return
            buffer = StringBuffer(buffer.substring(0, buffer.length - 1))
            sendMessage(session, "\b \b")
            return
        }
        if (message != "\r") {
            buffer.append(message)
            sendMessage(session, message)
            return
        }
        val line = buffer.toString()
        if (line.isBlank()) {
            sendMessage(session, "\n$lineStart")
            return
        }
        sendMessage(session, "\n")
        buffer = StringBuffer()
        val ret = runBlocking {
            execCommand(line) { str -> sendMessage(session, str) }
        }
        if (ret.isNotEmpty())
            sendMessage(session, ret)
        sendMessage(session, "\n$lineStart")

    }

    //错误时调用
    @OnError
    fun onError(session: Session?, throwable: Throwable) {
        println("发生错误")
        throwable.printStackTrace()
    }

    fun addOnlineCount() {
        onlineNum.incrementAndGet()
    }

    fun subOnlineCount() {
        onlineNum.decrementAndGet()
    }

    fun getOnlineNumber(): AtomicInteger? {
        return onlineNum
    }

    fun getSessionPools(): ConcurrentHashMap<String, Session>? {
        return sessionPools
    }






    private suspend fun execCommand(commandStr: String, fn: (String) -> Unit): String {
        var command = if (commandStr[0] != '/') {
            "/$commandStr"
        } else {
            commandStr
        }
        if (command == "/?") {
            command = "/help"
        }
        try {
            // consoleLogger.debug("INPUT> $next")
            val result = WebCommandSender(fn).executeCommand(command)
            when (result) {
                is CommandExecuteResult.Success -> {
                    return ""
                }
                is CommandExecuteResult.IllegalArgument -> { // user wouldn't want stacktrace for a parser error unless it is in debugging mode (to do).
                    return result.exception.message ?: result.exception.localizedMessage
                }
                is CommandExecuteResult.ExecutionFailed -> {
                    return result.exception.localizedMessage
                }
                is CommandExecuteResult.UnresolvedCommand -> {
                    return "未知指令: ${command}, 输入 ? 获取帮助"
                }
                is CommandExecuteResult.PermissionDenied -> {
                    return "权限不足."
                }
                is CommandExecuteResult.UnmatchedSignature -> {
                    return "参数不匹配"
                }
                is CommandExecuteResult.Failure -> {
                    return result.toString()
                }
                else -> {
                    println(result)
                }
            }
        } catch (e: InterruptedException) {
            return ""
        } catch (e: CancellationException) {
            return ""
        } catch (e: Throwable) {
            return "Unhandled exception: ${e.localizedMessage}"
        }
        return ""
    }
}

/**
 * 由Web来发送指令
 * @param fn 返回消息的回调函数
 */
internal class WebCommandSender(val fn: (String) -> Unit): CommandSender {
    private val NAME = "Web"

    /**
     * 与这个 [CommandSender] 相关的 [Bot].
     * 当通过控制台执行时为 `null`.
     */
    override val bot: Bot? = null

    /**
     * The context of this scope.
     * Context is encapsulated by the scope and used for implementation of coroutine builders that are extensions on the scope.
     * Accessing this property in general code is not recommended for any purposes except accessing the [Job] instance for advanced usages.
     *
     * By convention, should contain an instance of a [job][Job] to enforce structured concurrency.
     */
    override val coroutineContext: CoroutineContext by lazy { MiraiConsole.childScopeContext(NAME) }
    /**
     * [User.nameCardOrNick] 或 [ConsoleCommandSender.NAME]
     */
    override val name: String = NAME


    override val permitteeId: PermitteeId = AbstractPermitteeId.Console

    /**
     * 与这个 [CommandSender] 相关的 [Contact].
     *
     * - 当一个群员执行指令时, [subject] 为所在 [群][Group]
     * - 当通过控制台执行时为 `null`.
     */
    override val subject: Contact? = null

    /**
     * 指令原始发送*人*.
     * - 当通过控制台执行时为 `null`.
     */
    override val user: User? = null

    /**
     * 立刻发送一条消息.
     *
     * 对于 [MemberCommandSender], 这个函数总是发送给所在群
     */
    override suspend fun sendMessage(message: String): MessageReceipt<Contact>? {
        fn(message)
        return null
    }

    /**
     * 立刻发送一条消息.
     *
     * 对于 [MemberCommandSender], 这个函数总是发送给所在群
     */
    override suspend fun sendMessage(message: Message): MessageReceipt<Contact>? {
        return sendMessage(message.content)
    }

}