package com.lc.spring_mirai.session

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member

class GroupSession(override val sender: Member, val group: Group): Session(sender) {

    override fun isThis(id: Long): Boolean {
        return false
    }

    fun isThis(senderId: Long, groupId: Long): Boolean{
        return sender.id == senderId && this.group.id == groupId && !isInvalid()
    }
}