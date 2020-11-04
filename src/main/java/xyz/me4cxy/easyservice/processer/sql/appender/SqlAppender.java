package xyz.me4cxy.easyservice.processer.sql.appender;

import xyz.me4cxy.easyservice.processer.sql.SqlAndValue;

import java.util.Map;

public interface SqlAppender {
    String PREFIX = "${";
    String SUFFIX = "}";
    String MID_EQUAL = "=";

    SqlAndValue getSqlAndValue(Map<String, Object> param);

    String paramName();

    boolean needParam();

    default String getActualParamName(String name) {
//        return PREFIX + name + SUFFIX;
        return "?";
    }
}
