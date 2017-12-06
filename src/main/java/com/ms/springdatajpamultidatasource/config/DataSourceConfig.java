package com.ms.springdatajpamultidatasource.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.Data;

/**
 * 数据源配置类
 * 
 * @author wuketao
 *
 */
@Configuration
@EnableConfigurationProperties({DataSourceConfig.PrimaryDataSourceConfig.class,
        DataSourceConfig.SecondaryDataSourceConfig.class})
public class DataSourceConfig {

    /**
     * 主数据源配置类
     * 
     * @author wuketao
     *
     */
    @Data
    @ConfigurationProperties(prefix = "primary.datasource", ignoreUnknownFields = true)
    class PrimaryDataSourceConfig {

        String jdbcUrl;
        String username;
        String password;
        String driverClassName;
        int maxPoolSize;
        int minIdle;
    }

    /**
     * 主数据源配置类 实例
     */
    @Autowired
    private PrimaryDataSourceConfig primaryDataSourceConfig;

    /**
     * 从数据源配置类
     * 
     * @author wuketao
     *
     */
    @Data
    @ConfigurationProperties(prefix = "secondary.datasource", ignoreUnknownFields = true)
    class SecondaryDataSourceConfig {

        String jdbcUrl;
        String username;
        String password;
        String driverClassName;
        int maxPoolSize;
        int minIdle;
    }

    /**
     * 从数据源配置类 实例
     */
    @Autowired
    private SecondaryDataSourceConfig secondaryDataSourceConfig;

    /**
     * 配置主数据源<br/>
     * 主数据源Bean需要使用@Primary注解
     * 
     * @return
     */
    @Bean(name = "primaryDS")
    @Primary
    public DataSource primaryDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(primaryDataSourceConfig.getJdbcUrl());
        config.setUsername(primaryDataSourceConfig.getUsername());
        config.setPassword(primaryDataSourceConfig.getPassword());
        config.setDriverClassName(primaryDataSourceConfig.getDriverClassName());
        config.setMaximumPoolSize(primaryDataSourceConfig.getMaxPoolSize());
        config.setMinimumIdle(primaryDataSourceConfig.getMinIdle());
        return new HikariDataSource(config);
    }

    /**
     * 从数据源配置
     * 
     * @return
     */
    @Bean(name = "secondaryDS")
    public DataSource secondaryDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(secondaryDataSourceConfig.getJdbcUrl());
        config.setUsername(secondaryDataSourceConfig.getUsername());
        config.setPassword(secondaryDataSourceConfig.getPassword());
        config.setDriverClassName(secondaryDataSourceConfig.getDriverClassName());
        config.setMaximumPoolSize(secondaryDataSourceConfig.getMaxPoolSize());
        config.setMinimumIdle(secondaryDataSourceConfig.getMinIdle());
        return new HikariDataSource(config);
    }
}
