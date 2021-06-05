package com.lc.spring_mirai.invoke

import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.invoke.after.AfterManager
import com.lc.spring_mirai.invoke.around.AroundManager
import com.lc.spring_mirai.invoke.before.BeforeManager
import com.lc.spring_mirai.invoke.exception.ExceptionManager
import com.lc.spring_mirai.request.EventRequest
import com.lc.spring_mirai.request.Request
import net.mamoe.mirai.event.Event
import org.slf4j.LoggerFactory
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
    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('aroundManager')}")
    protected lateinit var aroundManager: AroundManager
    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('exceptionManager')}")
    protected lateinit var exceptionManager: ExceptionManager

    val log = LoggerFactory.getLogger(this::class.java)
    /**
     * event发生后调用(对于每个控制器方法，将此方法添加给事件监听)
     */
    fun onEvent(event: Event, request: Request, func: Func) {
        if (request !is EventRequest)
            return
        log.warn("onEvent:$event")
        val args = paramInjectHandle.injectData(request, func)
        log.warn("inject $args")
        beforeManager.before(request, func, args)
        aroundManager.before(request, func, args)
        try {
            log.warn("before invoke")
            val ret = func.invokeNotSuspend(args)
            aroundManager.after(request, func, ret)
            afterManager.after(request, func, ret)
        } catch (e: Exception) {
            exceptionManager.exception(request, func, e)
        }
    }
}