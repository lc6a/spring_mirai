package com.lc.spring_mirai

import com.lc.spring_mirai.SpringMiraiApplication.Companion.springMiraiLogger
import com.lc.spring_mirai.annotation.RequestMapper
import com.lc.spring_mirai.annotation.NeedPermission
import com.lc.spring_mirai.bean.Config
import com.lc.spring_mirai.exception.MapperException
import com.lc.spring_mirai.exception.PermissionDeniedException
import com.lc.spring_mirai.exception.UserException
import com.lc.spring_mirai.logger.SimpleJvmConsoleLogger
import com.lc.spring_mirai.service.IPermissionService
import com.lc.spring_mirai.route.Route
import com.lc.spring_mirai.route.RouteMapper
import com.lc.spring_mirai.session.GroupSession
import com.lc.spring_mirai.session.Session
import com.lc.spring_mirai.session.SessionManager
import com.lc.spring_mirai.util.AnnotationUtil
import kotlinx.coroutines.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.FriendEvent
import net.mamoe.mirai.event.events.GroupEvent
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.TempMessageEvent
import net.mamoe.mirai.message.data.*
import org.springframework.context.ConfigurableApplicationContext
import kotlin.reflect.*
import kotlin.reflect.full.callSuspendBy
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.jvm.jvmErasure

class SpringMiraiApplication() {

    private lateinit var bot: Bot

    /**
     * 运行
     */
    suspend fun runApplication(_context: ConfigurableApplicationContext, bot: Bot, _config: Config? = null){
        context = _context
        config = _config ?: context.getBean(Config::class.java)
        Route.instance
        this.bot = bot
        runMiraiMain(bot)
    }

    /**
     * 关掉当前Bot
     */
    fun exitBot(){
        bot.close(null)
    }

   companion object {
       val springMiraiLogger = SimpleJvmConsoleLogger()
       lateinit var context: ConfigurableApplicationContext
       lateinit var config: Config
    }
}

private fun onEvent(event: Event) {
    runBlocking {
        try {
            if (event::class.isSubclassOf(MessageEvent::class))
                SessionManager.instance.getSession(event as MessageEvent)
            RouteMapper.mapper(event, ::invokeCtrlFunc)
        } catch (e: MapperException) {
            springMiraiLogger.verbose(e.message)
        } catch (e: PermissionDeniedException) {
            if (event::class.isSubclassOf(MessageEvent::class))
                (event as MessageEvent).quoteReply(e.message ?: "权限不足")
        } catch (e: UserException) {
            if (event::class.isSubclassOf(MessageEvent::class))
                (event as MessageEvent).quoteReply(e.message ?: "权限不足")
        }
    }
}


/**
 * 运行mirai机器人
 */
private suspend fun runMiraiMain(bot: Bot, onEvent: (Event)->Unit = ::onEvent) {
    bot.subscribeAlways<Event> { event ->
        onEvent(event)
    }
}

/**
 * 执行控制器的方法
 */
private fun invokeCtrlFunc(ctrl: Any, func: KFunction<*>, routes: List<String>, argc: String, event: Event) {
    if (event::class.isSubclassOf(MessageEvent::class)){
        checkPermission(ctrl, func, event as MessageEvent)
    }
    val params = paramInject(ctrl, func, routes, argc, event)
    runBlocking {
        val ret: Any? = if (func.isSuspend) {
            func.callSuspendBy(params)
        } else {
            func.callBy(params)
        }
        if (ret != null && ret !is Unit) {
            replyRet(ret, event)
        }
    }
}

/**
 * 参数注入
 *
 * @return 控制器方法调用时传入的参数
 */
