package com.lc.spring_mirai.controller.kclass

import com.lc.spring_mirai.controller.annotation.Mapping
import com.lc.spring_mirai.controller.mapped.BasedOnAnnotationMapped
import com.lc.spring_mirai.controller.mapped.IMappedItem
import com.lc.spring_mirai.util.AnnotationUtil
import kotlin.reflect.KClass


open class BasedOnAnnotationControllerClass(
    override val clazz: KClass<*>,
    override val ctrlObj: Any
) : ControllerClass(clazz, ctrlObj) {
    /**
     * 判断其是否是控制器
     */
    override fun isController(): Boolean {
        return AnnotationUtil.getAnnotation(clazz, Mapping::class) != null
    }

    override val mappedItems: List<IMappedItem<*>> by lazy {
        requiredController()
        val annotation = AnnotationUtil.getAnnotation(clazz, Mapping::class)!!
        BasedOnAnnotationMapped.getMappedItems(annotation, null)
    }

}