package com.lc.spring_mirai

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.debug
import org.springframework.boot.runApplication

object SpringMiraiDemoPlugin: KotlinPlugin(
    JvmPluginDescription(
        id = "com.lc.spring.mirai.SpringMiraiDemoPlugin",
        version = "0.0.1",
        name = "SpringMiraiDemoPlugin"
    )
) {
    override fun onEnable() {
        logger.debug { "Start Spring Mirai" }
        runApplication<SpringMiraiApplication>()
    }
}