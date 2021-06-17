package com.lc.spring_mirai.web.entity

import kotlinx.serialization.Serializable

@Serializable
data class Answer(
    val question: String,
    val answer: String
)
