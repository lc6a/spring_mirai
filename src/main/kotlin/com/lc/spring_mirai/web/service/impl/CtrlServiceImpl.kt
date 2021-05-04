package com.lc.spring_mirai.web.service.impl

import com.lc.spring_mirai.annotation.GroupFilter
import com.lc.spring_mirai.util.SpringApplicationContextUtil
import com.lc.spring_mirai.web.controller.bot.BaseBotController
import com.lc.spring_mirai.web.dao.CtrlDao
import com.lc.spring_mirai.web.dao.CtrlDataDao
import com.lc.spring_mirai.web.entity.*
import com.lc.spring_mirai.web.service.CtrlService
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.annotation.Resource

@Service
class CtrlServiceImpl: CtrlService {
    @Resource
    private lateinit var ctrlDao: CtrlDao

    @Resource
    private lateinit var ctrlDataDao: CtrlDataDao

    override fun getMap(ctrlMap: Map<String, BaseBotController>): Map<String, CtrlStatus>{
        val map = mutableMapOf<String, CtrlStatus>()
        ctrlMap.forEach { (t, u) ->
            map[t] = getCtrlStatus(t)
        }
        return map
    }

    @Transactional
    override fun getCtrlStatus(ctrlName: String): CtrlStatus {
        var ctrl = ctrlDao.findCtrlByName(ctrlName)
        val ctBean = SpringApplicationContextUtil.context.getBean(ctrlName) as BaseBotController
        if (ctrl == null) {
            ctrlDao.addCtrl(Ctrl(null, ctBean.showName, ctBean.enable, ctrlName))
            ctrl = ctrlDao.findCtrlByName(ctrlName)
            val ann = AnnotationUtils.findAnnotation(ctBean.javaClass, GroupFilter::class.java)
            if (ann != null) {
                val inc = ann.includeGroupId
                val exc = ann.ignoreGroupId
                inc.forEach {
                    ctrlDao.addCtrlInclude(CtrlInclude(ctrl!!.id!!, ctrlDao.findIdTypeByType(IdTypes.group.type)!!.id!!, it))
                }
                exc.forEach {
                    ctrlDao.addCtrlExclude(CtrlExclude(ctrl!!.id!!, ctrlDao.findIdTypeByType(IdTypes.group.type)!!.id!!, it))
                }
            }

        }
        ctrl!!.managerUrl = ctBean.managerUrl
        return CtrlStatus(ctrl, ctrlDao.findCtrlIncludes(ctrl.id!!), ctrlDao.findCtrlExcludes(ctrl.id!!))
    }

    override fun setEnable(enable: Boolean, ctrlName: String) {
        val ctrl = ctrlDao.findCtrlByName(ctrlName) ?: return
        ctrl.enable = enable
        ctrlDao.updateCtrlById(Ctrl(ctrl.id, ctrl.showName, enable, ctrlName))
    }

    override fun setShowName(showName: String, ctrlName: String) {
        val ctrl = ctrlDao.findCtrlByName(ctrlName) ?: return
        ctrl.showName = showName
        ctrlDao.updateCtrlById(Ctrl(ctrl.id, showName, ctrl.enable, ctrlName))
    }

    override fun setInclude(include: CtrlInclude) {
        if (!ctrlDao.hasCtrlInclude(include)) {
            ctrlDao.addCtrlInclude(include)
        }

    }

    override fun setExclude(exclude: CtrlExclude) {
        if (!ctrlDao.hasCtrlExclude(exclude)) {
            ctrlDao.addCtrlExclude(exclude)
        }
    }

    override fun removeInclude(include: CtrlInclude) {
        ctrlDao.deleteCtrlInclude(include)
    }

    override fun removeExclude(exclude: CtrlExclude) {
        ctrlDao.deleteCtrlExclude(exclude)
    }

    override fun commonFilter(id: Long, ctrlName: String, idTypes: IdTypes): Boolean {
        val ctrl = ctrlDao.findCtrlByName(ctrlName) ?: return false
        val ctrlId = ctrl.id!!
        val typeId = ctrlDao.findIdTypeByType(idTypes.type)!!.id!!
        val includes = ctrlDao.findCtrlIncludes(ctrlId)
        val excludes = ctrlDao.findCtrlExcludes(ctrlId)
        return if (includes.isEmpty()) {
            excludes.find { it.idTypeId == typeId && it.id == id } == null
        } else {
            includes.find { it.idTypeId == typeId && it.id == id } != null
        }
    }

}