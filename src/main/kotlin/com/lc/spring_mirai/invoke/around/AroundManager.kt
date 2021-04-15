package com.lc.spring_mirai.invoke.around

import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.invoke.after.AfterHandleData
import com.lc.spring_mirai.invoke.before.BeforeHandleData
import com.lc.spring_mirai.request.EventRequest
import com.lc.spring_mirai.util.beanSort.BeanSortUtil
import org.springframework.stereotype.Component
import javax.annotation.Resource
import kotlin.reflect.KParameter

@Component("defaultAroundManager")
class AroundManager {
    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('beanSortUtil')}")
    protected lateinit var beanSortUtil: BeanSortUtil

    protected val aroundList by lazy { beanSortUtil.sortBeans(AroundHandle::class.java) }

    fun before(request: EventRequest, func: Func, args: Map<KParameter, Any?>) {
        val data = BeforeHandleData(request, func, args)
        for (before in aroundList) {
            before.before(data)
        }
    }

    fun after(request: EventRequest, func: Func, ret: Any?) {
        val data = AfterHandleData(request, func, ret)
        for (after in aroundList) {
            after.after(data)
        }
    }
}