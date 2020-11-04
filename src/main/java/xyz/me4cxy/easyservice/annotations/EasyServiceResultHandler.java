package xyz.me4cxy.easyservice.annotations;

import xyz.me4cxy.easyservice.result.ResultHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * api结果处理器.
 * @author Jayin
 * @create 2020/10/27
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EasyServiceResultHandler {
    /**
     * 结果处理器的类
     */
    Class<? extends ResultHandler> handlerClass();

    /**
     * 结果处理器的bean名称
     * @return
     */
    String handlerBean() default "";
}