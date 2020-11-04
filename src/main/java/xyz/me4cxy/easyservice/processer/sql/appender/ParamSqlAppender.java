package xyz.me4cxy.easyservice.processer.sql.appender;

import org.springframework.util.StringUtils;
import xyz.me4cxy.easyservice.exception.SqlProcessorException;
import xyz.me4cxy.easyservice.processer.sql.SqlAndValue;
import xyz.me4cxy.utils.FieldUtils;

import java.util.Map;

/**
 * @author Jayin
 * @create 2020/10/30
 */
public class ParamSqlAppender implements SqlAppender {
    private String actualParam;
    private String referenceParam;

    public ParamSqlAppender(String param) {
        this.resolveExpression(param);
    }

    /**
     * 解析参数表达式
     * @param param
     */
    protected void resolveExpression(String param) {
        this.checkParamExpression(param);
        if (param.startsWith(SqlAppender.PREFIX)) {
            param = param.substring(SqlAppender.PREFIX.length());
        }
        if (param.endsWith(SqlAppender.SUFFIX)) {
            param = param.substring(0, param.length() - SqlAppender.SUFFIX.length());
        }
        // 再次校验处理后的结果
        this.checkParamExpression(param);

        // 按 = 开始切割，左边为实际参数名，右边为数据引用关系
        String[] ary = param.split(SqlAppender.MID_EQUAL);
        if (ary == null)
            throw new SqlProcessorException("无效的sql参数，参数信息为空");

        // 判断分割后的长度来设置值
        switch (ary.length) {
            case 1:
                this.actualParam = this.referenceParam = ary[0].trim();
                break;
            case 2:
                this.actualParam = ary[0].trim();
                this.referenceParam = ary[1].trim();
                break;
            default:
                throw new SqlProcessorException("无效的sql参数表达式");
        }
    }

    /**
     * 校验参数表达式
     * @param param
     */
    private void checkParamExpression(String param) {
        if (StringUtils.isEmpty(param))
            throw new SqlProcessorException("无效的sql参数，参数信息为空");
    }

    @Override
    public SqlAndValue getSqlAndValue(Map<String, Object> param) {
        return new SqlAndValue(
                true, this.getActualParamName(this.actualParam),
                FieldUtils.getFieldValueByMap(param, this.referenceParam, false)
        );
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