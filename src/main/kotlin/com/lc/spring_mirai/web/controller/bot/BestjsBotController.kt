package com.lc.spring_mirai.web.controller.bot

import com.lc.spring_mirai.annotation.BotController
import com.lc.spring_mirai.annotation.EventFilter
import com.lc.spring_mirai.annotation.RequestMapped
import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.content
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@BotController
@RequestMapped("bestjs")
@EventFilter(FriendMessageEvent::class)
class BestjsBotController: BaseBotController() {
    override var showName: String = "bestjs钢琴脚本"

    @Autowired
    lateinit var bestjsBotData: BestjsBotData

    @RequestMapped("制作/{id}")
    fun make(id: Int, message: MessageChain, event: FriendMessageEvent): String {
        val param = message.content.substringAfterLast('?', missingDelimiterValue = "")
        val userInfo = bestjsBotData.userInfoMap[event.user.id] ?: return "发送\"bestjs 登录信息 [用户名] [密码]\"提供账号信息"
        var url = "http://49.235.33.223/qq-bot/make" +
                "?userName=${userInfo.userName}&password=${userInfo.password}&midId=${id}"
        if ("" != param) {
            url += "&$param"
        }

//        val conn = URL(url).openConnection()
//        val fileName = URLDecoder.decode(
//            conn.getHeaderField("Content-Disposition")
//                .substringAfter('=')
//                .replace("\"", ""), "UTF-8")
        return url
    }

    @RequestMapped("登录信息/{userName}/{password}")
    fun loginInfo(userName: String, password: String, event: FriendMessageEvent) {
        val userInfo = UserInfo(userName, password);
        bestjsBotData.userInfoMap[event.user.id] = userInfo
    }
}

@Serializable
data class UserInfo(val userName: String, val password: String)

@Component
class BestjsBotData: AutoSavePluginData("bestjs-bot") {
    val userInfoMap: MutableMap<Long, UserInfo> by value()
}