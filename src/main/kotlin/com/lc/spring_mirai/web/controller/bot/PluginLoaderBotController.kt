package com.lc.spring_mirai.web.controller.bot

import com.lc.spring_mirai.SpringMiraiStartPlugin
import com.lc.spring_mirai.annotation.BotController
import com.lc.spring_mirai.annotation.RequestMapped
import net.mamoe.mirai.console.plugin.PluginManager
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import java.io.File
import java.net.URLClassLoader

@BotController
class PluginLoaderBotController {

    private fun loadPlugin(path: String) {
        val loaderConstructor = Class.forName("net.mamoe.mirai.console.internal.plugin.JvmPluginClassLoader")
            .constructors[0]
        val impl = Class.forName("net.mamoe.mirai.console.internal.plugin.BuiltInJvmPluginLoaderImpl")
            .kotlin.objectInstance!!
        val loadersField = impl::class.java.getDeclaredField("classLoaders")
        val loadersFieldAccessible = loadersField.canAccess(impl)
        loadersField.isAccessible = true
        val loaders = loadersField.get(impl) as MutableList<URLClassLoader>
        loadersField.isAccessible = loadersFieldAccessible
        val loader = loaderConstructor.newInstance(File(path), PluginLoaderBotController::class.java.classLoader, loaders)
        if (!loaders.contains(loader)) {
            loaders.add(loader as URLClassLoader)
        }
        
    }

    @RequestMapped("加载所有插件")
    fun loadAll() {

    }

    private fun getPlugins() = PluginManager.INSTANCE.plugins

    private fun getNewJar() {

    }

    @RequestMapped("load")
    fun load(): String {
        SpringMiraiStartPlugin.load()
        SpringMiraiStartPlugin.enable()
        return "加载成功"
    }
}