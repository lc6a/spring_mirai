package com.lc.spring_mirai.web.controller

import com.lc.spring_mirai.web.dto.Result
import com.lc.spring_mirai.web.error.TokenAuthExpiredException
import com.lc.spring_mirai.web.token.TokenUtil
import net.mamoe.mirai.Bot
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("token")
@CrossOrigin
class TokenController {

    @Resource
    private lateinit var tokenUtil: TokenUtil

    @RequestMapping("check")
    fun check(@RequestParam token: String, response: HttpServletResponse): String? {
        return try {
            tokenUtil.checkToken(token, response)
            val id = tokenUtil.parseToken(token).userId
            // -1 是控制台
            if (id == -1L) {
                return Result.success.toJson()
            }
            Bot.instances.forEach {
                if (it.id == id) {
                    return Result.success.toJson()
                } else {
                    it.getFriend(id)?.also {
                        return Result.success.toJson()
                    }
                }
            }
            Result.tokenError.toJson()
        } catch (e: TokenAuthExpiredException) {
            Result.tokenError.toJson()
        }
    }

    @RequestMapping("user")
    fun user(@RequestParam token: String): UserData? {
        val id = tokenUtil.parseToken(token).userId
        if (id == -1L) {
            return UserData(id, "控制台", "/picture/console.jpeg")
        }
        Bot.instances.forEach { bot ->
            if (bot.id == id) {
                return UserData(bot.id, bot.nick, bot.avatarUrl)
            } else {
                bot.getFriend(id)?.also {
                    return UserData(it.id, it.nick, it.avatarUrl)
                }
            }
        }
        throw TokenAuthExpiredException()
    }

    data class UserData(
        val id: Long,
        val name: String,
        val imgUrl: String
    )
}