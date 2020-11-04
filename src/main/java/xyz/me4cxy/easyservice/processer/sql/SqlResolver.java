package xyz.me4cxy.easyservice.processer.sql;

import xyz.me4cxy.easyservice.processer.sql.appender.DefaultSqlAppender;
import xyz.me4cxy.easyservice.processer.sql.appender.ParamSqlAppender;
import xyz.me4cxy.easyservice.processer.sql.appender.SqlAppender;

/**
 * @author Jayin
 * @create 2020/10/30
 */
public class SqlResolver {
    private static final char ESCAPE_CHARACTER = '/';
    private static final char PLACEHOLDER_BEGIN = '$';
    private static final char OPEN_CHARACTER = '{';
    private static final char CLOSE_CHARACTER = '}';

    public static SqlAppendManager resolveSql(String sql) {
        SqlAppendManager manager = new SqlAppendManager();

        // 记录 开口位置 和 关口位置 和 sql语句长度
        int openIndex = -1, defaultIndex = 0, length = sql.length();
        boolean placeholderBegin = false, beginOpen = false;
        char[] sqlChars = sql.toCharArray();
        char currentChar;
        for (int i = 0; i < length; i++) {
            currentChar = sqlChars[i];
            switch (currentChar) {
                case ESCAPE_CHARACTER: { // 如果是转义符则直接跳过下一个字符
                    i++;
                    continue;
                }
                case PLACEHOLDER_BEGIN: { // 如果是 $ 开始的则记录开始位置
                    placeholderBegin = true;
                    openIndex = i;
                    continue;
                }
                case OPEN_CHARACTER: { // 如果是 {
                    if (placeholderBegin && openIndex + 1 == i) {
                        beginOpen = true;
                        continue;
                    } else {
                        placeholderBegin = false;
                        beginOpen = false;
                    }
                }
                case CLOSE_CHARACTER: {
                    if (beginOpen) {
                        appenderDefaultSql(manager, sql, defaultIndex, openIndex);

                        String str = sql.substring(openIndex, i + 1);
                        SqlAppender appender = new ParamSqlAppender(str);
                        manager.registerAppender(appender);

                        placeholderBegin = false;
                        beginOpen = false;
                        defaultIndex = i + 1;
                    }
                }
            }
        }

        appenderDefaultSql(manager, sql, defaultIndex, openIndex);
        return manager;
    }

    private static void appenderDefaultSql(SqlAppendManager manager, String sql, int defaultIndexBegin, int openIndex) {
        manager.registerAppender(new DefaultSqlAppender(
                sql.substring(defaultIndexBegin, openIndex < 0 || defaultIndexBegin > openIndex ? sql.length() : openIndex))
        );
    }

//    public static void main(String[] args) {
//        SqlAppendManager m = resolveSql("select * from $${123 } AND username = ${ww2}");
//        SqlAndValue sav = m.getSqlAndPlaceholderValue(new HashMap() {{
//            this.put("123", 1);
//            this.put("ww2", "111");
//        }});
//        System.out.println(sav.getSql());
//        System.out.println(sav.getValue());
//    }
}