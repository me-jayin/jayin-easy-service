package xyz.me4cxy.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import xyz.me4cxy.easyservice.aop.EasyServiceAspect;

import javax.sql.DataSource;

/**
 * @author Jayin
 * @create 2020/10/28
 */
@Configuration
public class WebConfig {
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource datasource) {
        return new JdbcTemplate(datasource);
    }

    @Bean
    public EasyServiceAspect easyApiAspect() {
        return new EasyServiceAspect();
    }
}