package xyz.me4cxy.easyservice.processer.sql;

import xyz.me4cxy.easyservice.processer.sql.appender.SqlAppender;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Jayin
 * @create 2020/10/30
 */
public class SqlAppendManager {
    private LinkedList<SqlAppender> appenders;

    public SqlAppendManager() {
        this.appenders = new LinkedList<>();
    }

    public void registerAppender(SqlAppender appender) {
        appenders.add(appender);
    }

    public SqlAndValue getSqlAndPlaceholderValue(Map<String, Object> argsMapping) {
        StringBuilder sb = new StringBuilder();
        List<Object> params = new LinkedList<>();
        Iterator<SqlAppender> iterator = appenders.iterator();
        while (iterator.hasNext()) {
            SqlAppender appender = iterator.next();
            // 获取sql和value值
            SqlAndValue sav = appender.getSqlAndValue(argsMapping);
            // 拼接sql
            sb.append(sav.getSql());
            // 添加参数对象
            if (sav.isHaveValue()) params.add(sav.getValue());
        }
        return new SqlAndValue(true, sb.toString(), params.toArray());
    }

}