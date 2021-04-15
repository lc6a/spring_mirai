package com.lc.spring_mirai.invoke.after

import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.request.EventRequest
import com.lc.spring_mirai.util.beanSort.BeanSortUtil
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * @Author 19525
 * @Date 2021/2/7 21:09
 */
@Component("defaultAfterManager")
class AfterManager {

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('beanSortUtil')}")
    protected lateinit var beanSortUtil: BeanSortUtil

    protected val afterList by lazy { beanSortUtil.sortBeans(AfterHandle::class.java) }

    fun after(request: EventRequest, func: Func, ret: Any?) {
        val data = AfterHandleData(request, func, ret)
        for (after in afterList) {
            after.after(data)
        }
    }
}