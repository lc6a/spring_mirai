package com.lc.spring_mirai

import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringMiraiApplication

@OptIn(ConsoleExperimentalApi::class)
suspend fun main(args: Array<String>) {
    runApplication<SpringMiraiApplication>(*args)
    MiraiConsoleTerminalLoader.startAsDaemon()
    //val loader =  ModuleClassLoader(File("app.jar"), SpringMiraiApplication::class.java.classLoader)

}
