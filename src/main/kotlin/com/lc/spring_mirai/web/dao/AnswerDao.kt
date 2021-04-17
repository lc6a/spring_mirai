package com.lc.spring_mirai.web.dao

import com.lc.spring_mirai.web.entity.Answer
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface AnswerDao {
    @Insert("insert into answer values(#{question},#{answer},0)")
    fun addAnswer(answer: Answer)

    @Insert("insert into answer values(#{question},#{answer},1)")
    fun addVagueAnswer(answer: Answer)

    @Select("select answer from answer where question=#{question} and vague=0")
    fun findAnswer(question: String): String?

    @Select("select question,answer from answer where instr(#{question},question) and vague=1")
    fun findVagueAnswer(question: String): List<Answer>

    @Delete("delete from answer where question=#{question}")
    fun deleteAnswer(question: String)
}