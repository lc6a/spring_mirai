package com.lc.spring_mirai.web.entity

import kotlinx.serialization.json.Json

data class CtrlData(
    var id: Int? = null,
    var ctrlId: Int,
    var dataName: String,
    var data: String
)
