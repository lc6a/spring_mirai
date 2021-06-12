package com.lc.spring_mirai.demo.test

import com.lc.spring_mirai.SpringMiraiApplication
import com.lc.spring_mirai.util.SpringApplicationContextUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component("defaultTestBean")
open class TestBean {
    open fun test() = "默认test"
}

@Component("test1")
class Test1Bean: TestBean() {
    override fun test(): String {
        return "覆写之后输出test1"
    }
}

@Component("test2")
class Test2Bean: TestBean() {
    override fun test(): String {
        return "覆写之后输出test2"
    }
}

fun main() {
    runApplication<SpringMiraiApplication>()
    val test = SpringApplicationContextUtil.context.getBean(Test::class.java)
    test.show()
}

@Component
class Test{
    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('testBean')}")
    lateinit var testBean: TestBean

    fun show() {
        println(testBean.test())
    }
}