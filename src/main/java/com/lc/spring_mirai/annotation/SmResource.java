package com.lc.spring_mirai.annotation;


import java.lang.annotation.*;

/**
 * 本来打算用@SmResource(name)注解
 * 代替@Resource(name = "#{springMiraiBeanNameManager.beanNameConfig.getBeanName('name')}")
 * 避免每次都要写这么长一串注解参数，也不方便记忆
 * 并用一些技术在运行前替换回去，但未成功
 * 尝试过的技术方案有：
 * - javax.annotation.processing包的AbstractProcessor，编译期注解处理器，但未必能够胜任
 * - java.lang.instrument.Instrumentation，可以进行类替换等，但是对byte[]类型的字节码操作不熟悉
 * - ASM技术， 基于ASM应该是可以的，但目前没有足够时间学习如何使用
 *
 * 并且本注解并非必需的，没有的话只是麻烦一些，功能照常使用。
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface SmResource {
    String value() default "";
}
