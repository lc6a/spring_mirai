package com.lc.spring_mirai.demo.service

interface PermissionService {
    companion object{
        /**
         * root权限，最高权限
         */
        const val root = "root"

        /**
         * 能够授予其他人权限的权限
         * 注： 非root用户只能授予自己拥有的权限，而且不包括[addPermission]和[delPermission]
         */
        const val addPermission = "addPermission"

        /**
         * 能够移除其他人权限的权限
         * 注：   非root用户只能移除自己拥有的权限，而且不包括[addPermission]和[delPermission]
         */
        const val delPermission = "delPermission"
    }
    /**
     * 用户[qqId]是否拥有[permission]权限
     * 注：root用户拥有所有权限
     */
    fun havePermission(qqId: Long, permission: String?) : Boolean

    /**
     * 授予[qqId]用户[permission]权限，操作发起者是[senderId]
     * 注： 非root用户只能授予自己拥有的权限，而且不包括[addPermission]和[delPermission]
     */
    fun addPermission(qqId: Long, permission: String, senderId: Long) : Boolean

    /**
     * 移除[qqId]用户[permission]权限，操作发起者是[senderId]
     * 注： 非root用户只能移除自己拥有的权限，而且不包括[addPermission]和[delPermission]
     */
    fun delPermission(qqId: Long, permission: String, senderId: Long) : Boolean

    /**
     * 获取[qqId]用户拥有的所有权限
     */
    fun getAllPermission(qqId: Long) : List<String>
}