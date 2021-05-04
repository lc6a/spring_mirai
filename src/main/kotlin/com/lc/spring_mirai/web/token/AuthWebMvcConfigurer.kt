package com.lc.spring_mirai.web.token

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import com.lc.spring_mirai.web.token.AuthHandlerInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import javax.annotation.Resource

@Configuration  //注释掉，暂时不用token
class AuthWebMvcConfigurer : WebMvcConfigurer {
    @Resource
    private lateinit var authHandlerInterceptor: AuthHandlerInterceptor

    /**
     * 给除了 /login 的接口都配置拦截器,拦截转向到 authHandlerInterceptor
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authHandlerInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns(
                "/token/check"
            )
    }
}