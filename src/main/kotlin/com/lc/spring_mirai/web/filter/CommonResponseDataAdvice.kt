package com.lc.spring_mirai.web.filter

import com.lc.spring_mirai.util.JsonUtil
import com.lc.spring_mirai.web.dto.Result
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import javax.annotation.Resource

@RestControllerAdvice
class CommonResponseDataAdvice : ResponseBodyAdvice<Any?> {

    @Resource
    private lateinit var jsonUtil: JsonUtil

    override fun supports(returnType: MethodParameter,
                          converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }


    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        return when (body) {
            is Unit, null -> Result.success
            is String -> body
            else -> jsonUtil.toJson(body)
        }
    }
}