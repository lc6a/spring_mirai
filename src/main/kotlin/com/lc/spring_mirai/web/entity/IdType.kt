package com.lc.spring_mirai.web.entity

import kotlinx.serialization.Serializable

@Serializable
data class IdType(
    var id: Int? = null,
    var type: String
)

enum class IdTypes(val type: String) {
    bot("bot"),
    group("group"),
    friend("friend")
}