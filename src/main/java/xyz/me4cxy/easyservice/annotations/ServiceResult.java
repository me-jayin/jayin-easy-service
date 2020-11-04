package xyz.me4cxy.easyservice.annotations;

import xyz.me4cxy.easyservice.processer.ServiceBeanServiceProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定接口直接返回的业务方法数据.
 * 注意，接口入参需要和执行的业务处理器方法入参一致
 * @author Jayin
 * @create 2020/10/24
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EasyServiceProcessor(value = ServiceBeanServiceProcessor.class)
public @interface ServiceResult {
    /**
     * 业务bean及方法的调用表达式
     * @return
     */
    String beanName() default "";

    /**
     * 待获取业务bean的类型
     * @return
     */
    Class beanClass() default Object.class;

    /**
     * 方法表达式
     * @return
     */
    String method() default "";
}