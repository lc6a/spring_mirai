package com.lc.spring_mirai.web.dao

import com.lc.spring_mirai.util.IdUtil
import com.lc.spring_mirai.web.entity.Ctrl
import com.lc.spring_mirai.web.entity.CtrlExclude
import com.lc.spring_mirai.web.entity.CtrlInclude
import com.lc.spring_mirai.web.entity.IdType
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value
import org.springframework.stereotype.Component

//import com.lc.spring_mirai.web.entity.Ctrl
//import com.lc.spring_mirai.web.entity.CtrlExclude
//import com.lc.spring_mirai.web.entity.CtrlInclude
//import com.lc.spring_mirai.web.entity.IdType
//import org.apache.ibatis.annotations.*
//
//@Mapper
//interface CtrlDao {
//    @Insert("insert into ctrl values(#{id},#{showName},#{enable},#{ctrlName})")
//    fun addCtrl(ctrl: Ctrl)
//
//    @Select("select * from ctrl where id=#{id}")
//    fun findCtrlById(id: Int): Ctrl?
//
//    @Select("select * from ctrl where ctrl_name=#{ctrlName}")
//    fun findCtrlByName(ctrlName: String): Ctrl?
//
//    @Update("update ctrl set show_name=#{showName},enable=#{enable} where id=#{id}")
//    fun updateCtrlById(ctrl: Ctrl)
//
//    // id type
//    @Insert("insert into id_type values(#{id},#{type}")
//    fun addIdType(idType: IdType)
//
//    @Select("select * from id_type where id=#{id}")
//    fun findIdTypeById(id: Int): IdType?
//
//    @Select("select * from id_type where type=#{type}")
//    fun findIdTypeByType(type: String): IdType?
//
//    // include
//    @Insert("insert into ctrl_include values(#{ctrlId},#{idTypeId},#{id})")
//    fun addCtrlInclude(ctrlInclude: CtrlInclude)
//
//    @Select("select count(1) from ctrl_include where ctrl_id=#{ctrlId} and id_type_id=#{idTypeId} and id=#{id}")
//    fun hasCtrlInclude(ctrlInclude: CtrlInclude): Boolean
//
//    @Select("select * from ctrl_include where ctrl_id=#{ctrlId}")
//    fun findCtrlIncludes(ctrlId: Int): List<CtrlInclude>
//
//    @Delete("delete from ctrl_include where ctrl_id=#{ctrlId} and id_type_id=#{idTypeId} and id=#{id}")
//    fun deleteCtrlInclude(ctrlInclude: CtrlInclude)
//
//    // exclude
//    @Insert("insert into ctrl_exclude values(#{ctrlId},#{idTypeId},#{id})")
//    fun addCtrlExclude(ctrlExclude: CtrlExclude)
//
//    @Select("select count(1) from ctrl_exclude where ctrl_id=#{ctrlId} and id_type_id=#{idTypeId} and id=#{id}")
//    fun hasCtrlExclude(ctrlExclude: CtrlExclude): Boolean
//
//    @Select("select * from ctrl_exclude where ctrl_id=#{ctrlId}")
//    fun findCtrlExcludes(ctrlId: Int): List<CtrlExclude>
//
//    @Delete("delete from ctrl_exclude where ctrl_id=#{ctrlId} and id_type_id=#{idTypeId} and id=#{id}")
//    fun deleteCtrlExclude(ctrlExclude: CtrlExclude)
//}

@Component
class CtrlDao: AutoSavePluginData("ctrl") {

    private val ctrls: MutableList<Ctrl> by value()
    private val idTypes: MutableMap<Int, String> by value()
    private val includes: MutableList<CtrlInclude> by value()
    private val excludes: MutableList<CtrlExclude> by value()

    fun addCtrl(ctrl: Ctrl) {
        if (ctrl.id == null) {
            ctrl.id = IdUtil.createInt()
        }
        ctrls.add(ctrl)
    }

    fun findCtrlById(id: Int): Ctrl? {
        return ctrls.find { it.id == id }
    }

    fun findCtrlByName(ctrlName: String): Ctrl? {
        return ctrls.find { it.ctrlName == ctrlName }
    }

    fun updateCtrlById(ctrl: Ctrl) {
        ctrls.removeIf{it.id == ctrl.id}
        ctrls.add(ctrl)
    }

    fun addIdType(idType: IdType) {
        if (idType.id == null) {
            idType.id = IdUtil.createInt()
        }
        idTypes[idType.id!!] = idType.type
    }

    fun findIdTypeById(id: Int): IdType? {
        return idTypes[id]?.let { IdType(id, it) }
    }

    fun findIdTypeByType(type: String): IdType? {
        val t = idTypes.filter { it.value == type }.keys
        if (t.isEmpty()) return null
        return IdType(t.first(), type)
    }

    fun addCtrlInclude(ctrlInclude: CtrlInclude) {
        includes.add(ctrlInclude)
    }

    fun hasCtrlInclude(ctrlInclude: CtrlInclude): Boolean {
        return includes.contains(ctrlInclude)
    }

    fun findCtrlIncludes(ctrlId: Int): List<CtrlInclude> {
        return includes.filter { it.ctrlId == ctrlId }
    }

    fun deleteCtrlInclude(ctrlInclude: CtrlInclude) {
        includes.remove(ctrlInclude)
    }

    fun addCtrlExclude(ctrlExclude: CtrlExclude) {
        excludes.add(ctrlExclude)
    }

    fun hasCtrlExclude(ctrlExclude: CtrlExclude): Boolean {
        return excludes.contains(ctrlExclude)
    }

    fun findCtrlExcludes(ctrlId: Int): List<CtrlExclude> {
        return excludes.filter { it.ctrlId == ctrlId }
    }

    fun deleteCtrlExclude(ctrlExclude: CtrlExclude) {
        excludes.remove(ctrlExclude)
    }

}