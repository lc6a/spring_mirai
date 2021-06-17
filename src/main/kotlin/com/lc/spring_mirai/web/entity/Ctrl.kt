package com.lc.spring_mirai.web.entity

import kotlinx.serialization.Serializable

@Serializable
data class Ctrl (
    var id: Int? = null,
    var showName: String,
    var enable: Boolean = true,
    var ctrlName: String,
    var managerUrl: String? = null
) {
    constructor(id: Int, showName: String, enableInt: Int, ctrlName: String)
        :this(id, showName, enableInt != 0, ctrlName, null)
}
