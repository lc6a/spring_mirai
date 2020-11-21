package com.lc.spring_mirai.session

import com.lc.spring_mirai.util.BaseAttribute
import net.mamoe.mirai.contact.User
import java.util.*

//30 min
private const val sessionTimeOut = 30 * 60 * 1000L

open class Session(open val sender: User) : BaseAttribute() {
    var time: Long = 0
    init {
        time = Date().time
    }

    /**
     * 是否是此session
     */
    open fun isThis(id: Long) = sender.id == id && !isInvalid()

    /**
     * 是否失效
     */
    open fun isInvalid() = Date().time - this.time >= sessionTimeOut

}