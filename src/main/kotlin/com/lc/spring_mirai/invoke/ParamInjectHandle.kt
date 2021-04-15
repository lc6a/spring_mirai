package com.lc.spring_mirai.invoke

import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.invoke.inject.exception.ParamInjectException
import com.lc.spring_mirai.invoke.inject.ParamInject
import com.lc.spring_mirai.invoke.inject.ParamInjectData
import com.lc.spring_mirai.request.EventRequest
import com.lc.spring_mirai.util.beanSort.BeanSortUtil
import org.springframework.stereotype.Component
import javax.annotation.Resource
import kotlin.reflect.KParameter

/**
 * 参数注入
 * @Author 19525
 * @Date 2021/2/7 20:59
 */
@Component("defaultParamInjectHandle")
class ParamInjectHandle {

    /**
     * 获取方法的参数注入数据
     */
    fun injectData(request: EventRequest, func: Func): Map<KParameter, Any?> {
        val funcMappingItems = request.mappingItems.subList(func.ctrl.mappedItems.size, request.mappingItems.size)
        val args = mutableMapOf<KParameter, Any?>()
        if (func.params.isEmpty())
            return args
        val params = if (func.params[0].clazz == func.ctrl.clazz) {
            args[func.params[0].kParameter] = func.ctrl.ctrlObj
            func.params.subList(1, func.params.size)
        } else {
            func.params
        }
        for (param in params) {
            args[param.kParameter] = getParamArgument(ParamInjectData(funcMappingItems, param, request.event))
        }
        return args
    }

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('beanSortUtil')}")
    protected lateinit var beanSortUtil: BeanSortUtil

    protected val paramInjects by lazy { beanSortUtil.sortBeans(ParamInject::class.java) }

    /**
     * 获取参数注入数据
     */
    protected fun getParamArgument(data: ParamInjectData): Any? {
        for (paramInject in paramInjects) {
            if (paramInject.accept(data)) {
                return paramInject.inject(data)
            }
        }
        throw ParamInjectException("无法注入参数${data.param}")
    }
}