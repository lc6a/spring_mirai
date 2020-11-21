# 开发教程


### 新建springboot项目

> 项目除了Spring和Mirai必选，其他都是可选的

> 项目内置了权限系统，建议添加Mybatis依赖，配置数据库。
> 如果没有数据库或者不想使用Mybatis，可以手动实现DAO层接口

> 开发语言建议用Kotlin，其次Java

> 项目管理工具建议使用Gradle

### 添加依赖

##### 下载并添加本项目build/libs下的jar包

[github仓库的jar](https://github.com/lc6a/spring_mirai/build/libs)

[gitee仓库的jar](https://gitee.com/lc6a/spring_mirai/build/libs)

* 在项目根路径建文件夹libs
* 修改build.gradle文件，在`dependencies`里添加一句代码
`compile fileTree(dir : 'libs', includes: ["*.jar"])`

#### 让Spring扫描本库的一些包
* 修改启动类上面的配置，让Spring扫描本库的一些包:

```
@SpringBootApplication(scanBasePackages = [
    "com.lc.spring_mirai.dao",          //jar包里面的
    "com.lc.spring_mirai.service",      //jar包里面的
    "com.lc.spring_mirai.controller",   //jar包里面的
    "com.lc.spring_mirai.bean",         //jar包里面的
    "com.xx.xxx"])        //自己项目的包
              //jar包里面的dao
@MapperScan("com.lc.spring_mirai.dao", "com.xx.xxx.dao")
```

说明：
`com.lc.spring_mirai.dao`和`com.lc.spring_mirai.service`包含权限系统的业务逻辑，如果不使用内置的dao和service，可以不扫描这些包，但需要自己实现相关接口并添加相关Spring注解。

`com.lc.spring_mirai.controller`内置了权限控制器和菜单控制器，可查看相关源码。菜单控制器的输出格式比较丑，可以自己写一个类似的控制器

`com.lc.spring_mirai.bean`内置一个配置内，可以在yml配置文件里面配置相关数据，或者不扫描此包，将其当做一个普通类手动传入数据也行

### 编写main函数
* 在主函数或者插件的加载函数初始化本库(插件的方式没有测试过）:

```
suspend fun main(args: Array<String>) {
    val context = runApplication<SpringApplication>(*args)
    val config = context.getBean(Config::class.java) //若扫描了配置类，可以调用Spring获取配置实例
    val bot = defaultCreateBot(config.qqId, config.password)
    SpringMiraiApplication().runApplication(context, bot, config)
    bot.join()
}
/**
 * 创建机器人
 */
private suspend fun defaultCreateBot(qqId: Long, password: String): Bot {
    return Bot(qqId, password) {
        fileBasedDeviceInfo()
        inheritCoroutineContext() // 使用 `coroutineScope` 的 Job 作为父 Job
        botLoggerSupplier = { bot ->
            PlatformLogger("Bot ${bot.id}", {
                logFilter(it)            //过滤不必要的日志
            }, false)
        }
        networkLoggerSupplier = { bot ->
            PlatformLogger("Net ${bot.id}", {
                logFilter(it)
            }, false)
        }
    }.alsoLogin()
}
```

从上面的代码可以看出，初始化本库需要提供` ConfigurableApplicationContext`、`Bot`、`Config`3个对象即可，后面无需任何与库代码直接关联的代码，除了注解。

### 使用注解
* `Controller`注解，这个是Spring的注解，但这里被本库代为管理，表明这是一个控制器。不过本库一些注解复合了此注解，因此可以不必写，但推荐控制器类以Controller结尾命名。

* `RequestMapper`注解，**注意**这个**不是Spring的RequestMapping注解**, 此注解用于标注控制器的类和方法，是路径映射必须有的注解，因此0层路由也请使用`@RequestMapper("")`，不可省略。此注解还包含describe可选参数，表明此命令的一段描述，可以是介绍命令的用法。

* `EventType`注解，此注解用于指定接受的事件类型，只能标注控制器类，这里子类事件可以被注解父类的控制器监听，例如`@EventType(MessageEvent::class)`标注的控制器能够接收群、好友、临时消息。

* `GroupFilter`注解，用于群过滤，此注解包含`@EventType(GroupMessageEvent::class)`注解，其拥有两个LongArray参数include、ignore用于过滤群号，两个参数只能生效一个，include参数表示只接受列表中的群，ignore参数表示接受列表以外所有的群。

* `NeedPermission`注解，用于声明发送消息者需要具备的权限，这里权限为字符串类型，此注解可以标注控制器类和方法。若消息发送者权限不足将发送消息提醒。

* `NotEnd`注解，只用于标注控制器的方法，表明事件被此方法处理后仍将继续进行路径映射，其他方法还有接收此事件的机会。

### 路径映射原理

* 库初始化时对于控制器类和方法进行了预处理，根据事件类型或群的不同建立了一个个“路径树”，随着群消息不断被接收，群树的数量会动态更新。关于路径树的细节可以不必关注。

* 事件到来后首先判断事件类型，注解此事件类型或其子类事件的路径树会进行路径匹配。

* 控制器和方法都注明`@RequestMapper("")`即0层路径时，其控制器方法在路径树根节点，该方法无条件监听到此事件，不需要进行路径映射。这类方法请注明NotEnd，否则该类型的事件都被此方法匹配到，其他方法没有机会监听到此事件。

* 接下来是对消息的内容与控制器类与方法注解的路径进行匹配。消息默认以空格作为层级分隔符，可以在Config中定义成其他字符，也可以使用转义字符表示当做普通字符，但一般用不到。消息**不会**一开始就通过分隔符都分开，而是像**剥洋葱**一样一层一层的分，分出来一层就匹配一层，匹配成功后剩余内容作为附加数据可以注入方法的参数。RequestMapper注解固定用'/'做分隔符。（开发人员没有自由，不能自定义！）

* 从上一步知道匹配路径是一层一层进行的，每一层匹配时遵循以下原则：
* * 固定路径需要完全匹配，例如“计算器”消息不会匹配`@RequestMapper("计算")`。
* * 占位符路径可以匹配非空字符串，例如“群禁言 开启”消息会匹配`@RequestMapper("群禁言/{flag}")fun test(flag: Boolean)`，并且“开启”这一层字符串会匹配{flag}占位符，并根据参数名注入flag参数，这里会转换为true，一些常用的表示肯定的词的集合会转换为true，在集合之外转换为flase，可以通过config扩充肯定词的集合。

* * 匹配到某一层时，如果此节点拥有控制器方法，则执行，如果注解了NotEnd将继续匹配，否则匹配结束，即使后面可能其他方法能够匹配。

* * 消息内容一层一层剥光了还没有匹配则匹配失败。

* * 路径映射是用纯字符串的方式处理，如果包含其他类型内容，可以考虑将其放到最后，或者直接在根节点处理。

### 参数注入

* 路径中的占位符会根据参数名进行注入，能够注入基本类型和字符串。

* 事件实例event能够被注入，这里根据事件类型进行判断。

* Session能够被注入，包含发送者，如果是群Session还包含群对象

* 附加数据会根据参数名进行注入，这里固定成"argc"，"args"这两个字符串作为附加数据可选的参数名，参数类型推荐为字符串。附加数据是消息在匹配路径时剩余的内容。

### 协程函数支持

* 控制器的方法可以是普通函数也可以是协程函数，库都能够正常调用。

### 控制器方法返回值处理

* 控制器方法可以没有返回值，也可以返回任意类型的返回值，但应该是可处理的。

* 返回Unit或者null时，不做处理。

* 返回非null的数据时，会尝试将其作为消息发送出去。因此建议返回字符串、各类Message、MessageChain。可以是可空类型，方便控制是否发送消息。


### 不影响其他库

* 虽然这里借助了Spring的Controller注解来获取控制器，只有包含EventType和RequestMapper这两个必选注解的控制器才被库使用，缺少这两个注解的控制器仍然能够作为Web控制器正常使用。

* 库的内置dao依赖于mybaits，但是可以不扫描该包，就不必使用mybatis。

### 目前存在的不足

* 设计之初没有考虑多bot，虽然在尝试改进，但仍建议只用单个bot。

* 没有对结构进行设计就直接开始写了本库，因此很多内容固定死了，扩展不太方便。

* 没有研究插件的开发形式，因此不清楚是否能够开发机器人插件。

* 没有接触过其他机器人框架，因此本库只基于Mirai框架，不考虑兼容其他框架。

* 没有考虑支持其他语言。


