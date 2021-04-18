package com.lc.spring_mirai.web.controller.bot

import com.lc.spring_mirai.annotation.BotController
import com.lc.spring_mirai.annotation.EventFilter
import com.lc.spring_mirai.annotation.PermissionFilter
import com.lc.spring_mirai.annotation.RequestMapped
import com.lc.spring_mirai.web.entity.Answer
import com.lc.spring_mirai.web.service.AnswerService
import com.lc.spring_mirai.web.service.DataService
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageKey
import net.mamoe.mirai.message.data.MessageSource
import net.mamoe.mirai.message.data.PlainText
import javax.annotation.Resource


@EventFilter(GroupMessageEvent::class)
@BotController
@RequestMapped
class AskAnswerBotController: BaseBotController() {

    override var showName: String = "问答系统"

    companion object {
        private const val setAnswer = "设置问答"
        private const val seeAnswers = "查看问答"
    }

    @Resource
    private lateinit var answerService: AnswerService

    @RequestMapped
    fun getAnswer(messageChain: MessageChain): String? {
        val questions = messageChain.filterIsInstance<PlainText>()
        questions.forEach {
            val question = it.content
            val ans = answerService.getAnswer(question)
            if (ans != null && ans != "")
                return ans
            val vagueAns = answerService.getVagueAnswer(question)
            if (vagueAns.isEmpty())
                return@forEach
            if (vagueAns.size == 1) {
                return vagueAns[0].answer
            }
            val sb = StringBuilder()
            for (a in vagueAns)
                sb.append("问题[${a.question}]的答案是[${a.answer}]\n")
            return sb.toString()
        }
        return null
    }

    @RequestMapped("所有问答")
    fun all(): String {
        val sb = StringBuilder()
        val ans = answerService.getAnswers()
        val vag = answerService.getVagueAnswers()
        sb.append("精确问答：\n")
        ans.forEach { sb.append(it.question).append(" => ").append(it.answer).append('\n') }
        sb.append("模糊问答：\n")
        vag.forEach { sb.append(it.question).append(" => ").append(it.answer).append('\n') }
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