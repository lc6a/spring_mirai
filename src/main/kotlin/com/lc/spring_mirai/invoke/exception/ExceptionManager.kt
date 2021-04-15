package com.lc.spring_mirai.invoke.exception

import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.request.EventRequest
import com.lc.spring_mirai.util.beanSort.BeanSortUtil
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component("defaultExceptionManager")
class ExceptionManager {
    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('beanSortUtil')}")
    protected lateinit var beanSortUtil: BeanSortUtil

    protected val exceptionHandleList by lazy { beanSortUtil.sortBeans(ExceptionHandle::class.java) }

    fun exception(request: EventRequest, func: Func, exception: Exception) {
        if (exceptionHandleList.isEmpty()) {
            exception.printStackTrace()
            return
        }
        val data = ExceptionHandleData(exception, request, func)
        for (handle in exceptionHandleList) {
            handle.exception(data)
        }
    }
}