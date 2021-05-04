package com.lc.spring_mirai

import com.lc.spring_mirai.application.StartSpringMirai
import com.lc.spring_mirai.util.SpringApplicationContextUtil
import com.lc.spring_mirai.web.service.impl.CommandServiceImpl
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.utils.BotConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringMiraiApplication

@OptIn(ConsoleExperimentalApi::class)
suspend fun main(args: Array<String>) {
    runApplication<SpringMiraiApplication>(*args)
    val bean = SpringApplicationContextUtil.context.getBean(StartSpringMirai::class.java)
    val bot = BotFactory.newBot(qq, password) {
        fileBasedDeviceInfo()
        protocol = BotConfiguration.MiraiProtocol.ANDROID_PAD
    }.alsoLogin()
    MiraiConsoleTerminalLoader.startAsDaemon()
    //bot.join()
}













const val qq = 2120906525L


















const val password = "li12345678.."