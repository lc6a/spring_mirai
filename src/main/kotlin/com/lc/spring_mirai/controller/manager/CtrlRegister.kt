package com.lc.spring_mirai.controller.manager

import com.lc.spring_mirai.controller.ctrl.ControllerClass
import com.lc.spring_mirai.controller.filter.FilterHandle
import com.lc.spring_mirai.controller.filter.FilterData
import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.invoke.EventHandle
import com.lc.spring_mirai.request.Request
import com.lc.spring_mirai.request.RequestFactory
import com.lc.spring_mirai.util.beanSort.BeanSortUtil
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.GlobalEventChannel
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * 用于注册控制器
 * @Author 19525
 * @Date 2021/2/8 18:46
 */
@Component("defaultCtrlRegister")
class CtrlRegister {

    /**
     * 将控制器注册到事件监听
     */
    fun register(ctrl: ControllerClass) {
        ctrl.functions.forEach { registerFunc(it) }
    }

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('eventHandle')}")
    protected lateinit var eventHandle: EventHandle

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('requestFactory')}")
    protected lateinit var requestFactory: RequestFactory

    /**
     * 将方法注册到事件监听
     */
    protected fun registerFunc(func: Func) {
        // 可能没初始化，用func::listener.isInitialized会报错，因此try一下
        try {
            func.listener.cancel()
        } catch (e: Exception){}
        val eventChannel = getEventChannel(func).filter { createFilter(func, requestFactory.createRequest(it))(it) }
        val listener = eventChannel.subscribeAlways<Event> { event ->
            eventHandle.onEvent(event, requestFactory.createRequest(event), func)
        }
        func.listener = listener
    }

    /**
     * 创建控制器类和方法的过滤器
     */
    protected fun createFilter(func: Func, request: Request):  suspend (Event) -> Boolean {
        return { event ->
            // 控制器类过滤
            createFilter(func.ctrl.clazz.annotations, func.ctrl, null)(event) &&
                    // 控制器方法过滤
                    createFilter(func.kFunction.annotations, null, func)(event) &&
                    // 请求路径过滤
                    createMappedFilter(func, request)(event)
        }
    }

    protected fun createMappedFilter(func: Func, request: Request): suspend (Event) -> Boolean {
        return { event ->
            val iterator = request.mappingItems.listIterator()
            func.ctrl.mapping(iterator) && func.mapping(iterator)
        }
    }

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('beanSortUtil')}")
    protected lateinit var beanSortUtil: BeanSortUtil

    protected val filters by lazy { beanSortUtil.sortBeans(FilterHandle::class.java) }

    /**
     * 在一堆注解里面创建过滤器
     */
    fun createFilter(annotations: List<Annotation>,ctrl: ControllerClass?, func: Func?):
            suspend (Event) -> Boolean{
        return  { event ->
            filters.all { it.filter(FilterData(event, annotations, ctrl, func)) }
        }
    }

    /**
     * 获取事件通道，后期或许会通过方法注解获取控制器类或者方法指定的事件通道
     */
    protected fun getEventChannel(func: Func): EventChannel<Event>{
        return GlobalEventChannel
    }
}