package com.lc.spring_mirai.web.controller

import com.lc.spring_mirai.config.Config
import com.lc.spring_mirai.util.JsonUtil
import com.lc.spring_mirai.web.controller.bot.getMd5
import com.lc.spring_mirai.web.dto.BotStatus
import com.lc.spring_mirai.web.dto.Result
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.utils.BotConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import javax.annotation.Resource

@RestController
@CrossOrigin
@RequestMapping("bot")
class BotManagerController {

    @Autowired
    private lateinit var config: Config

    @Resource
    private lateinit var jsonUtil: JsonUtil

    @RequestMapping("bots")
    fun bots() = jsonUtil.toJson(Bot.instances.map { bot -> BotStatus(bot) })


    private val md5s = mutableMapOf<Long, ByteArray>()

    @RequestMapping("login")
    fun login(@RequestParam qqId: Long, @RequestParam online: Boolean): Result {

        if (online) {
            return Result("暂未实现，请在控制台登录")
            TODO()
            if (Bot.getInstanceOrNull(qqId).let { it != null && it.isOnline }) {
                return Result("该账号已经在线")
            }
            runBlocking {
                BotFactory.newBot(qqId, md5s[qqId]!!).login()
            }
        }
        val bot = kotlin.runCatching {
            Bot.getInstance(qqId)
        }.getOrElse {
            return Result("没有该bot")
        }.let { bot ->
            bot.close()
            md5s[bot.id] = getMd5(bot)
        }
        return Result.success

    }

    @RequestMapping("newLogin")
    fun newLogin(@RequestParam qqId: Long,
                         @RequestParam password: String,
                         @RequestParam protocol: BotConfiguration.MiraiProtocol,
                         @RequestParam(required = false) deviceFile: MultipartFile?): Result {
        val filePath = "bots/$qqId/device.json"
        val file = File(filePath)
        deviceFile?.transferTo(file)
        val bot = runBlocking {
            BotFactory.newBot(qqId, password) {
                if (deviceFile != null) {
                    fileBasedDeviceInfo(filePath)
                } else if (file.exists()) {
                    fileBasedDeviceInfo(filePath)
                } else if (File(config.commonDeviceFilePath).exists()) {
                    fileBasedDeviceInfo(config.commonDeviceFilePath)
                }
                this.protocol = protocol
            }.alsoLogin()
        }
        return if (bot.isOnline)
            Result.success
        else {
            Result("登录失败")
        }

    }

}