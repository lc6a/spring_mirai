package com.lc.spring_mirai.web.service

import com.lc.spring_mirai.web.entity.Answer

interface AnswerService {

    fun getAnswers(): List<Answer>

    fun getVagueAnswers(): List<Answer>

    fun getAnswer(question: String): String?

    fun getVagueAnswer(question: String): List<Answer>

    fun setAnswer(answer: Answer)

    fun setVagueAnswer(answer: Answer)
}