package com.lc.spring_mirai.controller.manager

import com.lc.spring_mirai.controller.ctrl.ControllerClass
import com.lc.spring_mirai.controller.factory.ControllerMappedFactory
import com.lc.spring_mirai.util.SpringApplicationContextUtil
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import javax.annotation.Resource

/**
 * 控制器管理类
 * @Author 19525
 * @Date 2021/2/8 18:25
 */
@Component("defaultCtrlManager")
class CtrlManager {
    protected val ctrlList = mutableListOf<ControllerClass>()

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('controllerMappedFactory')}")
    protected lateinit var controllerMappedFactory: ControllerMappedFactory

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('ctrlRegister')}")
    protected lateinit var ctrlRegister: CtrlRegister

    /**
     * 加载所有控制器
     */
    fun loadAllCtrl() = reloadAllCtrl()

    /**
     * 重新加载所有控制器
     */
    fun reloadAllCtrl() {
        val objs = getAllCtrlObj()
        for (obj in objs) {
            reloadCtrl(obj)
        }
    }

    /**
     * 获取所有控制器对象
     */
    protected fun getAllCtrlObj(): List<Any> {
        return SpringApplicationContextUtil.context.getBeansWithAnnotation(Controller::class.java).values.toList()
    }

    protected fun reloadCtrl(ctrlObj: Any) {
        val old = ctrlList.filter { it.clazz == ctrlObj::class }
        if (old.isNotEmpty()) {
            old.forEach { removeCtrl(it) }
        }
        val ctrl = controllerMappedFactory.createControllerClass(ctrlObj)
        if (ctrl != null)
            addCtrl(ctrl)
    }

    /**
     * 添加一个控制器
     */
    protected fun addCtrl(ctrl: ControllerClass) {
        if (ctrlList.contains(ctrl)) return
        ctrlRegister.register(ctrl)
        ctrlList.add(ctrl)
    }

    /**
     * 移除一个控制器
     */
    protected fun removeCtrl(ctrl: ControllerClass) {
        for (function in ctrl.functions) {
            if (function.listener.isActive) {
                function.listener.cancel()
            }
        }
        if (ctrlList.contains(ctrl)) {
            ctrlList.remove(ctrl)
        }
    }

}