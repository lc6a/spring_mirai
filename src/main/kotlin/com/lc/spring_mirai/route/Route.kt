package com.lc.spring_mirai.route

import com.lc.spring_mirai.SpringMiraiApplication
import com.lc.spring_mirai.annotation.RequestMapper
import com.lc.spring_mirai.annotation.EventType
import com.lc.spring_mirai.annotation.GroupFilter
import com.lc.spring_mirai.util.AnnotationUtil
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.message.GroupMessageEvent
import org.springframework.stereotype.Controller
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSuperclassOf

class Route private constructor(){
    companion object{
        val instance: Route = Route()
    }

    /**
     * 根节点
     */
    private var rootRouteNodes = mutableMapOf<KClass<out Event>, RouteNode>()

    /**
     * 群号根节点
     */
    private var groupRootNodes = mutableMapOf<Long, RouteNode>()
    init {
        reloadControllers()
    }

    /**
     * 根据事件类型获取根节点，可能不止一个
     * 例如groupMessageEvent也会被MessageEvent, GroupEvent, Event等接收
     */
    fun getRootRouteNodes(event: Event): List<RouteNode> {
        var list = mutableListOf<RouteNode>()
        when(event){
            is GroupMessageEvent -> {
                if (groupRootNodes.containsKey(event.group.id))
                    list.add(groupRootNodes[event.group.id]!!)
                else
                    list.add(addGroupRootNode(event.group.id))
            }
        }
        for (rootNodePair in rootRouteNodes){
            if (rootNodePair.key.isSuperclassOf(event::class))
                list.add(rootNodePair.value)
        }
        return list
    }

    /**
     * 获取根节点，没有太大的实际意义，一般获取所有根节点
     * 请确保根节点存在
     */
    private fun getRootRouteNode(event: Event): RouteNode{
        if (event is GroupMessageEvent)
            return if (groupRootNodes.containsKey(event.group.id))
                groupRootNodes[event.group.id]!!
            else
                addGroupRootNode(event.group.id)
        return rootRouteNodes[event::class]!!
    }

    /**
     * 获取一个根节点下所有控制器
     */
    fun getControllers(rootRouteNode: RouteNode): Set<Any>{
        val set = mutableSetOf<Any>()
        getControllers(rootRouteNode, set)
        return set
    }

    /**
     * 深度优先递归获取所有控制器
     */
    private fun getControllers(node: RouteNode, set: MutableSet<Any>){
        if (node.controller != null){
            set.add(node.controller!!)
        }
        for (n in node.nextNodes){
            getControllers(n, set)
        }
    }

    /**
     * 重置并重新加载所有控制器
     */
    fun reloadControllers(){
        rootRouteNodes.clear()
        val controllers = getAllControllers()
        for (ctrl in controllers){
            if (AnnotationUtil.getAnnotation(ctrl::class, EventType::class)?.clazz == GroupMessageEvent::class)
                continue
            loadController(ctrl)
        }
        //群控制器统一加载
        reloadAllGroupController()
    }

    /**
     * 加载一个新的控制器，可能会产生ignore类型群控制器更新
     * 注：不可重复加载，旧的控制器数据不会删除或者更新
     * 实在要重新加载某个控制器，请用[reloadControllers]
     */
    fun loadController(ctrl: Any){
        val eventType = AnnotationUtil.getAnnotation(ctrl::class, EventType::class)
        val type : KClass<out Event> = eventType?.clazz ?: Event::class
        if (type == GroupMessageEvent::class){
            loadGroupController(ctrl)
            return
        }
        if (!rootRouteNodes.containsKey(type))
            rootRouteNodes[type] = RouteNode("${type.simpleName}类型根节点")
        val root = rootRouteNodes[type]!!
        loadController(ctrl, root)
    }

    /**
     * 重新加载所有群控制器
     */
    fun reloadAllGroupController(){
        groupRootNodes.clear()
        val controllers = getAllGroupControllers()
        val includes = mutableListOf<Any>()
        val ignores = mutableListOf<Any>()
        for (ctrl in controllers){
            val anno = AnnotationUtil.getAnnotation(ctrl::class, GroupFilter::class)
            when{
                anno == null -> ignores.add(ctrl)
                anno.includeGroupId.isNotEmpty() -> includes.add(ctrl)
                else -> ignores.add(ctrl)
            }
        }
        loadIncludeGroupCtrls(includes)
        loadIgnoreGroupCtrls(ignores)
    }

    /**
     * 初始化或者reloadAll时加载所有include类的群控制器
     * 不会更新ignore类的群控制器
     */
    private fun loadIncludeGroupCtrls(ctrls: List<Any>){
        for (ctrl in ctrls){
            val groups = AnnotationUtil.getAnnotation(ctrl::class, GroupFilter::class)!!.includeGroupId
            for (group in groups){
                val rootNode = addGroupRootNodeWithoutUpdate(group)
                loadController(ctrl, rootNode)
            }
        }
    }

