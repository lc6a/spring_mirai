package com.lc.spring_mirai.controller.mapped

import com.lc.spring_mirai.annotation.PriorityNum
import com.lc.spring_mirai.annotation.Priority
import org.springframework.stereotype.Component

/**
 * 正则表达式映射处理类，语法：[regexStr]
 * @Author 19525
 * @Date 2021/2/7 18:04
 */
@Component
@Priority(PriorityNum.DEFAULT)
class TextRegexMappedItemHandle: MappedItemHandle {
    /**
     * 路径字符串mappedStr是否符合本handle的要求
     * @param mappedStr 路径字符串(一层)
     * @param index 路径字符串在注解中的层数，从0开始
     */
    override fun accept(data: MappedItemHandleData): Boolean {
        return data.mappedStr.matches("""^\[.+]$""".toRegex())
    }

    /**
     * 将mappedStr解析成指定IMappedItem
     * 此mappedStr一定是accept为true的
     */
    override fun handle(data: MappedItemHandleData): IMappedItem<*> {
        val regex = data.mappedStr.substring(1, data.mappedStr.length - 1)
        return TextRegexMappedItem(regex)
    }
}