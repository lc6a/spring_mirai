package com.lc.spring_mirai.web.entity

import com.lc.spring_mirai.util.JsonUtil

data class Result(
    var ok: Boolean,
    var msg: String? = null,
    var data: Any? = null
) {
    constructor(e: Exception): this(false, e.message)

    constructor(errorMsg: String): this(false, errorMsg)

    companion object {
        val success = Result(true)
        val unLogin = Result("unLogin")
        val tokenError = Result("token错误")
    }

    fun toJson(): String {
        return JsonUtil().toJson(this)
    }
}
