package xyz.me4cxy.easyservice.annotations;

import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用EasyApi的controller修饰注解
 * @author Jayin
 * @create 2020/10/24
 */
@RestController
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EasyService {

}