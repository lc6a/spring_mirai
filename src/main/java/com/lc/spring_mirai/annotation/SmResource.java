package com.lc.spring_mirai.annotation;


import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SmResource {
    String value() default "";
}
