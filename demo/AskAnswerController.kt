package com.lc.miraispring.controller

import com.lc.spring_mirai.annotation.EventType
import com.lc.spring_mirai.annotation.NeedPermission
import com.lc.spring_mirai.annotation.NotEnd
import com.lc.spring_mirai.annotation.RequestMapper
import net.mamoe.mirai.message.GroupMessageEvent

private const val setAnswer = "设置问答"

@EventType(GroupMessageEvent::class)
@RequestMapper("", describe = "问答系统")
class AskAnswerController {
    //精确问答
    private val answers = mutableMapOf<Long, MutableMap<String, String>>()
    //模糊问答
    private val vagueAnswers = mutableMapOf<Long, MutableMap<String, String>>()

    @RequestMapper("{question}", describe = "将消息作为问题尝试回答")
    @NotEnd
    fun getAnswer(question: String, event: GroupMessageEvent): String?{
        val ans = answers[event.group.id]?.get(question)
        if (ans != null && ans != "")
            return ans
        val vagueAns = vagueAnswers[event.group.id]?.filterKeys { question.contains(it) }
        if (vagueAns == null || vagueAns.isEmpty())
            return null
        val sb = StringBuilder()
        for (a in vagueAns)
            sb.append("问题[${a.key}]的答案是[${a.value}]\n")
        return sb.toString()
    }

    @RequestMapper("问/{question}/答", describe = "设置精确问答")
    @NeedPermission(setAnswer)
    fun setAnswer(question: String, argc: String, event: GroupMessageEvent): String{
        return setAnswer(question, argc, answers, event.group.id)
    }

    @RequestMapper("模糊问/{question}/答", describe = "设置模糊问答")
    @NeedPermission(setAnswer)
    fun setVagueAnswer(question: String, argc: String, event: GroupMessageEvent): String{
        return setAnswer(question, argc, vagueAnswers, event.group.id)
    }

    /**
     * 设置问题的答案
     */
    private fun setAnswer(question: String, answer: String,
                          map: MutableMap<Long, MutableMap<String, String>>, groupId: Long): String{
        return if (answer.isEmpty()){
            map[groupId]?.remove(question)
            "已删除问题${question}的回答"
        }else{
            if (!map.containsKey(groupId))
                map[groupId] = mutableMapOf(question to answer)
            else
                map[groupId]!![question] = answer
            "已设置问题${question}的回答"
        }
    }
}