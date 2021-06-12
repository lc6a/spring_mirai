package com.lc.spring_mirai.web.dto;

import java.util.*

class Token (
     var userId: Long,
     var userRole: String = "",
     var timeStamp: Long = Date().time
         ) {
    constructor() : this(1) {

    }
}


