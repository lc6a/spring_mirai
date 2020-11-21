package com.lc.spring_mirai.bean

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class Config(
        @Value("${'$'}{botData.qqId}")
        val qqId: Long,
        @Value("${'$'}{botData.password}")
        val password : String,
        @Value("${'$'}{separator-char: }")
        val separatorChar : Char = ' ',
        @Value("${'$'}{escape-char:/}")
        val escapeChar: Char = '/',
        @Value("${'$'}{positive-words:}")
        val positiveWords: Set<String> = emptySet()
)