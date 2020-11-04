package xyz.me4cxy.easyservice.processer.sql;

/**
 * @author Jayin
 * @create 2020/10/30
 */
public class SqlAndValue {
    private String sql;
    private Object value;
    private boolean haveValue;

    public SqlAndValue(boolean haveValue, String sql, Object value) {
        this.sql = sql;
        this.value = value;
        this.haveValue = haveValue;
    }

    public String getSql() {
        return sql;
    }

    public Object getValue() {
        return value;
    }

    public boolean isHaveValue() {
        return haveValue;
    }
}