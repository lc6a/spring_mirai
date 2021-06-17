package com.lc.spring_mirai

import com.lc.spring_mirai.config.Config
import com.lc.spring_mirai.config.ServerConfig
import com.lc.spring_mirai.util.SpringApplicationContextUtil
import com.lc.spring_mirai.util.URLOpener
import net.mamoe.mirai.console.data.AbstractPluginData
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.LoggerAdapters.asMiraiLogger
import net.mamoe.mirai.utils.MiraiLogger
import net.mamoe.mirai.utils.debug
import org.slf4j.LoggerFactory
import org.springframework.boot.runApplication

object SpringMiraiStartPlugin: KotlinPlugin(
    JvmPluginDescription(
        id = "com.lc.spring.mirai.SpringMiraiStartPlugin",
        version = "0.0.1",
        name = "SpringMirai"
    )
) {

    override fun PluginComponentStorage.onLoad() {
        MiraiLogger.setDefaultLoggerCreator { identity -> LoggerFactory.getLogger(identity).asMiraiLogger() }
        runApplication<SpringMiraiApplication>()
        SpringApplicationContextUtil.context.getBeansOfType(PluginData::class.java).values.forEach {
            it.reload()
        }
        val config = SpringApplicationContextUtil.context.getBean(Config::class.java)
        if (config.autoOpenUrl) {
            val serverConfig = SpringApplicationContextUtil.context.getBean(ServerConfig::class.java)
            URLOpener.openURL(serverConfig.url)
        }
    }

    override fun onEnable() {
        logger.debug { "Start Spring Mirai" }

    }
}


object DemoPluginData: AbstractPluginData() {
    /**
     * 这个 [PluginData] 保存时使用的名称.
     */
    override val saveName: String = "SpringMiraiDemo"
}