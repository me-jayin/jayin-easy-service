package xyz.me4cxy.easyservice.processer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import xyz.me4cxy.easyservice.annotations.ExecuteSql;
import xyz.me4cxy.easyservice.aop.MethodInfoSignature;
import xyz.me4cxy.easyservice.exception.SqlProcessorAnnotationValueException;
import xyz.me4cxy.easyservice.exception.SqlProcessorException;
import xyz.me4cxy.easyservice.processer.sql.SqlAndValue;
import xyz.me4cxy.easyservice.processer.sql.SqlAppendManager;
import xyz.me4cxy.easyservice.processer.sql.SqlResolver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 预制sql执行处理器.
 * 要求引入spring jdbc，基于#{@link JdbcTemplate}实现
 * @author Jayin
 * @create 2020/10/30
 */
public class ExecuteSqlProcessor implements ServiceProcessor {
    private Logger logger = LoggerFactory.getLogger(ExecuteSqlProcessor.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    // sql破解管理器缓存
    private Map<String, SqlAppendManager> sqlAppenderManagerMap = new ConcurrentHashMap<>();

    @Override
    public Object doService(MethodInfoSignature mis, Map<String, Object> args) {
        ExecuteSql executeSql = (ExecuteSql) mis.getEasyApiAnnotation();
        String sql = executeSql.sql();
        if (StringUtils.isEmpty(sql)) {
            throw new SqlProcessorAnnotationValueException("请为方法【" + mis.getMethod() + "】中的@SqlResult指定sql语句");
        }

        SqlAppendManager manager = this.getSqlAppendManager(sql);
        SqlAndValue sav = manager.getSqlAndPlaceholderValue(args);

        if (logger.isDebugEnabled()) {
            logger.debug("执行easy service SQL查询操作，SQL语句：{}", sav.getSql());
        }
        Object result = jdbcTemplate.query(sav.getSql(), (Object[]) sav.getValue(), new BeanPropertyRowMapper<>(executeSql.resultType()));
        return result;
    }

    /**
     * 获取sql拼接管理器
     * @param sql
     * @return
     */
    public SqlAppendManager getSqlAppendManager(String sql) {
        SqlAppendManager manager = this.sqlAppenderManagerMap.get(sql);
        if (manager == null) {
            // 解析sql
            manager = SqlResolver.resolveSql(sql);
            if (manager == null) {
                throw new SqlProcessorException("SQL拼接管理器解析失败：" + sql);
            }
            // 添加缓存
            SqlAppendManager temp = this.sqlAppenderManagerMap.putIfAbsent(sql, manager);
            if (temp != null) return temp;
        }

        return manager;
    }

    @Override
    public boolean isSupport(MethodInfoSignature mis) {
        return ExecuteSql.class.isInstance(mis.getEasyApiAnnotation());
    }
}