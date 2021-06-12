package com.lc.spring_mirai.web.controller.bot

import com.lc.spring_mirai.annotation.BotController
import com.lc.spring_mirai.annotation.RequestMapped
import com.lc.spring_mirai.controller.function.Func
import com.lc.spring_mirai.controller.manager.CtrlManager
import com.lc.spring_mirai.controller.manager.CtrlRegister
import com.lc.spring_mirai.request.RequestFactory
import com.lc.spring_mirai.web.token.TokenUtil
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.event.events.MessageEvent
import javax.annotation.Resource

@BotController
@RequestMapped
class MenuBotController: BaseBotController() {

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('ctrlManager')}")
    private lateinit var ctrlManager: CtrlManager

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('ctrlRegister')}")
    private lateinit var ctrlRegister: CtrlRegister

    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('requestFactory')}")
    protected lateinit var requestFactory: RequestFactory
//    @Resource
//    private lateinit var includeExcludeFilterHandle: IncludeExcludeFilterHandle
//
//    @Resource
//    private lateinit var miraiConsolePermissionFilterHandle: MiraiConsolePermissionFilterHandle

    @Resource
    private lateinit var tokenUtil: TokenUtil

    private fun getFuncList(event: MessageEvent): List<Func> {
        val funcList = mutableListOf<Func>()
//         ctrlManager.ctrlList.filter {  // 类过滤
//             val data = FilterData(event, it.clazz.annotations, it, null)
//            runBlocking {
//                includeExcludeFilterHandle.filter(data)
//            } and runBlocking {
//                miraiConsolePermissionFilterHandle.filter(data)
//            }
//        }.forEach { ctrl ->
//             ctrl.functions.filter {    // 方法过滤
//                 val data = FilterData(event, it.kFunction.annotations, null, it)
//                runBlocking {
//                    includeExcludeFilterHandle.filter(data)
//                } and runBlocking {
//                    miraiConsolePermissionFilterHandle.filter(data)
//                }
//            }.also {
//                funcList.addAll(it)
//            }
//         }
        ctrlManager.ctrlList.forEach { ctrl ->
            ctrl.functions.forEach {
                // 过滤，无需关注具体过滤规则 权限、enable、过滤均在内
                if ( runBlocking {ctrlRegister.createFilter(it.ctrl.clazz.annotations, it.ctrl, null)(event)
                            && ctrlRegister.createFilter(it.kFunction.annotations, null, it)(event)}) {
                    funcList.add(it)
                }
            }
        }
        return funcList
    }

    @RequestMapped("菜单")
    fun ctrlList(event: MessageEvent): String {
//        val id = tokenUtil.parseToken(token).userId
//        val bot = Bot.instances.find {
//            it.id == id || it.getFriend(id) != null
//        } ?: return Result("账号没添加机器人为好友").toJson()
//        val event = FriendMessageEvent(bot.getFriendOrFail(id),
//            PlainText("empty").toMessageChain(), (Date().time / 1000).toInt())
        val funcList = getFuncList(event)
        val sb = StringBuilder()
        funcList.forEach { func ->
            func.ctrl.mappedItems.forEach{
                sb.append(it.toString()).append(' ')
            }
            func.mappedItems.forEach {
                sb.append(it.toString()).append(' ')
            }
            sb.append('\n')
        }
        return sb.toString()
    }

    override var showName: String = "菜单"
}