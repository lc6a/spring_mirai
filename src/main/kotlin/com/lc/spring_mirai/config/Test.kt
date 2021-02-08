package com.lc.spring_mirai.config

import org.springframework.stereotype.Component

interface Test {
    fun show()
}

@Component
class Test1: Test {
    override fun show() {
        println("test1")
    }
}

@Component
class Test2: Test {
    override fun show() {
        println("test2")
    }
}

@Component("defaultTest")
class DefTest: Test{
    override fun show() {
        println("default test")
    }

}