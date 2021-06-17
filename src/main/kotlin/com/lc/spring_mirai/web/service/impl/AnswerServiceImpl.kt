package com.lc.spring_mirai.web.service.impl

import com.lc.spring_mirai.web.dao.AnswerDao
import com.lc.spring_mirai.web.entity.Answer
import com.lc.spring_mirai.web.service.AnswerService
import org.springframework.stereotype.Service
//import org.springframework.transaction.annotation.Transactional
import javax.annotation.Resource

@Service
class AnswerServiceImpl: AnswerService {

    @Resource
    private lateinit var answerDao: AnswerDao

    override fun getAnswers(): List<Answer> {
        return answerDao.getAllAnswers()
    }

    override fun getVagueAnswers(): List<Answer> {
        return answerDao.getAllVagueAnswer()
    }

    override fun getAnswer(question: String): String? {
        return answerDao.findAnswer(question)
    }

    override fun getVagueAnswer(question: String): List<Answer> {
        return answerDao.findVagueAnswer(question)
    }

    //@Transactional
    override fun setAnswer(answer: Answer) {
        answerDao.deleteAnswer(answer.question)
        if (answer.answer.isNotBlank()) {
            answerDao.addAnswer(answer)
        }
    }

    //@Transactional
    override fun setVagueAnswer(answer: Answer) {
        answerDao.deleteAnswer(answer.question)
        if (answer.answer.isNotBlank()) {
            answerDao.addVagueAnswer(answer)
        }
    }
}