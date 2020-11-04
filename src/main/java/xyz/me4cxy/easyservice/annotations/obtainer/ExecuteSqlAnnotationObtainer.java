package xyz.me4cxy.easyservice.annotations.obtainer;

import xyz.me4cxy.easyservice.annotations.ExecuteSql;

/**
 * @author Jayin
 * @create 2020/10/30
 */
public class ExecuteSqlAnnotationObtainer implements EasyServiceAnnotationObtainer {
    @Override
    public Class annotationType() {
        return ExecuteSql.class;
    }
}