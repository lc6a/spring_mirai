package com.lc.spring_mirai.web.dao

import com.lc.spring_mirai.util.IdUtil
import com.lc.spring_mirai.web.entity.CtrlData
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value
import org.springframework.stereotype.Component

//@Mapper
//interface CtrlDataDao {
//    @Insert("insert into ctrl_data values(#{id},#{ctrlId},#{dataName},#{data})")
//    fun addCtrlData(ctrlData: CtrlData)
//
//    @Select("select * from ctrl_data where id=#{id}")
//    fun findCtrlDataById(id: Int): CtrlData?
//
//    @Select("select * from ctrl_data where ctrl_id=#{ctrlId} and data_name=#{dataName}")
//    fun findCtrlDataByCtrlAndName(@Param("ctrlId") ctrlId: Int, @Param("dataName") dataName: String): CtrlData?
//
//    @Update("update ctrl_data set data=#{data} where id=#{id} or (ctrl_id=#{ctrlId} and data_name=#{dataName})")
//    fun updateCtrlData(ctrlData: CtrlData)
//}

@Component
class CtrlDataDao: AutoSavePluginData("ctrlData") {
    private val data: MutableList<CtrlData> by value()

    fun addCtrlData(ctrlData: CtrlData) {
        if (ctrlData.id == null) {
            ctrlData.id = IdUtil.createInt()
        }
        data.add(ctrlData)
    }

    fun findCtrlDataById(id: Int): CtrlData? {
        return data.find { it.id == id }
    }

    fun findCtrlDataByCtrlAndName(ctrlId: Int, dataName: String): CtrlData? {
        return data.find { it.ctrlId == ctrlId && it.dataName == dataName }
    }

    fun updateCtrlData(ctrlData: CtrlData) {
        data.removeIf { it.id == ctrlData.id }
        data.add(ctrlData)
    }
}