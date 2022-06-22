package com.lc.spring_mirai.config

import net.mamoe.mirai.utils.LoggerAdapters.asMiraiLogger
import net.mamoe.mirai.utils.MiraiLogger
import org.slf4j.LoggerFactory

class LoggerFactory : MiraiLogger.Factory {
    override fun create(requester: Class<*>, identity: String?): MiraiLogger {
        return LoggerFactory.getLogger(identity).asMiraiLogger()
    }
}