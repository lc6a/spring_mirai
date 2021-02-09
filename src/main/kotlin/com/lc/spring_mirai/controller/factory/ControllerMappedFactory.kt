package com.lc.spring_mirai.controller.factory

import com.lc.spring_mirai.annotation.RequestMapped
import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.controller.ctrl.ControllerClass
import com.lc.spring_mirai.controller.parameter.Param
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
import javax.annotation.Resource
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.javaMethod

/**
 * 控制器映射工厂
 * @Author 19525
 * @Date 2021/2/3 21:55
 */
@Component("defaultControllerMappedFactory")
class ControllerMappedFactory {

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('mappedFactory')}")
    protected lateinit var mappedFactory: MappedFactory

    /**
     * 通过控制器对象得到控制器内部实现，如果不是控制器返回null
     * @see ControllerClass
     */
    fun createControllerClass(ctrlObj: Any): ControllerClass? {
        if (!isController(ctrlObj)) return null
        // controller
        val controllerClass = ControllerClass(ctrlObj)
        val ctrlMapped = AnnotationUtils.findAnnotation(ctrlObj::class.java, RequestMapped::class.java)!!
        controllerClass.mappedItems = mappedFactory.createMapped(ctrlMapped.value, controllerClass, null)
        //function
        val funcList = mutableListOf<Func>()
        for (f in controllerClass.clazz.functions) {
            val func = createFunc(controllerClass, f)
            if (func != null) {
                funcList.add(func)
            }
        }
        controllerClass.functions = funcList
        return controllerClass
    }

    /**
     * 创建控制器方法
     */
    protected fun createFunc(ctrl: ControllerClass, f: KFunction<*>): Func? {
        val mapped = AnnotationUtils.findAnnotation(f.javaMethod!!, RequestMapped::class.java) ?: return null
        // params
        val func = Func(f, ctrl)
        val params = mutableListOf<Param>()
        for (p in f.parameters) {
            params.add(createParam(p, func))
        }
        func.params = params
        // mappedItems
        func.mappedItems = mappedFactory.createMapped(mapped.value, ctrl, func)
        return func
    }

    protected fun createParam(kp: KParameter, func: Func): Param {
        // todo
        return Param(kp, func)
    }

    /**
     * 判断ctrlObj对象是否是有效控制器
     */
    protected fun isController(ctrlObj: Any): Boolean {
        if (AnnotationUtils.findAnnotation(ctrlObj::class.java, RequestMapped::class.java) == null) {
            return false
        }
        for (func in ctrlObj::class.functions) {
            if (AnnotationUtils.findAnnotation(func.javaMethod!!, RequestMapped::class.java) != null) {
                return true
            }
        }
        return false
    }
}