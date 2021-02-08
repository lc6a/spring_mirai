package com.lc.spring_mirai.controller.mapped

import com.lc.spring_mirai.annotation.PriorityNum
import com.lc.spring_mirai.annotation.Priority
import org.springframework.stereotype.Component

/**
 * @Author 19525
 * @Date 2021/2/7 18:01
 */
@Component
@Priority(PriorityNum.LOWEST)
class TextEqualMappedItemHandle: MappedItemHandle {
    /**
     * 路径字符串mappedStr是否符合本handle的要求
     * @param mappedStr 路径字符串(一层)
     * @param index 路径字符串在注解中的层数，从0开始
     */
    override fun accept(data: MappedItemHandleData): Boolean {
        return data.mappedStr.isNotEmpty()
    }

    /**
     * 将mappedStr解析成指定IMappedItem
     * 此mappedStr一定是accept为true的
     */
    override fun handle(data: MappedItemHandleData): IMappedItem<*> {
        return TextEqualMappedItem(data.mappedStr)
    }
}