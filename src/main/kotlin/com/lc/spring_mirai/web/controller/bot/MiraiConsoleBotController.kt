@file:OptIn(ConsoleFrontEndImplementation::class)
@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.lc.spring_mirai.web.controller.bot

import com.lc.spring_mirai.annotation.BotController
import com.lc.spring_mirai.annotation.PermissionFilter
import com.lc.spring_mirai.annotation.RequestMapped
import com.lc.spring_mirai.util.JsonUtil
import com.lc.spring_mirai.web.controller.bot.MiraiConsoleBotController.Companion.MIRAI_CONSOLE_MANAGER
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.console.ConsoleFrontEndImplementation
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import javax.annotation.Resource
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.isAccessible

@BotController
@RequestMapped("控制台")
@PermissionFilter(MIRAI_CONSOLE_MANAGER)
class MiraiConsoleBotController: BaseBotController() {
    override var showName: String = "MiraiConsole管理"

    @Resource
    private lateinit var jsonUtil: JsonUtil

    companion object {
        internal const val MIRAI_CONSOLE_MANAGER = "miraiConsoleManager"
    }

    @RequestMapped("启动")
    fun start(): String {
        if (MiraiConsole.isActive) {
            return "启动失败，控制台已在运行"
        }
        startMiraiConsole()
        return "启动成功"
    }

    @RequestMapped("关闭")
    fun close(): String {
        if (!MiraiConsole.isActive) {
            return "关闭失败，控制台没有运行"
        }
        closeKeepBots()
        return "关闭成功"
    }

    @RequestMapped("重启")
    fun restart(): String {
        closeKeepBots()
        startMiraiConsole()
        return "重启成功"
    }

    /**
     * 关闭控制台，但保持机器人登录
     */
    private fun closeKeepBots() {
        if (MiraiConsole.isActive) {
            println(123)
            val bots = Bot.instances.filter { it.isOnline }
            runBlocking {
                MiraiConsole.job.cancel()

            }
            println(456)
            runBlocking {
                bots.forEach {
                    BotFactory.newBot(it.id, getMd5(it)){
                        fileBasedDeviceInfo()
                    }.login()
                }
            }
            println(789)
        }
    }

}

fun getMd5(bot: Bot): ByteArray {
    val botAccountField = Class.forName("net.mamoe.mirai.internal.QQAndroidBot").getDeclaredField("account")
    val botAccountFieldAccessible = botAccountField.canAccess(bot)
    botAccountField.isAccessible = true
    val botAccount = botAccountField.get(bot)
    botAccountField.isAccessible = botAccountFieldAccessible
    val md5Field = Class.forName("net.mamoe.mirai.internal.BotAccount").getDeclaredField("passwordMd5")
    val md5FieldAccessible = md5Field.canAccess(botAccount)
    md5Field.isAccessible = true
    val array = md5Field.get(botAccount) as ByteArray
    md5Field.isAccessible = md5FieldAccessible
    return array
}


fun startMiraiConsole() {
    // MiraiConsoleImplementation.instance = MiraiConsoleImplementationTerminal() // MiraiConsoleImplementation.kt 275
    val instance = MiraiConsoleImplementationTerminal()
    val obj = MiraiConsoleImplementation::class.companionObjectInstance!!
    val field = obj::class.declaredMemberProperties.find { it.name == "instance" }!! as KMutableProperty1<Any, Any>
    val accessible = field.isAccessible
    field.isAccessible = true
    field.set(obj, instance)
    field.isAccessible = accessible

    // MiraiConsoleImplementationBridge.doStart()   // MiraiConsoleImplementation.kt 277
    val bridgeObj = Class.forName("net.mamoe.mirai.console.internal.MiraiConsoleImplementationBridge")
        .kotlin.objectInstance!!
    val doStart = bridgeObj::class.memberFunctions.find { it.name == "doStart" }!!
    val doStartAccessible = doStart.isAccessible
    doStart.isAccessible = true
    doStart.call(bridgeObj)
    doStart.isAccessible = doStartAccessible

    val startup = MiraiConsoleTerminalLoader::class.memberFunctions.find { it.name == "startupConsoleThread" }!!
    val startupAccessible = startup.isAccessible
    startup.isAccessible = true
    startup.call(MiraiConsoleTerminalLoader)
    startup.isAccessible = startupAccessible
}