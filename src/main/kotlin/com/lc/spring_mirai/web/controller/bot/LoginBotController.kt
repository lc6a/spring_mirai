package com.lc.spring_mirai.web.controller.bot

import com.lc.spring_mirai.annotation.BotController
import com.lc.spring_mirai.annotation.PermissionFilter
import com.lc.spring_mirai.annotation.RequestMapped
import com.lc.spring_mirai.demo.service.PermissionService
import com.lc.spring_mirai.web.controller.bot.LoginBotController.Companion.PERMISSION_LOGIN
import com.lc.spring_mirai.web.controller.bot.LoginBotController.Companion.ctrlName
import com.lc.spring_mirai.web.error.PermissionErrors
import com.lc.spring_mirai.web.service.DataService
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.utils.BotConfiguration
import javax.annotation.Resource

@BotController(ctrlName)
@PermissionFilter(PERMISSION_LOGIN)
class LoginBotController: BaseBotController() {

    companion object {
        internal const val PERMISSION_LOGIN = "login"
        internal const val ctrlName = "loginBotController"
    }

    override var managerUrl: String? = "/ctrl/loginManager.html"

    @Resource
    private lateinit var dataService: DataService

    override var showName: String = "登录控制器"

    private inline fun getBots() = Bot.instances

    private inline fun getBot(qqId: Long) = Bot.getInstanceOrNull(qqId)

    @RequestMapped("登录/{qqId}/{password}")
    suspend fun login(qqId: Long, password: String): String {
        val bot = getBot(qqId)
        if (bot != null) {
            if (bot.isOnline)
                return "该机器人已登录"
        }
        BotFactory.newBot(qqId, password){
            fileBasedDeviceInfo()
            protocol = BotConfiguration.MiraiProtocol.ANDROID_PAD
        }.login()
        return "登录成功"
    }

    @RequestMapped("保存登录信息/{qqId}/{password}")
    fun save(qqId: Long, password: String): String {
        dataService.setData("qq-$qqId", password, ctrlName)
        return "保存成功"
    }

    @RequestMapped("登录/{qqId}")
    suspend fun login(qqId: Long, event: MessageEvent): String {
        if (!permissionService.havePermission(event.sender.id, getLoginWithoutPasswordPermission(qqId))) {
            return PermissionErrors.noPermission
        }
        val password = dataService.getData("qq-$qqId", String::class.java ,ctrlName) ?: return "请提供密码或保存登录信息"
        return login(qqId, password)
    }

    @Resource
    private lateinit var permissionService: PermissionService

    private fun getLoginWithoutPasswordPermission(qqId: Long) = "$PERMISSION_LOGIN-login-$qqId"

    private fun getLogoutPermission(qqId: Long) = "$PERMISSION_LOGIN-logout-$qqId"

    @RequestMapped("退出登录/{qqId}")
    suspend fun logout(qqId: Long, event: MessageEvent): String {
        if (!permissionService.havePermission(event.sender.id, getLogoutPermission(qqId))) {
            return PermissionErrors.noPermission
        }
        val bot = getBot(qqId)
        if (bot == null || !bot.isOnline) {
            return "该账号未在本系统登录"
        }
        return "退出登录成功"
    }

}

data class BotStatus(
    var id: Long,
    var online: Boolean
)