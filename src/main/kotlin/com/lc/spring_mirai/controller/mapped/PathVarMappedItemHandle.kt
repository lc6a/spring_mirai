package com.lc.spring_mirai.controller.mapped

import com.lc.spring_mirai.annotation.PriorityNum
import com.lc.spring_mirai.annotation.Priority
import com.lc.spring_mirai.controller.exception.LoadControllerException
import com.lc.spring_mirai.controller.parameter.PathBindParam
import org.springframework.stereotype.Component
import java.lang.NumberFormatException

/**
 * 路径映射变量处理类，路径变量字符串语法：{name}或者{index}，名称需要与方法参数名一致，index从0开始，表示方法第几个参数
 * @Author 19525
 * @Date 2021/2/7 17:36
 */
@Component
@Priority(PriorityNum.DEFAULT)
class PathVarMappedItemHandle: MappedItemHandle {
    /**
     * 路径字符串mappedStr是否符合本handle的要求
     */
    override fun accept(data: MappedItemHandleData): Boolean {
        return try {
            handle(data)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 将mappedStr解析成指定IMappedItem
     * 此mappedStr一定是accept为true的
     */
    override fun handle(data: MappedItemHandleData): IMappedItem<*> {
        if (data.func == null) throw Exception()
        if (!data.mappedStr.matches("""^\{\w+}$""".toRegex())) {
            throw Exception()
        }
        val varName = data.mappedStr.substring(1, data.mappedStr.length - 1)
        var param = try {
            val i = varName.toInt()
            data.func.params[i + 1]  //第0个参数是所属类实例
        } catch (e: NumberFormatException) {
            try {
                data.func.params.first { it.kParameter.name == varName }
            } catch (e: NoSuchElementException) {
                throw LoadControllerException("无法将'${data.mappedStr}'解析为路径变量")
            }
        }
        if (param !is PathBindParam) {
            val pathBindParam = PathBindParam(param.kParameter, param.func, data.index)
            data.func.params.remove(param)
            data.func.params.add(pathBindParam)
            param = pathBindParam
        }
        return PathVarMappedItem(varName, param.clazz)
    }
}