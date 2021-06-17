package com.lc.spring_mirai

import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import org.junit.jupiter.api.Test

/**
 * @Author 19525
 * @Date 2021/2/14 18:48
 */
class PluginTest {

    @Test
    fun test() {
        MiraiConsoleTerminalLoader.startAsDaemon()
            SpringMiraiStartPlugin.load()
            SpringMiraiStartPlugin.enable()
    }
}