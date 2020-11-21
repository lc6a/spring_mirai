package com.lc.spring_mirai.util


fun logFilter(str : String){
    val ignores = listOf("Heartbeat.Alive")
    for (ignore in ignores){
        if (str.contains(ignore))
            return
    }
    println(str)
}