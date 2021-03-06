package com.lc.spring_mirai.web.dto

import com.lc.spring_mirai.web.entity.CtrlStatus
import com.lc.spring_mirai.web.entity.IdTypes
import net.mamoe.mirai.console.plugin.description.PluginDescription

data class LayuiCtrlStatus(
    var ctrlName: String,
    var showName: String,
    var enable: Boolean,
    var includes: List<List<Long>>,
    var excludes: List<List<Long>>,
    var miraiPlugin: Boolean = false,
    var pluginDescription: PluginDescription? = null,
    var managerUrl: String? = null
) {
    companion object {
        fun fromCtrlStatus(status: CtrlStatus): LayuiCtrlStatus {
            val idTypesSize = IdTypes.values().size
            val includes1 = mutableListOf<MutableList<Long>>()
            for (i in 0..idTypesSize) {
                includes1.add(mutableListOf())
            }
            for (ctrlInclude in status.ctrlIncludes) {
                includes1[ctrlInclude.idTypeId].add(ctrlInclude.id)
            }
            val excludes1 = mutableListOf<MutableList<Long>>()
            for (i in 0..idTypesSize) {
                excludes1.add(mutableListOf())
            }
            status.ctrlExcludes.forEach {
                excludes1[it.idTypeId].add(it.id)
            }
            return LayuiCtrlStatus(status.ctrl.ctrlName,
                status.ctrl.showName, status.ctrl.enable,
                includes1, excludes1, managerUrl = status.ctrl.managerUrl
            )
        }
    }
}
