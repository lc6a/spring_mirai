package com.lc.spring_mirai.web.entity

data class LayuiTable<T>(
    var code: Int,
    var msg: String,
    var count: Int,
    var data: List<T>
)
