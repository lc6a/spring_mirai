package com.lc.spring_mirai.web.controller.bot

import com.lc.spring_mirai.annotation.BotController
import com.lc.spring_mirai.annotation.EventFilter
import com.lc.spring_mirai.annotation.PermissionFilter
import com.lc.spring_mirai.annotation.RequestMapped
import com.lc.spring_mirai.web.entity.Answer
import com.lc.spring_mirai.web.service.AnswerService
import com.lc.spring_mirai.web.service.DataService
import net.mamoe.mirai.event.events.GroupMessageEvent
import javax.annotation.Resource


@EventFilter(GroupMessageEvent::class)
@BotController
@RequestMapped
class AskAnswerBotController: BaseBotController() {

    override var showName: String = "问答系统"

    companion object {
        private const val setAnswer = "设置问答"
    }

    @Resource
    private lateinit var answerService: AnswerService

    @RequestMapped("{question}")
    fun getAnswer(question: String, event: GroupMessageEvent): String?{
        val ans = answerService.getAnswer(question)
        if (ans != null && ans != "")
            return ans
        val vagueAns = answerService.getVagueAnswer(question)
        if (vagueAns.isEmpty())
            return null
        if (vagueAns.size == 1) {
            return vagueAns[0].answer
        }
        val sb = StringBuilder()
        for (a in vagueAns)
            sb.append("问题[${a.question}]的答案是[${a.answer}]\n")
        return sb.toString()
    }

    @RequestMapped("问/{question}/答")
    @PermissionFilter(setAnswer)
    fun setAnswer(question: String, argc: String, event: GroupMessageEvent): String{
        answerService.setAnswer(Answer(question, argc))
        return reply(question, argc)
    }

    @RequestMapped("模糊问/{question}/答")
    @PermissionFilter(setAnswer)
    fun setVagueAnswer(question: String, argc: String, event: GroupMessageEvent): String{
        answerService.setVagueAnswer(Answer(question, argc))
        return reply(question, argc)
    }

    /**
     * 设置问题的答案
     */
    private fun reply(question: String, answer: String): String{
        return if (answer.isEmpty()){
            "已删除问题${question}的回答"
        }else{
            "已设置问题${question}的回答为${answer}"
        }
    }
}