    /**
     * 初始化或者reloadAll时加载所有ignore类型的群控制器
     */
    private fun loadIgnoreGroupCtrls(ctrls: List<Any>){
        for (ctrl in ctrls){
            val groups = AnnotationUtil.getAnnotation(ctrl::class, GroupFilter::class)?.ignoreGroupId ?: longArrayOf()
            for (root in groupRootNodes){
                if (!groups.contains(root.key)){
                    loadController(ctrl, root.value)
                }
            }
        }
    }

    //获取所有控制器
    private fun getAllControllers() =
            SpringMiraiApplication.context.getBeansWithAnnotation(Controller::class.java).values

    /**
     * 获取所有群控制器
     */
    private fun getAllGroupControllers() : List<Any> {
        val groupCtrls = mutableListOf<Any>()
        val ctrls = getAllControllers()
        for (ctrl in ctrls){
            val anno = AnnotationUtil.getAnnotation(ctrl::class, EventType::class)?:continue
            if (anno.clazz == GroupMessageEvent::class){
                groupCtrls.add(ctrl)
            }
        }
        return groupCtrls
    }

    /**
     * 新增一个群根节点，不影响其他控制器
     */
    private fun addGroupRootNodeWithoutUpdate(groupId: Long): RouteNode{
        if (groupRootNodes.containsKey(groupId))
            return groupRootNodes[groupId]!!
        val node = RouteNode("群[${groupId}]的根节点")
        groupRootNodes[groupId] = node
        return node
    }

    /**
     * 新增一个群根节点，并触发ignore群控制器更新
     */
    private fun addGroupRootNode(groupId: Long): RouteNode{
        addGroupRootNodeWithoutUpdate(groupId)
        val groupCtrls = getAllGroupControllers()
        loop@ for (ctrl in groupCtrls){
            val conn = AnnotationUtil.getAnnotation(ctrl::class, GroupFilter::class)
            var ignore = longArrayOf()
            when{
                conn == null -> {}
                conn.includeGroupId.isEmpty() -> ignore = conn.ignoreGroupId
                conn.includeGroupId.isNotEmpty() -> continue@loop
            }
            if (!ignore.contains(groupId)){
                loadController(ctrl, groupRootNodes[groupId]!!)
            }
        }
        return groupRootNodes[groupId]!!
    }

    /**
     * 加载新的群控制器，若新增群根节点，会触发ignore群控制器更新
     */
    fun loadGroupController(ctrl: Any){
        val groupFilter = AnnotationUtil.getAnnotation(ctrl::class, GroupFilter::class)
        val include = groupFilter?.includeGroupId ?: longArrayOf()
        val ignore = groupFilter?.ignoreGroupId ?: longArrayOf()
        if (include.isNotEmpty()){
            for (groupId in include){
                if (!groupRootNodes.containsKey(groupId)){
                    addGroupRootNode(groupId)
                }
                loadGroupController(ctrl, groupId)
            }
        } else {
            for (rt in groupRootNodes){
                if (!ignore.contains(rt.key)){  //不在忽视名单
                    loadController(ctrl, rt.value)
                }
            }
        }
    }

    /**
     * 将控制器加载到某群的路径树,若新增群根节点，会触发ignore群控制器更新
     */
    private fun loadGroupController(ctrl: Any, groupId: Long){
        if (!groupRootNodes.containsKey(groupId)){
            addGroupRootNode(groupId)
        }
        val root = groupRootNodes[groupId]!!
        loadController(ctrl, root)
    }


    /**
     * 加载一个任意的新控制器，不会影响其他控制器
     */
    private fun loadController(ctrl: Any, rootRouteNode: RouteNode){
        val cmdMapper = AnnotationUtil.getAnnotation(ctrl::class, RequestMapper::class) ?: return
        val cmdRoute = cmdMapperSplit(cmdMapper.value)
        val ctrlRouteNode = addControlRoute(cmdRoute, ctrl, rootRouteNode)
        val funcs = ctrl::class.functions  //控制器的所有方法
        for (func in funcs){
            val funcCmdMapper = AnnotationUtil.getAnnotation(func, RequestMapper::class) ?: continue
            val funcCmdRoute = cmdMapperSplit(funcCmdMapper.value)
            addFuncRoute(funcCmdRoute, ctrl, func, ctrlRouteNode)
        }
    }

    /**
     * 在控制台显示某个节点下的所有节点
     */
    fun showRouteNodes(node: RouteNode){
        if (node.controller != null)
            print("${node.routeName}包含控制器${node.controller!!::class.toString()} ")
        if (node.function != null)
            print("${node.routeName}包含方法${node.function} ")
        if (node.nextNodes.size == 0) {
            println()
            return
        }
        print("${node.routeName} 的子节点有")
        for (n in node.nextNodes){
            print("${n.routeName} ")
        }
        println()
        for (n in node.nextNodes)
            showRouteNodes(n)
    }


