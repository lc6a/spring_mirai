package com.lc.spring_mirai.web.entity

import kotlinx.serialization.Serializable

@Serializable
data class CtrlExclude(
    var ctrlId: Int,
    var idTypeId: Int,
    var id: Long
)
