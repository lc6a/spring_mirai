package com.lc.spring_mirai.web.dao

import com.lc.spring_mirai.web.entity.CtrlData
import org.apache.ibatis.annotations.*

@Mapper
interface CtrlDataDao {
    @Insert("insert into ctrl_data values(#{id},#{ctrlId},#{dataName},#{data})")
    fun addCtrlData(ctrlData: CtrlData)

    @Select("select * from ctrl_data where id=#{id}")
    fun findCtrlDataById(id: Int): CtrlData?

    @Select("select * from ctrl_data where ctrl_id=#{ctrlId} and data_name=#{dataName}")
    fun findCtrlDataByCtrlAndName(@Param("ctrlId") ctrlId: Int, @Param("dataName") dataName: String): CtrlData?

    @Update("update ctrl_data set data=#{data} where id=#{id} or (ctrl_id=#{ctrlId} and data_name=#{dataName})")
    fun updateCtrlData(ctrlData: CtrlData)
}