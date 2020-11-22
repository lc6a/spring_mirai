package com.lc.miraispring.controller

import com.lc.spring_mirai.annotation.EventType
import com.lc.spring_mirai.annotation.RequestMapper
import net.mamoe.mirai.message.MessageEvent
import org.springframework.stereotype.Controller

@RequestMapper("框架")
@EventType(MessageEvent::class)
@Controller
class GetCodeController {
    @RequestMapper("开源库")
    fun getGit() = "github: https://github.com/lc6a/spring_mirai\n" +
            "gitee: https://gitee.com/lc6a/spring_mirai"

    @RequestMapper("群")
    fun getGroup() = "群：827191054"

    @RequestMapper("作者")
    fun getAuthor() = "作者QQ: 1952511149"
}