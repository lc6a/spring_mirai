package com.lc.spring_mirai.controller

import com.lc.spring_mirai.SpringMiraiApplication
import com.lc.spring_mirai.annotation.EventType
import com.lc.spring_mirai.annotation.NeedPermission
import com.lc.spring_mirai.annotation.NotEnd
import com.lc.spring_mirai.annotation.RequestMapper
import com.lc.spring_mirai.route.Route
import com.lc.spring_mirai.util.AnnotationUtil
import net.mamoe.mirai.message.MessageEvent
import kotlin.reflect.KFunction
import kotlin.reflect.full.functions


@EventType(MessageEvent::class)
@RequestMapper("菜单", describe = "查看命令")
class MenuController {
    @RequestMapper("", describe = "查看所有命令")
    @NotEnd
    fun menu(event: MessageEvent): String{
        val sb = StringBuilder("----Spring_Mirai----\n")
        //获取会监听该事件的所有路径树    --------------------
        val rootNodes = Route.instance.getRootRouteNodes(event)
        for (rootNode in rootNodes) {
            val ctrls = Route.instance.getControllers(rootNode)
            for (ctrl in ctrls) {
                sb.append(getCtrlFuncMenu(ctrl))
            }
        }
        return sb.toString()
    }

    @RequestMapper("{cmd}", describe = "查看指定命令，未完成")
    fun menuCmd(event: MessageEvent, cmd: String){

    }

    /**
     * 得到一个方法的菜单
     */
    private fun getFuncMenu(func: KFunction<*>): StringBuilder{
        val requestAnnotation: RequestMapper = AnnotationUtil.getAnnotation(func, RequestMapper::class)
                ?: return StringBuilder()
        val sb = StringBuilder("\t子指令：").append(separatorCharReplace(requestAnnotation.value)).append('\n')
        sb.append(getNeedPermission(AnnotationUtil.getAnnotation(func, NeedPermission::class), "\t"))
        if (requestAnnotation.describe != "")
            sb.append("\t描述：").append(separatorCharReplace(requestAnnotation.describe)).append('\n')
        return sb
    }

    /**
     * 得到一个控制器的菜单
     */
    private fun getCtrlMenu(ctrl: Any): StringBuilder{
        val requestAnnotation: RequestMapper = AnnotationUtil.getAnnotation(ctrl::class, RequestMapper::class)
                ?: return StringBuilder()
        val sb =  StringBuilder("父指令：").append(separatorCharReplace(requestAnnotation.value)).append('\n')
        sb.append(getNeedPermission(AnnotationUtil.getAnnotation(ctrl::class, NeedPermission::class)))
        if (requestAnnotation.describe != "")
            sb.append("描述：").append(separatorCharReplace(requestAnnotation.describe)).append('\n')
        return sb
    }

    /**
     * 得到对于需要权限的描述
     */
    private fun getNeedPermission(needPermission: NeedPermission?, prefix: String = ""): StringBuilder{
        val sb = StringBuilder(prefix)
        needPermission?: return StringBuilder()
        val permission = needPermission.permission
        sb.append(" 需要[${permission}]权限\n")
        return sb
    }

    /**
     * 得到一个控制器及其方法的菜单
     */
    private fun getCtrlFuncMenu(ctrl: Any): StringBuilder{
        AnnotationUtil.getAnnotation(ctrl::class, RequestMapper::class)?:return StringBuilder()
        val escape = "--------------------\n"
        val ret = StringBuilder()
        ret.append(getCtrlMenu(ctrl))
        for (func in ctrl::class.functions){
            AnnotationUtil.getAnnotation(func, RequestMapper::class) ?:continue
            ret.append(getFuncMenu(func))
        }
        ret.append(escape)
        return ret
    }

    private fun separatorCharReplace(mapperStr: String): String{
        return mapperStr.replace('/', SpringMiraiApplication.config.separatorChar)
    }
}