package xyz.me4cxy.easyservice.processer.sql.appender;

import xyz.me4cxy.easyservice.processer.sql.SqlAndValue;

import java.util.Map;

/**
 * @author Jayin
 * @create 2020/10/30
 */
public class DefaultSqlAppender implements SqlAppender {
    private String sql;

    public DefaultSqlAppender(String sql) {
        this.sql = sql;
    }

    @Override
    public SqlAndValue getSqlAndValue(Map<String, Object> param) {
        return new SqlAndValue(false, sql, null);
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