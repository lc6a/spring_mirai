package com.lc.spring_mirai.util

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson

object JsonUtil {

    inline fun<reified T> fromJson(json: String): T{
        return Gson().fromJson(json)
    }

    fun toJson(obj: Any): String {
        return Gson().toJson(obj)
    }
}