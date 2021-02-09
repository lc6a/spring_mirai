package com.lc.spring_mirai.controller.mapped

import com.lc.spring_mirai.annotation.PriorityNum
import com.lc.spring_mirai.annotation.Priority
import com.lc.spring_mirai.util.JsonUtil
import org.springframework.stereotype.Component
import javax.annotation.Resource
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * 通用性MappedItem处理类：[MappedItemJson]的json字符串（太麻烦不建议使用），例如：
 * {"clazz":"java.lang.String","data":"abc","handle":"com.lc.spring_mirai.controller.mapped.UniversalMappedItem"}
 *
 * @Author 19525
 * @Date 2021/2/7 17:20
 */
@Component
@Priority(PriorityNum.HIGHEST)
class UniversalMappedItemHandle: MappedItemHandle {

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('jsonUtil')}")
    protected lateinit var jsonUtil: JsonUtil
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

    protected lateinit var iMappedItem: IMappedItem<*>

    /**
     * 将mappedStr解析成指定IMappedItem
     * 此mappedStr一定是accept为true的
     */
    override fun handle(data: MappedItemHandleData): IMappedItem<*> {
        if (data.mappedStr.isEmpty()) throw Exception()
        //通用型
        val ij = jsonUtil.fromJson(data.mappedStr, MappedItemJson::class.java)
        //似乎创建对象用不到这个
        val clazz = Class.forName(ij.clazz).kotlin
        val handle = Class.forName(ij.handle).kotlin
        return handle.primaryConstructor!!.call(ij.data) as IMappedItem<*>
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