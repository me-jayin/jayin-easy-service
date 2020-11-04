package xyz.me4cxy.easyservice.processer.sql.appender;

import xyz.me4cxy.easyservice.processer.sql.SqlAndValue;

import java.util.Map;

/**
 * @author Jayin
 * @create 2020/10/30
 */
public class ConditionSqlAppender implements SqlAppender {

    @Override
    public SqlAndValue getSqlAndValue(Map<String, Object> param) {
        return null;
    }

    @Override
    public String paramName() {
        return null;
    }

    @Override
    public boolean needParam() {
        return false;
    }
}