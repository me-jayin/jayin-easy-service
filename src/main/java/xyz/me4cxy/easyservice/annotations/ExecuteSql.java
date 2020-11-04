package xyz.me4cxy.easyservice.annotations;

import xyz.me4cxy.easyservice.processer.ExecuteSqlProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EasyServiceProcessor(value = ExecuteSqlProcessor.class)
public @interface ExecuteSql {
    /**
     * 执行的sql语句
     * @return
     */
    String sql();

    /**
     * 返回的结果类型
     * @return
     */
    Class resultType();
}
