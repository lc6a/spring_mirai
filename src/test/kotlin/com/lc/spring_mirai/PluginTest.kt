package com.lc.spring_mirai

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.plugin.PluginManager
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.utils.BotConfiguration
import org.junit.jupiter.api.Test

/**
 * @Author 19525
 * @Date 2021/2/14 18:48
 */
class PluginTest {

    @Test
    fun test() {
        MiraiConsoleTerminalLoader.startAsDaemon()
        runBlocking {
            val bot = BotFactory.newBot(qq, password) {
                fileBasedDeviceInfo()
                protocol = BotConfiguration.MiraiProtocol.ANDROID_PAD
            }.alsoLogin()
            SpringMiraiDemoPlugin.load()
            SpringMiraiDemoPlugin.enable()
            bot.join()
        }
    }
}