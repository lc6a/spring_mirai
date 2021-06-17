package com.lc.spring_mirai

import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SpringMiraiApplication

var runArgs: Array<String> = emptyArray()

@OptIn(ConsoleExperimentalApi::class)
fun main(args: Array<String>) {
    runArgs = args
    MiraiConsoleTerminalLoader.startAsDaemon()
    SpringMiraiStartPlugin.load()
    SpringMiraiStartPlugin.enable()
}
