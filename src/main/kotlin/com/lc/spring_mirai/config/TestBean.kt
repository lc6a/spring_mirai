package com.lc.spring_mirai.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * 从配置文件获取指定bean实例的例子
 */
@Component
class TestBean {

    /**
     * 根据beanNameConfig类获取bean名称来注入的示例
     * 参数[test]为类名首字母小写（与yml文件里面key值保持一致）
     */
    @Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('test')}")
    private lateinit var test: Test

    fun test() {
        test.show()
    }
}