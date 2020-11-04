package xyz.me4cxy.easyservice.annotations;

import xyz.me4cxy.easyservice.processer.ServiceProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * EasyApi注解处理器.
 * 注意，需要进行easy service处理的方法必须指定返回值，最方便的就是让Object做返回类型，否则AOP代理执行后返回结果仍会返回空或与实际结果不符
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EasyServiceProcessor {
    /**
     * 当前注解支持的业务处理器
     * @return
     */
    Class<? extends ServiceProcessor> value();
}
