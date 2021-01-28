package com.lc.spring_mirai.controller.mapped

import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.util.JsonUtil
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmName

object DefaultMappedItemFactory {
    /**
     * 通过字符串构造映射项，有以下n种构造规则：
     *
     * 1. 通用型：[MappedItemJson]的json字符串（太麻烦不建议使用），例如：
     * {"clazz":"java.lang.String","data":"abc","handle":"com.lc.spring_mirai.controller.mapped.UniversalMappedItem"}
     *
     * 2. 路径变量[PathVarMapped],格式：
     * {varName}
     *
     * 3. 正则表达式，格式：
     * [regexString]
     *
     * 4. 普通文本
     *
     */
    fun getMappedItem(string: String, func: Func?): IMappedItem<*> {
        //通用型
        try {
            val ij = JsonUtil.fromJson<MappedItemJson>(string)
            //似乎创建对象用不到这个
            val clazz = Class.forName(ij.clazz).kotlin
            val handle = Class.forName(ij.handle).kotlin
            return handle.primaryConstructor!!.call(ij.data) as IMappedItem<*>
        } catch (e: Exception){
        }
        //路径变量
        if (string.matches("""^\{\w+}$""".toRegex())){
            try {
                val varName = string.substring(1, string.length - 1)
                val param = func!!.params.filter { it.kParameter.name == varName }[0]
                return PathVarMapped(varName, param.clazz)
            } catch (e: Exception){
            }
        }
        //正则表达式
        if (string.matches("""^[\w+]$""".toRegex())) {
            try {
                val regex = string.substring(1, string.length - 1)
                return TextRegexMapped(regex)
            } catch (e: Exception) {

            }
        }
        //匹配文本
        return TextEqualMapped(string)
    }
}

/**
 * 通用映射项json模型
 * @param clazz 映射目标类型的完整类名[KClass.jvmName]
 * @param data 附带数据
 * @param handle 自定义[IMappedItem]类的完整类名[KClass.jvmName]
 */
data class MappedItemJson(
    val clazz: String,
    val data: String?,
    val handle: String
)

fun main() {
    val s = MappedItemJson(String::class.jvmName, "abc", UniversalMappedItem::class.jvmName)
    println(JsonUtil.toJson(s))
}