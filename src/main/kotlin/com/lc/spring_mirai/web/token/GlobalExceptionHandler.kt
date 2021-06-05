package com.lc.spring_mirai.web.token

import com.lc.spring_mirai.util.JsonUtil
import com.lc.spring_mirai.web.dto.Result
import com.lc.spring_mirai.web.error.TokenAuthExpiredException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @Autowired
    private lateinit var jsonUtil: JsonUtil

    /**
     * 用户 token 过期
     * @return
     */
    @ExceptionHandler(value = [TokenAuthExpiredException::class])
    @ResponseBody
    fun tokenExpiredExceptionHandler(): String {
        return jsonUtil.toJson(Result.unLogin)
    }

    @ExceptionHandler(NotImplementedError::class)
    @ResponseBody
    fun notImplementedErrorHandle(error: NotImplementedError): String {
        //return jsonUtil.toJson(if (error.message != null) Result(error.message!!) else Result.notImplemented)
        return jsonUtil.toJson(error.message?.also { Result(it) } ?: Result.notImplemented)
    }
}