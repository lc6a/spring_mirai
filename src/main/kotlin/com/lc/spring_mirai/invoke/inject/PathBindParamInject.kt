package com.lc.spring_mirai.invoke.inject

import com.lc.spring_mirai.annotation.Priority
import com.lc.spring_mirai.annotation.PriorityNum
import com.lc.spring_mirai.controller.parameter.Param
import com.lc.spring_mirai.controller.parameter.PathBindParam
import com.lc.spring_mirai.request.mapping.IMappingItem
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.FlashImage
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URL
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

/**
 * 路径绑定参数注入
 * @Author 19525
 * @Date 2021/2/7 22:10
 */
@Component
@Priority(PriorityNum.HIGHEST)
class PathBindParamInject: ParamInject {
    /**
     * 是否接受注入该参数
     * @param funcMappingItems 方法路径映射列表
     * @param param 参数
     */
    override fun accept(data: ParamInjectData): Boolean {
        return data.param is PathBindParam
    }

    override fun inject(data: ParamInjectData): Any? {
        val pathBindParam = data.param as PathBindParam
        return castType(data.funcMappingItems[pathBindParam.index].value, data.param.clazz)
    }

    /**
     * 根据参数类型来进行类型转换
     */
    protected fun<T : Any> castType(value: Any?, clazz: KClass<T>):T? {
        // 如果类型一致就直接返回
        if (value == null || clazz.isSuperclassOf(value::class)) return value as T?
        // 尝试强制转换   //强制转换 字符串 到 Long 仍然是字符串，并且不抛异常
//        try {
//            return value as T?
//        } catch (e: Exception) {}
        // 需要手动转换
        return when (value) {
            // 字符串类型
            is String -> {
                val tmp: Any? = when (clazz) {
                    Int::class -> value.toInt()
                    Long::class -> value.toLong()
                    Byte::class -> value.toByte()
                    Char::class -> value[0]
                    Float::class -> value.toFloat()
                    Double::class -> value.toDouble()
                    Boolean::class -> toBoolean(value)
                    At::class -> At(value.toLong())
                    else -> null
                }
                if (tmp != null)
                    return tmp as T?
                TODO()
            }
            is FlashImage, is Image -> {
                val image: Image = if (value is FlashImage) value.image else value as Image
                when (clazz) {
                    ByteArray::class -> {
                        return runBlocking {
                            return@runBlocking URL(image.queryUrl()).openStream().readAllBytes() as T
                        }
                    }
                    else -> TODO()
                }
            }
            else -> TODO()
        }
    }

    @Value("${'$'}{spring-mirai.true-strings:}")
    protected lateinit var trueStrings: Array<String>

    protected val defaultTrueStrings = arrayOf("on", "open", "ok", "打开", "开启", "启用", "授权", "确认")

    /**
     * 将字符串转换为Boolean
     * 这里通过配置文件获取一些表示true的字符串
     */
    protected fun toBoolean(value: String): Boolean {
        return try {
            value.toBoolean()
        } catch (e: Exception) {
            defaultTrueStrings.contains(value) || trueStrings.contains(value)
        }
    }
}
