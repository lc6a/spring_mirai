package com.lc.spring_mirai

import com.lc.spring_mirai.config.Config
import com.lc.spring_mirai.config.ServerConfig
import com.lc.spring_mirai.util.SpringApplicationContextUtil
import com.lc.spring_mirai.util.URLOpener
import com.lc.spring_mirai.web.controller.execSmCommand
import com.lc.spring_mirai.web.token.TokenUtil
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.command.descriptor.*
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.permission.Permission
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.terminal.consoleLogger
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.buildMessageChain
import net.mamoe.mirai.utils.MiraiLogger
import net.mamoe.mirai.utils.debug
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import kotlin.reflect.typeOf

object SpringMiraiStartPlugin: KotlinPlugin(
    JvmPluginDescription(
        id = "com.lc.spring.mirai.SpringMiraiStartPlugin",
        version = "0.0.1",
        name = "SpringMirai"
    )
) {

    override fun PluginComponentStorage.onLoad() {
        runApplication<SpringMiraiApplication>()
        SpringApplicationContextUtil.context.getBeansOfType(PluginData::class.java).values.forEach {
            it.reload()
        }
        val config = SpringApplicationContextUtil.context.getBean(Config::class.java)
        if (config.autoOpenUrl) {
            val serverConfig = SpringApplicationContextUtil.context.getBean(ServerConfig::class.java)
            val url = serverConfig.url
            logger.info("管理页面地址：${url}")
            URLOpener.openURL(url)
        }
    }

    override fun onEnable() {
        logger.debug { "Start Spring Mirai" }
        SpringApplicationContextUtil.context.getBeansOfType(Command::class.java).values.forEach {
            logger.debug("注册命令：${it.primaryName}")
            CommandManager.registerCommand(it)
        }
    }

    override fun onDisable() {
        SpringApplicationContextUtil.context.getBeansOfType(Command::class.java).values.forEach {
            it.unregister()
        }
    }
}


@Component
class TokenCommand: SimpleCommand(
    SpringMiraiStartPlugin, "sm_token",
    description = "获取SpringMirai的Token"
) {
    @Autowired
    lateinit var tokenUtil: TokenUtil

    @Handler
    suspend fun CommandSender.handle() {
        sendMessage(tokenUtil.createDefaultToken())
    }
}


@Component
class SmCommandInvoker: SimpleCommand(
    SpringMiraiStartPlugin, primaryName,
    description = "执行SpringMirai指令"
) {

    companion object {
        private const val primaryName = "sm"
    }

    @Handler
    fun CommandSender.handle(vararg args: String) {
        val command = buildString { args.forEach { append(it).append(' ') } }.trimEnd()
        consoleLogger.debug("执行sm命令：${command}")
        execSmCommand(command) {
            runBlocking {
                sendMessage(it)
            }
        }
    }

//    override suspend fun CommandSender.onCommand(args: MessageChain) {
//        val command = args.content.substringAfter(primaryName).trimStart()
//        val logger = MiraiLogger.create("sm")
//        logger.info("message: ${args.content}")
//        logger.info("command: $command")
//        execSmCommand(command) {
//            runBlocking {
//                sendMessage(it)
//            }
//        }
//    }
}

abstract class MyRawCommand(
    /**
     * 指令拥有者.
     * @see CommandOwner
     */
    public override val owner: CommandOwner = SpringMiraiStartPlugin,
    public override val primaryName: String,
    /** 用法说明, 用于发送给用户 */
    public override val usage: String = "/sm sm指令内容",
    /** 指令描述, 用于显示在 [BuiltInCommands.HelpCommand] */
    public override val description: String = usage,
    /** 指令父权限 */
    parentPermission: Permission = owner.parentPermission,
    override val prefixOptional: Boolean = false,
    override val secondaryNames: Array<out String> = emptyArray()
) : Command {

    override val permission: Permission by lazy {
        val id = owner.permissionId("command.$primaryName")
        PermissionService.INSTANCE[id] ?: PermissionService.INSTANCE.register(id, description, parentPermission)
    }

    @OptIn(ExperimentalStdlibApi::class)
    @ExperimentalCommandDescriptors
    override val overloads: List<@JvmWildcard CommandSignature> = listOf(
        CommandSignatureImpl(
            receiverParameter = CommandReceiverParameter(false, typeOf<CommandSender>()),
            valueParameters = listOf(AbstractCommandValueParameter.UserDefinedType.createRequired<Array<out Message>>("args", true))
        ) { call ->
            val sender = call.caller
            val arguments = call.rawValueArguments
            //sender.onCommand(buildMessageChain { arguments.forEach { +it.value } })
            val message = buildString { arguments.forEach { append(it.value).append(' ') } }
            val logger = MiraiLogger.Factory.create(SpringMiraiStartPlugin::class, "sm-command")
            logger.info(message)
            sender.onCommand(buildMessageChain { +message })
        }
    )

    /**
     * 在指令被执行时调用.
     *
     * @param args 指令参数.
     *
     * @see CommandManager.execute 查看更多信息
     */
    public abstract suspend fun CommandSender.onCommand(args: MessageChain)
}
