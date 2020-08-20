package com.example.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author wangkaijin
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "four.datasource")
@MapperScan(basePackages = {"com.example.four.mapper"}, sqlSessionTemplateRef = "fourSqlSessionTemplate")
public class FourDataSourceConfig {

    private String driverClassName;

    private String username;

    private String password;

    private String jdbcUrl;

    /**
     * prefix值必须是application.properteis中对应属性的前缀
     */
    @Bean(name = "fourDataSource")
    public DataSource fourDataSource() throws SQLException {
//        DruidXADataSource druidXADataSource = new DruidXADataSource();
//        druidXADataSource.setUrl(jdbcUrl);
//        druidXADataSource.setUsername(username);
//        druidXADataSource.setPassword(password);
//
//        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
//        xaDataSource.setXaDataSource(druidXADataSource);
//        xaDataSource.setUniqueResourceName("thirdDataSource");
//        return xaDataSource;
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public SqlSessionFactory fourSqlSessionFactory(@Qualifier("fourDataSource") DataSource dataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath*:mapper/four/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionTemplate fourSqlSessionTemplate(@Qualifier("fourSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        // 使用上面配置的Factory
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory);
        return template;
    }
}
