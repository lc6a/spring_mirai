package com.lc.spring_mirai.util

import com.google.gson.Gson
import org.springframework.stereotype.Component

/**
 * Json转换工具类
 */
@Component("defaultJsonUtil")
class JsonUtil {

    fun<T> fromJson(json: String, clazz: Class<T>): T{
        return Gson().fromJson(json, clazz)
    }

    fun toJson(obj: Any): String {
        return Gson().toJson(obj)
    }
}