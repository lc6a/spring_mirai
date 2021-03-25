package com.lc.spring_mirai.demo.dao

import org.apache.ibatis.annotations.*


@Mapper
interface IPermissionDao {
    @Insert("insert ignore into user_permission(qqId,permission) values(#{qqId}, #{permission})")
    fun insert(@Param("qqId") qqId: Long, @Param("permission") permission: String) : Int
    @Select("select count(qqId) from user_permission where qqId=#{qqId} and permission=#{permission}")
    fun select(@Param("qqId") qqId: Long, @Param("permission") permission: String) : Int
    @Select("select permission from user_permission where qqId=#{qqId}")
    fun selectPermissions(qqId: Long) : List<String>
    @Delete("delete ignore from user_permission where qqId=#{qqId} and permission=#{permission}")
    fun delete(@Param("qqId") qqId: Long, @Param("permission") permission: String) : Int
}