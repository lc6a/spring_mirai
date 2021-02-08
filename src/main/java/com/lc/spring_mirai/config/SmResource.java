
package com.lc.spring_mirai.config;

import org.springframework.core.annotation.AliasFor;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author 19525
 * @Date 2021/2/3 18:48
 */
@Target({TYPE, FIELD, METHOD})
@Retention(RUNTIME)
@Documented
public @interface SmResource {
    /**
     * 指定的bean类名首字母小写形式，默认变量名
     */
    String value() default "";
}
