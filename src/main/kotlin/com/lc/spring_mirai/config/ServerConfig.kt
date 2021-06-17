package com.lc.spring_mirai.config

import org.springframework.boot.web.context.WebServerInitializedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import java.net.InetAddress
import java.net.UnknownHostException

@Component
class ServerConfig : ApplicationListener<WebServerInitializedEvent> {
    private var serverPort = 0
    val url: String
        get() {
            var address: InetAddress? = null
            try {
                address = InetAddress.getLocalHost()
            } catch (e: UnknownHostException) {
                e.printStackTrace()
            }
            return "http://" + address!!.hostAddress + ":" + serverPort
        }

    override fun onApplicationEvent(event: WebServerInitializedEvent) {
        serverPort = event.webServer.port
    }
}