package com.ms.springdatajpamultidatasource.config;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 从数据源的Repository配置
 * 
 * @author wuketao
 *
 */
@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactorySecondary",
        transactionManagerRef = "transactionManagerSecondary",
        basePackages = {"com.ms.springdatajpamultidatasource.repository.secondary"}) // 设置dao（repo）所在位置
public class RepositorySecondaryConfig {

    @Autowired
    private JpaProperties jpaProperties;

    /**
     * 注入从数据源Bean
     */
    @Autowired
    @Qualifier("secondaryDS")
    private DataSource secondaryDS;

    /**
     * 创建从数据源的实体管理器
     * 
     * @param builder
     * @return
     */
    @Bean(name = "entityManagerSecondary")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactorySecondary(builder).getObject().createEntityManager();
    }

    /**
     * 创建从数据源的实体管理器工厂
     * 
     * @param builder
     * @return
     */
    @Bean(name = "entityManagerFactorySecondary")
    public LocalContainerEntityManagerFactoryBean entityManagerFactorySecondary(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(secondaryDS).properties(getVendorProperties(secondaryDS))
                .packages("com.ms.springdatajpamultidatasource.bean.secondary") // 设置实体类所在位置
                .persistenceUnit("secondaryPersistenceUnit").build();
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

    /**
     * 创建从数据源的事务管理器
     * 
     * @param builder
     * @return
     */
    @Bean(name = "transactionManagerSecondary")
    PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactorySecondary(builder).getObject());
    }
}
