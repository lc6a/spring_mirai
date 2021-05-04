package com.lc.spring_mirai.web.controller

import com.lc.spring_mirai.annotation.Replace
import com.lc.spring_mirai.invoke.after.AfterHandle
import com.lc.spring_mirai.invoke.after.AfterHandleData
import com.lc.spring_mirai.invoke.after.ReplyAfterHandle
import com.lc.spring_mirai.web.custom.event.WebCommandEvent
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.broadcast
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.content
import org.springframework.stereotype.Component
import java.io.IOException
import java.lang.Exception
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import javax.annotation.Resource
import javax.websocket.*
import javax.websocket.server.PathParam
import javax.websocket.server.ServerEndpoint

@ServerEndpoint("/ctrl/{username}")
@Component
class CtrlCommandWebSocket {

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
    val lineStart = "spring mirai # "

    @OnMessage
    @Throws(IOException::class)
    fun onMessage(message: String, session: Session?) {
        when {
            message[0].toInt() == 127 -> {        // 退格键
                if (buffer.isEmpty())
                    return
                buffer = StringBuffer(buffer.substring(0, buffer.length - 1))
                sendMessage(session, "\b \b")
                return
            }
            message != "\r" -> {
                buffer.append(message)
                sendMessage(session, message)
                return
            }
            else -> {
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
        }

    }

    private fun execCommand(line: String, fn: (String) -> Unit): String {
        Bot.instances.forEach {
            runBlocking {
                WebCommandEvent(it, line, (Date().time / 1000).toInt(), fn).broadcast()
            }

        }
        return ""
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

}

/**
 * 取代原始的回复处理，遇到[WebCommandEvent]时通过网络回复
 * 否则沿用原来的逻辑回复
 */
@Component
@Replace(ReplyAfterHandle::class)
class WebReplyAfterHandle: AfterHandle {

    @Resource
    private lateinit var replyAfterHandle: ReplyAfterHandle

    override fun after(data: AfterHandleData) {
        if (data.ret != Unit && data.ret != null && data.request.event is WebCommandEvent) {
            val msg: String = when (data.ret) {
                is String -> data.ret
                is Message -> data.ret.content
                else -> {
                    "未知返回值：${data.ret}"
                }
            }
            (data.request.event as WebCommandEvent).sendWebMessage(msg)
            return
        }
        replyAfterHandle.after(data)    //使用原来的回复逻辑
    }

}