package com.lc.spring_mirai.session

import com.lc.spring_mirai.SpringMiraiApplication
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent

/**
 * session管理
 */
class SessionManager {

    companion object{
        val instance = SessionManager()
    }

    private val sessions = mutableSetOf<Session>()
    private val groupSessions = mutableSetOf<GroupSession>()

    /**
     * 根据事件返回Session，
     * 若没找到则创建新Session
     */
    fun getSession(event: MessageEvent): Session{
        if (event is GroupMessageEvent)
            return getGroupSession(event)
        val sendId = event.sender.id
        for (se in sessions){
            delSessionIfInvalid(se)
            if (se.isThis(sendId))
                return se
        }
        val newSe = Session(event.sender)
        sessions.add(newSe)
        return newSe
    }

    /**
     * 根据事件返回Session，
     * 若没找到则创建新Session
     */
    fun getGroupSession(event: GroupMessageEvent): GroupSession{
        val senderId = event.sender.id
        val groupId = event.group.id
        for (se in groupSessions){
            delSessionIfInvalid(se)
            if (se.isThis(senderId, groupId))
                return se
        }
        val newSe = GroupSession(event.sender, event.group)
        groupSessions.add(newSe)
        return newSe
    }

    /**
     * 删除失效session
     */
    private fun delSessionIfInvalid(session: Session){
        if (session.isInvalid()){
            if (session is GroupSession)
                groupSessions.remove(session)
            else
                sessions.remove(session)
            SpringMiraiApplication.springMiraiLogger.verbose("一个Session已失效：${session.sender.id}" +
                    if(session is GroupSession) "，群号：${session.group.id}" else "")
        }
    }
}