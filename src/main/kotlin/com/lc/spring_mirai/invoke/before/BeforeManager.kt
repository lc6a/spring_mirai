package com.lc.spring_mirai.invoke.before

import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.request.EventRequest
import com.lc.spring_mirai.util.beanSort.BeanSortUtil
import org.springframework.stereotype.Component
import javax.annotation.Resource
import kotlin.reflect.KParameter

/**
 * 控制器方法处理前调用
 * @Author 19525
 * @Date 2021/2/7 21:04
 */
@Component("defaultBeforeManager")
class BeforeManager {

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('beanSortUtil')}")
    protected lateinit var beanSortUtil: BeanSortUtil

    protected val beforeList by lazy { beanSortUtil.sortBeans(BeforeHandle::class.java) }

    fun before(request: EventRequest, func: Func, args: Map<KParameter, Any?>) {
        val data = BeforeHandleData(request, func, args)
        for (before in beforeList) {
            before.before(data)
        }
    }
}