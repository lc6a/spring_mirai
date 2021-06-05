package com.lc.spring_mirai.web.service.impl

import com.lc.spring_mirai.web.service.TestService
import org.springframework.stereotype.Service

@Service
class TestServiceImpl : TestService {
    override fun test(): String = "测试回复"
}