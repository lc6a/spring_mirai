package com.lc.spring_mirai.web.entity

data class CtrlStatus(
    var ctrl: Ctrl,
    val ctrlIncludes: List<CtrlInclude>,
    val ctrlExcludes: List<CtrlExclude>
)