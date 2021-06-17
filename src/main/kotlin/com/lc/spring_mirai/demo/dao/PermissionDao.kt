package com.lc.spring_mirai.demo.dao

import com.lc.spring_mirai.config.Config
import com.lc.spring_mirai.demo.service.PermissionService
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

//import org.apache.ibatis.annotations.*
//
//
//@Mapper
//interface PermissionDao {
//    @Insert("insert ignore into user_permission(qqId,permission) values(#{qqId}, #{permission})")
//    fun insert(@Param("qqId") qqId: Long, @Param("permission") permission: String) : Int
//    @Select("select count(qqId) from user_permission where qqId=#{qqId} and permission=#{permission}")
//    fun select(@Param("qqId") qqId: Long, @Param("permission") permission: String) : Int
//    @Select("select permission from user_permission where qqId=#{qqId}")
//    fun selectPermissions(qqId: Long) : List<String>
//    @Delete("delete ignore from user_permission where qqId=#{qqId} and permission=#{permission}")
//    fun delete(@Param("qqId") qqId: Long, @Param("permission") permission: String) : Int
//}

// 从mirai-console plugin data 获取数据
@Component
class PermissionDao @Autowired constructor(config: Config): AutoSavePluginData("permission") {
    val permissions: MutableMap<Long, MutableList<String>> by value()

    private val logger = LoggerFactory.getLogger(PermissionDao::class.java)

    init {
        val root = config.rootUserId
        initKey(root);
        if (permissions[root].isNullOrEmpty()) {
            logger.warn("当前没添加root用户权限，自动添加中")
            permissions[root]!!.add(PermissionService.root)
        }
    }

    private fun initKey(qqId: Long) {
        if (!permissions.containsKey(qqId)) {
            permissions[qqId] = mutableListOf()
        }
    }

    fun insert(qqId: Long, permission: String): Int {
        initKey(qqId)
        return if (kotlin.runCatching {
            permissions[qqId]!!.add(permission)
        }.getOrDefault(false)) 1 else 0
    }

    fun select(qqId: Long, permission: String): Int {
        initKey(qqId)
        return if (permissions[qqId]?.contains(permission) == true) 1 else 0
    }

    fun selectPermissions(qqId: Long): List<String> {
        initKey(qqId)
        return permissions[qqId]!!
    }

    fun delete(qqId: Long, permission: String): Int {
        initKey(qqId)
        return if (kotlin.runCatching {
            permissions[qqId]!!.remove(permission)
        }.getOrDefault(false)) 1 else 0
    }
}