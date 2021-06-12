# SpringMirai中配版开发教程

2021-2-9
> 目前项目各功能都在开发中，因此暂不提供jar包，建议从git上clone或下载下来，
> 直接在本项目基础上开发。
> 开发请看主函数和demo包下的例子

> 本项目基于Spring Ioc容器，实现高度可定制、可扩展性。
> 
> 本项目理念：万物皆可自定义（几乎没有写死的功能）。
> 
> 因此，开发者可以使用内部默认实现来开发，当某功能不满足要求时，可自定义该功能。

## 目录
1. [基于内置默认功能开发](#基于内置默认功能开发)
    1. [控制器](#控制器)
    2. [控制器方法](#控制器方法)
    3. [路径映射](#路径映射)
    4. [过滤器](#过滤器)
    5. [控制器方法参数](#控制器方法参数)
    6. [控制器方法返回值](#控制器方法返回值)
2. [自定义](#自定义)
    1. [项目bean介绍](#项目bean介绍)
    2. [自定义唯一bean](#自定义唯一bean)
    3. [添加排序bean](#添加排序bean)
    4. [自定义排序bean](#自定义排序bean)

## 基于内置默认功能开发
> 内置默认功能都是基于注解、反射的方式实现的，使用者只需要了解注解的功能，不必了解实现细节
### 控制器
> 控制器是有`org.springframework.stereotype.Controller`注解的类。
> 
> 项目以控制器类作为加载的最小单位。
>
> 由于路径映射被设定为必需注解，
> 因此控制器类必须添加`com.lc.spring_mirai.annotation.RequestMapped`注解，
> 此注解复合了`@Controller`注解，因此可以不写`@Controller`注解
> 
> 示例：
> ```kotlin
> @Controller
> @RequestMapped
> class DemoController{
>     //代码......
> }
> ```
[回到目录](#目录)

### 控制器方法
> 控制器方法是控制器内部有`@RequestMapped`注解的方法。
> 
> 每一个控制器方法都会单独创建一个mirai事件监听。
> 
> 因此控制器方法可以看做`eventChannel.subscribeAlways{}`里面的内容

[回到目录](#目录)

### 路径映射
> 路径映射是`@RequestMapped`注解的值`value`，用于映射消息内容。
> 
> 因此非`MessageEvent`则不需要给注解设置值（但仍需要注解本身）。
> 
> `@RequestMapped`注解的值默认以`/`分割，文本消息默认以空格分割，
> 非文本消息每个单独占1层路径，不进一步分割。
> 
> `@RequestMapped`注解的每一层路径有以下几种内置类型(定义在`controller/mapped`包下)：
> 1. 通用型（不好用，不做介绍，有可能被弃用）
> 2. 绑定参数的路径，格式: {参数名}或{从0开始的参数位置}
> 3. 正则表达式，格式: [正则表达式] 注意反斜杠需要转义
> 4. 文本，格式：文本 需要消息匹配项与之完全相等
> 
> 如果需要扩展其他路径映射类型，可以参考教程目录第2章

[回到目录](#目录)

### 过滤器
> 过滤器是对事件通道进行过滤的，内置过滤器都是基于注解的。
> 
> 过滤器最终被组装成`eventChannel.filter{}`里面的内容。
> 
> 以下是几种内置过滤器(注解功能在`controller/filter`包里面实现):
> 1. `@EventFilter` 事件过滤，可以指定接收的事件类型
> 2. `@GroupFilter` 群过滤，可以指定包含的群号或忽略的群号（后期可能会调整成以before拦截器实现，方便运行时动态设置群过滤，并持久化）
> 3. `@PermissionFilter` 权限过滤，正在开发中

[回到目录](#目录)

### 控制器方法参数
> 控制器方法参数是方法获取数据的主要渠道之一。
> 
> 以下是几种内置的控制器方法参数(定义在`invoke/inject`包下):
> 1. 路径绑定参数，参数名需要与注解那里设置的保持一致。详见[路径映射](#路径映射)
> 2. 直接注入的参数，包含Spring的bean，Mirai的event、message、bot、group、user等等，见[参数注入源码](/src/main/kotlin/com/lc/spring_mirai/invoke/inject/SpringMiraiInject.kt)

[回到目录](#目录)

### 控制器方法返回值
> 控制器返回值一般在after后置拦截器里面处理。
> 
> 后置拦截器在控制器方法执行完返回后调用，能够获取到控制器方法的返回值。
> 
> 以下是将方法返回值作为消息发送出去的拦截器：
> 
> [用返回值回消息的源码](/src/main/kotlin/com/lc/spring_mirai/invoke/after/ReplyAfterHandle.kt)
>
> 在源码中可以看到，String或者各种Message类型都是可以转换成消息的，
> 并且是通过`util/ReplyUtil`回的消息，`ReplyUtil`回消息的方式是可以改变的，例如调整成匿名发送、发送时回复、发送时At对方等等。方便全局修改机器人行为。
> 
> 因此不建议在控制器方法内手动发送消息，而是以返回值的方式发送，或者给控制器注入ReplyUtil的bean。
> 
[回到目录](#目录)


## 自定义
### 项目bean介绍
> 项目各功能都是基于`@Component`注解的bean,并且都不是固定指定bean。
> 
> 根据同一类型的bean是否只有一个生效，项目的bean分为唯一bean和排序bean。
> 1. 唯一bean: 内置唯一bean的显著特点是bean名称都以default开头
> 2. 排序bean: 内置排序bean的特点是不指定bean名称，但有优先级注解`@Priority`，此注解可省略

[回到目录](#目录)

### 自定义唯一bean
> 唯一bean的默认名称是default加类名
> 1. 首先自定义bean继承旧的bean类，给自定义bean指定名称
> 2. 用指定bean的名称替换旧bean的名称。
> 
> 如果使用的默认`config/BeanNameConfig`，根据该配置的定义，只需要在`application.yml`文件里面指定
> ```yaml
> spring-mirai:
>     bean-name:
>        beanKey: beanName
> ```
> 即可。其中beanKey是类名首字母小写，beanName是新bean的名称，这样其他地方就会载入新的bean，而不是内置默认bean。
> 
> 如果使用的是其他的BeanNameConfig，根据其获取名称的方式来指定自己的bean名称。
> 


[回到目录](#目录)

### 添加排序bean
> 排序bean是某处需要某个类型的bean集合，不管是否有足够的必要去排序，由于排序只做一次，对性能影响不大，就都排一下序了。
> 
> 排序bean不需要bean名称，但需要优先级注解`@Priority`。
> 
> 因此添加排序bean时自定义类继承该类型，在类上面添加`@Component`、`@Priority`就行了。

[回到目录](#目录)

### 自定义排序bean
> 如果某个排序bean不符合要求，需要用新的bean替换该bean，首先要看其类型。
> 
> 排序bean分为单一生效排序bean和全生效排序bean。
> 
> * 单一生效排序bean的显著特点是类型包含`fun accept(...): Boolean`方法，当某个bean的该方法返回true，就只执行该bean。
> 根据其特点，新bean需要替换某个bean时，只需要优先级比旧bean高，新bean的accept的范围大于等于旧bean的accept就行。
>
> * 全生效排序bean没有accept方法，因此在排序前就需要进行bean替换，根据默认`util/BeanSortUtil`的实现，需要在新bean上添加`@Replace`注解，注解指定被替换的bean的class
> 
[回到目录](#目录)