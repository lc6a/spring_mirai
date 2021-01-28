package com.lc.spring_mirai.controller

import com.lc.spring_mirai.controller.function.BasedOnAnnotationFunc
import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.controller.function.FuncAnnotation
import com.lc.spring_mirai.controller.kclass.BasedOnAnnotationControllerClass
import com.lc.spring_mirai.controller.kclass.ControllerClass
import com.lc.spring_mirai.controller.kclass.CtrlAnnotation
import com.lc.spring_mirai.controller.parameter.Param
import com.lc.spring_mirai.controller.parameter.ParamAnnotation
import com.lc.spring_mirai.util.AnnotationUtil
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

open class ConfigOfController {

    /**
     * 返回参数实例
     */
    var getParamInstance: (KParameter, Any) -> Param = BasedOnAnnotation::getParamInstance

    /**
     * 返回控制器方法实例，如果不是控制器方法返回null
     */
    var getFunctionInstance: (KFunction<*>, Any, ConfigOfController) -> Func? = BasedOnAnnotation::getFunctionInstance

    /**
     * 返回控制器实例，如果不是控制器返回null
     */
    var getControllerClassInstance: (KClass<*>, Any, ConfigOfController) -> ControllerClass?
            = BasedOnAnnotation::getControllerClassInstance

}

internal object BasedOnAnnotation {


    fun getParamInstance(kp: KParameter, ctrl: Any): Param {
        val annotation = AnnotationUtil.getAnnotation(kp, ParamAnnotation::class) ?: return Param(kp, ctrl)
        return annotation.value.primaryConstructor!!.call(kp, ctrl) //根据注解的值实例化
    }

    fun getFunctionInstance(func: KFunction<*>, ctrl: Any, config: ConfigOfController): Func? {
        val params = ControllerFactory.createParams(func, ctrl, config)
        val annotation = AnnotationUtil.getAnnotation(func, FuncAnnotation::class)
        val ret = if (annotation == null)
            BasedOnAnnotationFunc(func, ctrl)
        else
            annotation.value.primaryConstructor!!.call(func, ctrl)
        if (!ret.isControllerFunc())
            return null
        ret.params = params
        return ret
    }

    fun getControllerClassInstance(clazz: KClass<*>, ctrl: Any, config: ConfigOfController): ControllerClass? {
        val annotation = AnnotationUtil.getAnnotation(clazz, CtrlAnnotation::class)
        val functions = ControllerFactory.createFunctions(clazz, ctrl, config)
        val ret = if (annotation == null)
            BasedOnAnnotationControllerClass(clazz, ctrl)
        else
            annotation.value.primaryConstructor!!.call(clazz, ctrl)
        if (!ret.isController())
            return null
        ret.functions = functions
        return ret
    }
}