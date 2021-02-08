/**
 * @Author 19525
 * @Date 2021/2/3 18:33
 */
package com.lc.spring_mirai.config;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.HashSet;
import java.util.Set;

public class SmSourceProcess extends AbstractProcessor {

    private Messager messager; //用于打印日志
    private Elements elementUtils; //用于处理元素
    private Filer filer;  //用来创建java文件或者class文件

    /**
     * 该方法再编译期间会被注入一个ProcessingEnvironment对象，该对象包含了很多有用的工具类。
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        elementUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
    }

    /**
     * 该方法将一轮一轮的遍历源代码
     * @param annotations  该方法需要处理的注解类型
     * @param roundEnv 关于一轮遍历中提供给我们调用的信息.
     * @return  该轮注解是否处理完成 true 下轮或者其他的注解处理器将不会接收到此类型的注解.用处不大.
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(SmResource.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
