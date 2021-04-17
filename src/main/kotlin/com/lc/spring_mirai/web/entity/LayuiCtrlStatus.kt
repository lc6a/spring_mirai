package com.lc.spring_mirai.web.entity

data class LayuiCtrlStatus(
    var ctrlName: String,
    var showName: String,
    var enable: Boolean,
    var includes: List<List<Long>>,
    var excludes: List<List<Long>>
) {
    companion object {
        fun fromCtrlStatus(status: CtrlStatus): LayuiCtrlStatus {
            val includes1 = listOf<MutableList<Long>>()
            for (ctrlInclude in status.ctrlIncludes) {
                includes1[ctrlInclude.idTypeId].add(ctrlInclude.id)
            }
            val excludes1 = listOf<MutableList<Long>>()
            status.ctrlExcludes.forEach {
                excludes1[it.idTypeId].add(it.id)
            }
            return LayuiCtrlStatus(status.ctrl.ctrlName,
                status.ctrl.showName, status.ctrl.enable,
                includes1, excludes1
            )
        }
    }
}
