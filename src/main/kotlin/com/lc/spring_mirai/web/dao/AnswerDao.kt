package com.lc.spring_mirai.web.dao

import com.lc.spring_mirai.web.entity.Answer
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value
import org.springframework.stereotype.Component

//import com.lc.spring_mirai.web.entity.Answer
//import org.apache.ibatis.annotations.Delete
//import org.apache.ibatis.annotations.Insert
//import org.apache.ibatis.annotations.Mapper
//import org.apache.ibatis.annotations.Select
//
//@Mapper
//interface AnswerDao {
//    @Insert("insert into answer values(#{question},#{answer},0)")
//    fun addAnswer(answer: Answer)
//
//    @Insert("insert into answer values(#{question},#{answer},1)")
//    fun addVagueAnswer(answer: Answer)
//
//    @Select("select answer from answer where question=#{question} and vague=0")
//    fun findAnswer(question: String): String?
//
//    @Select("select question,answer from answer where instr(#{question},question) and vague=1")
//    fun findVagueAnswer(question: String): List<Answer>
//
//    @Delete("delete from answer where question=#{question}")
//    fun deleteAnswer(question: String)
//
//    @Select("select question,answer from answer where vague=0")
//    fun getAllAnswers(): List<Answer>
//
//    @Select("select question,answer from answer where vague=1")
//    fun getAllVagueAnswer(): List<Answer>
//}

@Component
class AnswerDao: AutoSavePluginData("answer") {

    val answer: MutableMap<String, String> by value()
    val vagueAnswer: MutableMap<String, String> by value()

    fun addAnswer(answer: Answer) {
        this.answer[answer.question] = answer.answer
    }

    fun addVagueAnswer(answer: Answer) {
        this.vagueAnswer[answer.question] = answer.answer
    }

    fun findAnswer(question: String): String? {
        return answer[question]
    }

    fun findVagueAnswer(question: String): List<Answer> {
        return vagueAnswer.filterKeys { key -> question.contains(key) }.map { Answer(it.key, it.value) }
    }

    fun deleteAnswer(question: String) {
        answer.remove(question)
    }

    fun deleteVagueAnswer(question: String) {
        vagueAnswer.remove(question)
    }

    fun getAllAnswers(): List<Answer> {
        return answer.map { Answer(it.key, it.value) }
    }

    fun getAllVagueAnswer(): List<Answer> {
        return vagueAnswer.map { Answer(it.key, it.value) }
    }
}