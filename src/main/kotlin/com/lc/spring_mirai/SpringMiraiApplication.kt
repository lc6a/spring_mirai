package com.lc.spring_mirai

import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.utils.BotConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringMiraiApplication

suspend fun main(args: Array<String>) {
    runApplication<SpringMiraiApplication>(*args)
    val bot = BotFactory.newBot(qq, password) {
        fileBasedDeviceInfo()
        protocol = BotConfiguration.MiraiProtocol.ANDROID_PAD
    }.alsoLogin()
    bot.join()
}

const val qq = 123L

const val password = "abc"