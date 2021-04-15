package com.lc.spring_mirai.web.service

import com.lc.spring_mirai.web.entity.Answer

interface AnswerService {
    fun getAnswer(question: String): String?

    fun getVagueAnswer(question: String): List<Answer>

    fun setAnswer(answer: Answer)

    fun setVagueAnswer(answer: Answer)
}