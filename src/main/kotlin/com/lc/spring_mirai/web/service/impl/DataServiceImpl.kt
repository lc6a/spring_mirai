package com.lc.spring_mirai.web.service.impl

import com.google.gson.annotations.Expose
import com.lc.spring_mirai.util.JsonUtil
import com.lc.spring_mirai.web.dao.CtrlDao
import com.lc.spring_mirai.web.dao.CtrlDataDao
import com.lc.spring_mirai.web.entity.CtrlData
import com.lc.spring_mirai.web.service.DataService
import org.springframework.stereotype.Service
import javax.annotation.Resource

@Service
class DataServiceImpl: DataService {
    @Resource
    private lateinit var ctrlDataDao: CtrlDataDao

    @Resource
    private lateinit var ctrlDao: CtrlDao

    @Resource
    private lateinit var jsonUtil: JsonUtil

    /**
     * 缓存
     */
    protected val map = mutableMapOf<String, Any>()

    override fun<T> getData(dataName: String, clazz: Class<T>, ctrlName: String): T? {
        return if (map.containsKey(dataName)) {
            map[dataName] as T
        } else {
            val ctrlData = ctrlDataDao.findCtrlDataByCtrlAndName(ctrlDao.findCtrlByName(ctrlName)!!.id!!, dataName)
            if (ctrlData != null) {
                map[dataName] = jsonUtil.fromJson(dataName, clazz)!!
            }
            map[dataName] as T?
        }
    }

    override fun setData(dataName: String, data: Any, ctrlName: String) {
        val ctrlId = ctrlDao.findCtrlByName(ctrlName)!!.id!!
        val json = jsonUtil.toJson(data)
        val ctrlData = ctrlDataDao.findCtrlDataByCtrlAndName(ctrlId, dataName)
        if (ctrlData == null) {
            ctrlDataDao.addCtrlData(CtrlData(null, ctrlId, dataName, json))
        } else {
            ctrlDataDao.updateCtrlData(CtrlData(ctrlData.id, ctrlId, dataName, json))
        }
        map[dataName] = data
    }
}