package com.lc.spring_mirai.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "spring-mirai.config")
class Config {
    var commonDeviceFilePath: String = "device.json"
    var rootUserId: Long = 1952511149L
    var autoOpenUrl: Boolean = true
}