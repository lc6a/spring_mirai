package com.lc.spring_mirai.controller.function

import com.lc.spring_mirai.controller.annotation.Mapping
import com.lc.spring_mirai.controller.mapped.BasedOnAnnotationMapped
import com.lc.spring_mirai.controller.mapped.IMappedItem
import com.lc.spring_mirai.util.AnnotationUtil
import kotlin.reflect.KFunction

open class BasedOnAnnotationFunc(
    open override val kFunction: KFunction<*>,
    open override val ctrlObj: Any
) : Func(kFunction, ctrlObj) {

    override fun isControllerFunc(): Boolean {
        return AnnotationUtil.getAnnotation(kFunction, Mapping::class) != null
    }

    /**
     * 获取其[IMappedItem]列表，如果0层路径返回[emptyList]
     */
    override val mappedItems: List<IMappedItem<*>> by lazy {
        requiredControllerFunc()
        val annotation = AnnotationUtil.getAnnotation(kFunction, Mapping::class)!!
        BasedOnAnnotationMapped.getMappedItems(annotation, this)
    }

}