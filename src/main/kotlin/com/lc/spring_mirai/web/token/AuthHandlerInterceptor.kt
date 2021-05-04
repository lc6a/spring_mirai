package com.lc.spring_mirai.web.token

import com.lc.spring_mirai.web.error.TokenAuthExpiredException
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthHandlerInterceptor : HandlerInterceptor {
    @Resource
    private lateinit var tokenUtil: TokenUtil

    /**
     * 权限认证的拦截操作.
     */
    @Throws(TokenAuthExpiredException::class)
    override fun preHandle(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        `object`: Any
    ): Boolean {
        // 如果不是映射到方法直接通过,可以访问资源.
        if (`object` !is HandlerMethod) {
            return true
        }
        val token = httpServletRequest.getParameter("token")
        try {
            tokenUtil.checkToken(token, httpServletResponse)
            return true
        } catch (e: Exception) {
            throw TokenAuthExpiredException()
        }
    }
}