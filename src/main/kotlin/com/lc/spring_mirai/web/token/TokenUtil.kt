package com.lc.spring_mirai.web.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.lc.spring_mirai.demo.service.PermissionService
import com.lc.spring_mirai.exception.PermissionDeniedException
import com.lc.spring_mirai.web.dto.Token
import com.lc.spring_mirai.web.error.TokenAuthExpiredException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.annotation.Resource
import javax.servlet.http.HttpServletResponse

private const val LOGIN_PERMISSION = "loginPermission"

@Component
class TokenUtil {

    @Resource
    private lateinit var permissionService: PermissionService

    @Resource
    var tokenUtil: TokenUtil? = null

    @Value("\${token.privateKey}")
    private val privateKey: String? = null

    @Value("\${token.yangToken}")
    private val yangToken: Long? = null

    @Value("\${token.oldToken}")
    private val oldToken: Long? = null

    /**
     * 加密token.
     */
    @JvmOverloads
    fun createToken(userId: Long, userRole: String? = "user"): String {
        //这个是放到负载payLoad 里面,魔法值可以使用常量类进行封装.
        return JWT
            .create()
            .withClaim("userId", userId)
            .withClaim("userRole", userRole)
            .withClaim("timeStamp", System.currentTimeMillis())
            .sign(Algorithm.HMAC256(privateKey))
    }

    /**
     * 在响应头设置token
     * @param httpServletResponse
     * @param token
     */
    fun setTokenHeader(httpServletResponse: HttpServletResponse, token: String?) {
        httpServletResponse.setHeader("token", token)
    }

    /**
     * 解析token.
     * (优化可以用常量固定魔法值+使用DTO在 mvc 之前传输数据，而不是 map,这里因为篇幅原因就不做了)
     * {
     * "userId": "3412435312",
     * "userRole": "ROLE_USER",
     * "timeStamp": "134143214"
     * }
     */
    fun parseToken(token: String?): Token {
        val decodedJwt = JWT.require(Algorithm.HMAC256(privateKey))
            .build().verify(token)
        val userId = decodedJwt.getClaim("userId")
        val userRole = decodedJwt.getClaim("userRole")
        val timeStamp = decodedJwt.getClaim("timeStamp")
        val token1 = Token()
        token1.userId = userId.asLong()
        token1.userRole = userRole.asString()
        token1.timeStamp = timeStamp.asLong()
        return token1
    }

    @Throws(TokenAuthExpiredException::class)
    fun checkToken(token: String?, response: HttpServletResponse) {
        if (null == token || "" == token.trim { it <= ' ' }) {
            throw TokenAuthExpiredException()
        }
        try {
            val token1 = parseToken(token)
            val userId = token1.userId
            val userRole = token1.userRole
            val timeOfUse = System.currentTimeMillis() - token1.timeStamp
            //1.判断 token 是否过期
            //年轻 token
            if (timeOfUse < yangToken!!) {

            } else if (timeOfUse >= yangToken && timeOfUse < oldToken!!) {
                response.setHeader("token", tokenUtil!!.createToken(userId, userRole))
            } else {
                throw TokenAuthExpiredException()
            }
            // 2. 是否具有访问权限
            if (!permissionService.havePermission(userId, LOGIN_PERMISSION)) {
                throw PermissionDeniedException("没有登录权限")
            }
        } catch (e: Exception) {
            throw TokenAuthExpiredException()
        }


        //        //2.角色匹配.
//        if ("user".equals(userRole)) {
//            return true;
//        }
//        if ("admin".equals(userRole)) {
//            return true;
//        }
//        return false;
    }
}