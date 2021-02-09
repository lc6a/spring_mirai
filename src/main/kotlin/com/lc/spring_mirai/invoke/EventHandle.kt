package com.lc.spring_mirai.invoke

import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.invoke.after.AfterManager
import com.lc.spring_mirai.invoke.before.BeforeManager
import com.lc.spring_mirai.request.EventRequest
import com.lc.spring_mirai.request.Request
import net.mamoe.mirai.event.Event
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * @Author 19525
 * @Date 2021/2/7 20:36
 */
@Component("defaultEventHandle")
class EventHandle {

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('paramInjectHandle')}")
    protected lateinit var paramInjectHandle: ParamInjectHandle
    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('beforeManager')}")
    protected lateinit var beforeManager: BeforeManager
    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('afterManager')}")
    protected lateinit var afterManager: AfterManager

    /**
     * event发生后调用(对于每个控制器方法，将此方法添加给事件监听)
     */
    fun onEvent(event: Event, request: Request, func: Func) {
        if (request !is EventRequest)
            return
        val args = paramInjectHandle.injectData(request, func)
        beforeManager.before(request, func, args)
        val ret = func.invokeNotSuspend(args)
        afterManager.after(request, func, ret)
    }
}