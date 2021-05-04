package com.lc.spring_mirai.web.controller

import com.google.gson.Gson
import com.lc.spring_mirai.util.JsonUtil
import net.mamoe.mirai.console.plugin.PluginManager
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.description
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.disable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.id
import net.mamoe.mirai.console.plugin.name
import net.mamoe.mirai.console.plugin.version
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource

@RestController
@CrossOrigin
@RequestMapping("miraiConsole")
class MiraiConsoleManagerController {

    @Resource
    private lateinit var jsonUtil: JsonUtil

    @RequestMapping("plugins")
    fun plugins(): String {
        val list = mutableListOf<PluginStatus>()
        PluginManager.INSTANCE.plugins.forEach {
            list.add(PluginStatus(it.id, it.name, it.version.toString(), it.isEnabled))
        }
        return jsonUtil.toJson(list)
    }

    @RequestMapping("setEnable")
    fun setEnable(@RequestParam enable: Boolean, @RequestParam id: String) {
        val plugin = PluginManager.INSTANCE.plugins.find { it.id == id } ?: return
        if (enable != plugin.isEnabled) {
            if (enable) {
                plugin.enable()
            } else {
                plugin.disable()
            }
        }
    }
}

data class PluginStatus(
    var id: String,
    var name: String,
    var version: String,
    var enable: Boolean
)