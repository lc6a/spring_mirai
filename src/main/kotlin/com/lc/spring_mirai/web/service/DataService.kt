package com.lc.spring_mirai.web.service

interface DataService {
    fun<T> getData(dataName: String, clazz: Class<T>, ctrlName: String): T?
    fun setData(dataName: String, data: Any, ctrlName: String)
}