    /**
     * 添加一个控制器类的路径节点，返回最后一个节点
     */
    private fun addControlRoute(routeNodes: List<String>, ctrl: Any, rootRouteNode: RouteNode): RouteNode {
        if (routeNodes.isEmpty()){  //如果路径为空，直接把控制器加到父节点
            rootRouteNode.controller = ctrl
            return rootRouteNode
        }
        val r = RouteNode(routeNodes.last(), ctrl)
        addRouteNodes(routeNodes, r, rootRouteNode)
        return r

    }

    /**
     * 添加一个控制器方法的路径节点
     */
    private fun addFuncRoute(routeNodes: List<String>, ctrl: Any, func: KFunction<*>, ctrlNode: RouteNode){
        if (routeNodes.isEmpty()){  //如果路径为空，直接把方法加到父节点
            ctrlNode.controller = ctrl
            ctrlNode.function = func
            return
        }
        val r = RouteNode(routeNodes.last(), ctrl, func)
        addRouteNodes(routeNodes, r, ctrlNode)
    }

    /**
     * 添加路径节点
     */
    private fun addRouteNodes(routeNodes: List<String>, lastNode: RouteNode, startNode: RouteNode){
        var r = startNode
        for (i in routeNodes.indices){
            val node = r.nextNodes.find { it.routeName == routeNodes[i]}
            //第i个节点不存在
            if (node == null){
                //最后一个节点
                if (i == routeNodes.size - 1){
                    r.nextNodes.add(lastNode)
                    r = lastNode
                }else{
                    val tmpNode = RouteNode(routeNodes[i])
                    r.nextNodes.add(tmpNode)
                    //下一层
                    r = tmpNode
                }
            }else{
                //已经存在最后一个节点
                if (i == routeNodes.size - 1){
                    if (node.function != null)
                        throw Exception("路径节点重复：${routeNodes[i]}")
                    else{
                        node.controller = lastNode.controller
                        node.function = lastNode.function
                    }
                }else{
                    //下一层
                    r = node
                }
            }
        }
    }

    /**
     * 返回是否是占位符
     */
    fun isPlaceholder(string: String) =
            string.length > 1 && string[0] == '{' && string.last() == '}'

    /**
     * 通过名称判断两个路径节点是否相同
     */
    fun routeNodeEqual(nodeName1: String, nodeName2: String) =
            nodeName1 == nodeName2 || (isPlaceholder(nodeName1) || isPlaceholder(nodeName2))

    /**
     * 得到第一个分隔符的下标，分隔符为[separatorChar]例如"a/b"
     * 若转义[escapeChar]+[separatorChar]，不将其视为分隔符，例如："a//b"
     * 无分隔符返回-1
     */
    fun getSeparatorIndex(string: String, startIndex : Int = 0, separatorChar: Char = '/', escapeChar: Char = '/'): Int {
        val one = string.indexOf(separatorChar, startIndex)
        if (one == -1) return -1
        if (separatorChar == escapeChar) {
            if (string.length > one + 1 && string[one + 1] == escapeChar)  //下一个字符
                return getSeparatorIndex(string, one + 2, separatorChar, escapeChar)
        }else{
            if (one != 0 && string[one - 1] == escapeChar)  //前一个字符是转义字符
                return getSeparatorIndex(string, one + 1, separatorChar, escapeChar)
        }
        return one
    }

    /**
     * 获得第一个分隔符前面的内容并还原转义内容
     */
    fun getStrBeforeSeparator(string: String, separatorChar: Char = '/', escapeChar: Char = '/'): String{
        val index = getSeparatorIndex(string, separatorChar = separatorChar, escapeChar = escapeChar)
        val str: String
        str = if (index == -1)
            string
        else
            string.substring(0, index)
        return str.replace("$escapeChar$separatorChar", "$separatorChar")
    }

    /**
     * 字符串分割，获取路径各层列表，这里转义会被还原
     * 例如：“浏览/网页/http::////abc.com//ab"分割为["浏览","网页","http://abc.com/ab"]
     */
    private fun cmdMapperSplit(cmdMapperValue: String, separatorChar: Char = '/', escapeChar: Char = '/'): MutableList<String>{
        var value = cmdMapperValue
        val list = mutableListOf<String>()
        while (true){
            val first = getStrBeforeSeparator(value, separatorChar, escapeChar)
            if (first != "")
                list.add(first)
            if (first == value)
                break
            value = value.substring(first.length + 1)
        }
        return list
    }
}

data class RouteNode(
        var routeName: String,
        var controller: Any? = null,
        var function: KFunction<*>? = null,
        val nextNodes: MutableList<RouteNode> = mutableListOf()
)