private fun paramInject(ctrl: Any, func: KFunction<*>, routes: List<String>, argc: String, event: Event): Map<KParameter, Any?>{
    var cmd = AnnotationUtil.getAnnotation(func, RequestMapper::class)!!.value
    val cmds = mutableListOf<String>()
    while (true) {
        val one = Route.instance.getStrBeforeSeparator(cmd)
        if (one == "")
            break
        cmds.add(one)
        if (one == cmd) {
            break
        }
        if (one.length + 1 < cmd.length)
            cmd = cmd.substring(one.length + 1)
        else
            break
    }
    val funcRoutes = routes.subList(routes.size - cmds.size, routes.size)
    val routeParam = mutableMapOf<String, String>()
    for (i in cmds.indices) {
        if (Route.instance.isPlaceholder(cmds[i])) {
            val name = cmds[i].substring(1, cmds[i].lastIndex)  //去掉头尾的{}
            routeParam[name] = funcRoutes[i]
        }
    }

    val kParams = func.parameters
    val params: MutableMap<KParameter, Any?> = mutableMapOf()
    for (i in kParams.indices) {
        val kp = kParams[i]
        //第一个参数
        if (kp.type.jvmErasure == ctrl::class){
            params[kp] = ctrl
            continue
        }
        if (kp.type.jvmErasure.isSuperclassOf(event::class)){
            params[kp] = event
            continue
        }
        when (kp.type.jvmErasure) {
            //这里写允许参数注入的类型
            Session::class, GroupSession::class -> params[kp] = SessionManager.instance.getSession(event as MessageEvent)
            else -> {
                //是路径中的占位符
                if (routeParam.containsKey(kp.name)) {
                    params[kp] = (string2obj(routeParam[kp.name]!!, kp.type.jvmErasure))
                }else if (kp.name == "argc" || kp.name == "args"){
                    params[kp] = argc
                } else {
                    throw Exception("无法处理的参数：${kp.name}，类型为${kp.type}")
                }
            }
        }
    }
    return params
}

/**
 * 检查发送消息者的权限
 *
 * @throws PermissionDeniedException 没有权限时抛出
 */
private fun checkPermission(ctrl: Any, func: KFunction<*>, event: MessageEvent){
    val sender = event.sender
    val ctrlPermission = AnnotationUtil.getAnnotation(ctrl::class, NeedPermission::class)?.permission
    val funcPermission = AnnotationUtil.getAnnotation(func, NeedPermission::class)?.permission
    val iPermissionService = SpringMiraiApplication.context.getBean(IPermissionService::class.java)
    if (!iPermissionService.havePermission(sender.id, ctrlPermission))
        throw PermissionDeniedException("你没有[${ctrlPermission}]权限")
    if (!iPermissionService.havePermission(sender.id, funcPermission))
        throw PermissionDeniedException("你没有[${funcPermission}]权限")
}

/**
 * 控制器方法返回值处理
 * 将ret作为消息发送出去
 */
private suspend fun replyRet(ret: Any, event: Event) {
    val message: MessageChain = when (ret) {
        is String -> PlainText(ret).asMessageChain()
        is Message -> ret.asMessageChain()
        is Image -> ret.asMessageChain()
        else -> ret as MessageChain
    }
    when (event) {
        is GroupMessageEvent -> event.group.sendMessage(message)
        is GroupEvent -> event.group.sendMessage(message)
        is FriendEvent -> event.friend.sendMessage(message)
        is TempMessageEvent -> event.sender.sendMessage(message)
    }
}

/**
 * 根据参数类型注入
 */
private fun string2obj(string: String, clazz: KClass<*>): Any {
    return when (clazz) {
        Int::class -> string.toInt()
        Long::class -> string.toLong()
        String::class -> string
        Short::class -> string.toShort()
        Byte::class -> string.toShort()
        Double::class -> string.toDouble()
        Float::class -> string.toFloat()
        Boolean::class -> string2Bool(string)
        else -> throw MapperException("类型转换失败，未识别的参数类型:${clazz}")
    }
}

/**
 * 字符串注入bool类型
 */
private fun string2Bool(string: String, positiveWords: Set<String> = SpringMiraiApplication.config.positiveWords): Boolean {
    val ok = setOf("true", "ok", "启用", "开启", "授予", "开始", "确认", "确定")
    return ok.contains(string) || positiveWords.contains(string)
}