package com.lc.spring_mirai.util

import org.apache.commons.logging.LogFactory
import javax.swing.JOptionPane

/**
 * Title: ShoppingCart
 * Description:
 * Copyright: Copyright (c) 2007
 * Company: Sanntuu
 *
 * @author tommy
 * @version 1.0
 */
object URLOpener {
    private val log = LogFactory.getLog(URLOpener::class.java)
    fun openURL(url: String) {
        val osName = System.getProperty("os.name")
        try {
            if (osName.startsWith("Mac OS")) {
                val fileMgr = Class.forName("com.apple.eio.FileManager")
                val openURL = fileMgr.getDeclaredMethod("openURL", String::class.java)
                openURL.invoke(null, url)
            } else if (osName.startsWith("Windows")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler $url")
            } else { //assume Unix or Linux
                val browsers = arrayOf("firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape")
                var browser: String? = null
                var count = 0
                while (count < browsers.size && browser == null) {
                    if (Runtime.getRuntime().exec(arrayOf("which", browsers[count])).waitFor() == 0) {
                        browser = browsers[count]
                    }
                    count++
                }
                if (browser == null) {
                    throw Exception("Could not find web browser")
                } else {
                    Runtime.getRuntime().exec(arrayOf(browser, url))
                }
            }
        } catch (ex: Exception) {
            log.warn("打开浏览器时出错:" + ex.message)
            JOptionPane.showMessageDialog(
                null, "打开浏览器时出错:"
                        + ex.localizedMessage
            )
        }
    }
}