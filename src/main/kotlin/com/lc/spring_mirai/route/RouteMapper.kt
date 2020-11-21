package com.lc.spring_mirai.route

import com.lc.spring_mirai.SpringMiraiApplication
import com.lc.spring_mirai.annotation.NotEnd
import com.lc.spring_mirai.bean.Config
import com.lc.spring_mirai.exception.MapperException
import com.lc.spring_mirai.util.AnnotationUtil
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.content
import kotlin.reflect.KFunction
import kotlin.reflect.full.isSubclassOf

class RouteMapper private constructor(){

    lateinit var ctrl : Any
    lateinit var func: KFunction<*>
    lateinit var argc: String
    var route = mutableListOf<String>()

    /**
     * 深度优先
     */
    @Deprecated(message = "结构不太好", replaceWith = ReplaceWith("depthFirstSearch"))
    private fun depthFirstSearch_old(requestString: String, node: RouteNode,
                                 separatorChar: Char, escapeChar: Char,
                                 invokeFunc: (Any, KFunction<*>, List<String>, String) -> Unit): Boolean{
        if (requestString == "" || (requestString.length == 1 && requestString[0] == separatorChar)){
            if (node.function == null)
                return false
            ctrl = node.controller!!
            func = node.function!!
            argc = ""
            return true
        }
        var list = mutableListOf<String>()
        for (n in node.nextNodes){
            //最前一层路径
            val str = Route.instance.getStrBeforeSeparator(requestString, separatorChar, escapeChar)
            //本层成功映射
            if (isMapper(str, n)){
                argc = if (str.length + 1 < requestString.length)
                    requestString.substring(str.length + 1)
                else
                    ""
                route.add(str)
                //叶子节点，映射结束
                if (n.function != null){
                    //若有NotEnd注解，仍未结束
                    if (AnnotationUtil.getAnnotation(n.function!!, NotEnd::class) != null){
                        invokeFunc(n.controller!!, n.function!!, this.route, argc)
                    }else {
                        ctrl = n.controller!!
                        func = n.function!!
                        return true
                    }
                }
                    //映射未完成，接着映射
                if (depthFirstSearch_old(argc, n, separatorChar, escapeChar, invokeFunc)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 深度优先搜索
     */
    private fun depthFirstSearch(requestString: String, node: RouteNode,
                                 separatorChar: Char, escapeChar: Char,
                                 invokeFunc: (Any, KFunction<*>, List<String>, String) -> Unit): Boolean{
        //可执行节点，匹配成功
        if (node.function != null){
            /**
             * 如果有[NotEnd]注解，匹配仍将继续，在此次会直接调用可执行方法
             */
            if (AnnotationUtil.getAnnotation(node.function!!, NotEnd::class) != null){
                invokeFunc(node.controller!!, node.function!!, this.route, requestString)
            } else {
                //匹配终点，返回
                this.ctrl = node.controller!!
                this.func = node.function!!
                this.argc = requestString
                return true
            }
        }
        //如果请求字符串没有可分解的内容了，说明匹配失败，不必再匹配子节点
        if (requestString == "" || (requestString.length == 1 && requestString[0] == separatorChar)){
            return false
        }
        //获取一层路径
        val str = Route.instance.getStrBeforeSeparator(requestString, separatorChar, escapeChar)
        this.route.add(str)
        val routeIndex = this.route.lastIndex
        //匹配子节点
        for (n in node.nextNodes){
            //如果匹配成功
            if (isMapper(str, n)){
                //去掉本层路径str之后的请求字符串
                val requestStr = if (str.length + 1 < requestString.length)
                    requestString.substring(str.length + 1)
                else
                    ""
                //匹配下一层
                if (depthFirstSearch(requestStr, n, separatorChar, escapeChar, invokeFunc))
                    return true
            }
        }
        this.route.removeAt(routeIndex)
        return false    //所有子节点都无法匹配
    }

    /**
     * 将请求字符串映射到控制器的方法
     * [func]处理映射结果的函数
     */
    fun mapper(requestString: String, rootRouteNode: RouteNode, func: (Any, KFunction<*>, List<String>, String) -> Unit){
        val config = SpringMiraiApplication.config
        val separatorChar = config.separatorChar
        val escapeChar = config.escapeChar
        /*//判断根节点是否有方法  用重构后的深搜足够处理根节点,不需要在这里单独判断
        if (rootRouteNode.function != null){
            func(rootRouteNode.controller!!, rootRouteNode.function!!, emptyList(), requestString)
        }*/

        val ok = depthFirstSearch(requestString, rootRouteNode, separatorChar, escapeChar, func)
        if (!ok){
            throw MapperException("映射失败：${requestString}")
        }
        func(ctrl, this.func, route, argc)
    }

    companion object {

        public fun mapper(event: Event, func: (Any, KFunction<*>, List<String>, String, Event) -> Unit){
            val rootRouteNodes = Route.instance.getRootRouteNodes(event)
            if (rootRouteNodes.isEmpty())
                throw MapperException("没有接收${event::class}类型的控制器")
            val func1 : (Any, KFunction<*>, List<String>, String) -> Unit = {
                ctrl: Any, fu : KFunction<*>, routes: List<String>, argc: String->
                func(ctrl, fu, routes, argc, event)
            }
            for (rootRouteNode in rootRouteNodes) {
                try {
                    when {
                        event::class.isSubclassOf(MessageEvent::class) ->
                            instance.mapper((event as MessageEvent).message.content, rootRouteNode, func1)
                        else -> instance.mapper(event.toString(), rootRouteNode, func1)
                    }
                }catch (e: MapperException){
                    SpringMiraiApplication.springMiraiLogger.verbose(e.message)
                }
            }
        }

        val instance = RouteMapper()
        /**
         * 是否成功映射此节点
         */
        private fun isMapper(nodeName: String, node: RouteNode): Boolean {
            return Route.instance.routeNodeEqual(nodeName, node.routeName)
        }

        /**
         * 获取请求数据的分隔符
         * 默认两种分割符：' '或者'/'，转义字符为'/'
         */
        private fun getRequestSeparatorChar(requestString: String, separatorChars: List<Char>, escapeChar: Char): Char {
            var separatorChar = ' '
            var minIndex = -1
            for (se in separatorChars) {
                val index = Route.instance.getSeparatorIndex(requestString, separatorChar = se, escapeChar = escapeChar)
                if (minIndex == -1 || index < minIndex) {
                    minIndex = index
                    separatorChar = se
                }
            }
            return separatorChar
        }
    }

}