package com.lc.spring_mirai.web.controller.bot

/**
 * 由Web管理的控制器的基类，
 * 继承本类的控制器都能够自动被Web端进行图形界面方式管理
 *
 * [enable]属性决定该控制器是否开启，
 *
 * [showName]属性用于web端显示控制器的名称，最好能够体现出该控制器的功能
 *
 * [managerUrl]属性用于定义该控制器的专属管理页面url
 */
abstract class BaseBotController {

    open var enable: Boolean = true
    abstract var showName: String
    open var managerUrl: String? = null

}