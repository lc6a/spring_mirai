package com.lc.spring_mirai.demo.service.impl

import com.lc.spring_mirai.demo.dao.PermissionDao
import com.lc.spring_mirai.demo.service.PermissionService
import com.lc.spring_mirai.demo.service.PermissionService.Companion.addPermission
import com.lc.spring_mirai.demo.service.PermissionService.Companion.delPermission
import com.lc.spring_mirai.exception.PermissionDeniedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("defaultPermissionService")
open class PermissionServiceImpl : PermissionService {
    @Autowired
    private lateinit var permissionDao: PermissionDao

    /**
     * 查找一条记录是否存在
     */
    private fun findUserPermission(qqId: Long, permission: String) =
            permissionDao.select(qqId, permission) != 0

    /**
     * 用户[qqId]是否拥有[permission]权限
     * 注：root用户拥有所有权限
     */
    override fun havePermission(qqId: Long, permission: String?) =
            permission == null || findUserPermission(qqId, PermissionService.root) || findUserPermission(qqId, permission)

    /**
     * 事务处理的一个例子，别调用这个
     * Transactional声明这个方法采用事务处理
     * 只要方法内抛出RuntimeException或者其子类，就回滚
     * 所以应该判断每个修改类操作是否正确执行，若没有就抛出异常（UserException是RuntimeException的子类）
     *
     * 给两个人授予某个权限，要么两个人都授予，要么都不授予。
     * 两个人就应该整整齐齐的，同生共死！
     */
//    @Transactional
//    open fun add2PersonPermission(qqId1: Long, qqId2: Long, permission: String){
//        val rootUser = 1952511149L
//        if (!addPermission(qqId1, permission, rootUser))
//            throw UserException("授权失败")
//        if (!addPermission(qqId2, permission, rootUser))
//            throw UserException("授权失败")
//    }

    /**
     * 确保拥有授予或者移除权限的权限
     * 权限不足抛出[PermissionDeniedException]异常
     */
    private fun haveAddOrDelPermission(qqId: Long, senderId: Long, permission: String, addOrDelPermission: String){
        if (!havePermission(senderId, PermissionService.root)){
            when{
                havePermission(qqId, PermissionService.root) ->
                    throw PermissionDeniedException("对方权限过高")
                addOrDelPermission == PermissionService.delPermission && havePermission(qqId, PermissionService.delPermission) ->
                    throw PermissionDeniedException("对方也拥有[${PermissionService.delPermission}]权限")
                permission == PermissionService.addPermission || permission == PermissionService.delPermission ->
                    throw PermissionDeniedException("你不能处理特殊权限[${permission}]")
                ! havePermission(senderId, permission) ->
                    throw PermissionDeniedException("你没有[${permission}]权限")
                ! havePermission(senderId, addOrDelPermission) ->
                    throw PermissionDeniedException("你没有[${addOrDelPermission}]权限")
            }
        }
    }

    /**
     * 授予[qqId]用户[permission]权限，操作发起者是[senderId]
     * 注： 非root用户只能授予自己拥有的权限，而且不包括[addPermission]和[delPermission]
     */
    override fun addPermission(qqId: Long, permission: String, senderId: Long): Boolean {
        haveAddOrDelPermission(qqId, senderId, permission, PermissionService.addPermission)
        return permissionDao.insert(qqId, permission) != 0
    }

    /**
     * 移除[qqId]用户[permission]权限，操作发起者是[senderId]
     * 注： 非root用户只能移除自己拥有的权限，而且不包括[addPermission]和[delPermission]
     */
    override fun delPermission(qqId: Long, permission: String, senderId: Long): Boolean {
        haveAddOrDelPermission(qqId, senderId, permission, PermissionService.delPermission)
        return permissionDao.delete(qqId, permission) != 0
    }

    /**
     * 获取[qqId]用户拥有的所有权限
     */
    override fun getAllPermission(qqId: Long): List<String> {
        return permissionDao.selectPermissions(qqId)
    }

}

//@Service
//class PermissionServiceImpl : IPermissionService{
//
//    @Autowired
//    private lateinit var iPermissionDao: IPermissionDao
//
//    /**
//     * 查找一条记录是否存在
//     */
//    private fun findUserPermission(qqId: Long, permission: String) =
//            iPermissionDao.select(qqId, permission) != 0
//
//    /**
//     * 用户[qqId]是否拥有[permission]权限
//     * 注：root用户拥有所有权限
//     */
//    override fun havePermission(qqId: Long, permission: String) =
//            findUserPermission(qqId, IPermissionService.root) || findUserPermission(qqId, permission)
//
//    /**
//     * 事务处理的一个例子，别调用这个
//     * Transactional声明这个方法采用事务处理
//     * 只要方法内抛出RuntimeException或者其子类，就回滚
//     * 所以应该判断每个修改类操作是否正确执行，若没有就抛出异常（UserException是RuntimeException的子类）
//     *
//     * 给两个人授予某个权限，要么两个人都授予，要么都不授予。
//     * 两个人就应该整整齐齐的，同生共死！
//     */
//    @Transactional
//    open fun add2PersonPermission(qqId1: Long, qqId2: Long, permission: String){
//        val rootUser = 1952511149L
//        if (!addPermission(qqId1, permission, rootUser))
//            throw UserException("授权失败")
//        if (!addPermission(qqId2, permission, rootUser))
//            throw UserException("授权失败")
//    }
//
//    /**
//     * 授予[qqId]用户[permission]权限，操作发起者是[senderId]
//     * 注： 非root用户只能授予自己拥有的权限，而且不包括[addPermission]和[delPermission]
//     */
//    override fun addPermission(qqId: Long, permission: String, senderId: Long): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    /**
//     * 移除[qqId]用户[permission]权限，操作发起者是[senderId]
//     * 注： 非root用户只能移除自己拥有的权限，而且不包括[addPermission]和[delPermission]
//     */
//    override fun delPermission(qqId: Long, permission: String, senderId: Long): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    /**
//     * 获取[qqId]用户拥有的所有权限
//     */
//    override fun getAllPermission(qqId: Long): List<String> {
//        TODO("Not yet implemented")
//    }
//
//}