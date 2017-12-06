#spring data jpa配置多数据源方案
##摘要
1. spring boot可以通过自动配置将数据源、实体管理器、事务管理器通过jar包和配置文件自动生成，无需程序员参与。 但是这种方式只能配置一个数据源。对于多数据源的情况需要手动配置数据源、实体管理器、事务管理器对象。  
2. 首先在DataSourceConfig类中配置了两个数据源的实例。由于都是DataSource类型的Bean，为了区分这两个Bean，需要给每个Bean指定唯一的名称，并且为一个Bean添加注释@Primary。由于DataSourceConfig类中采用内部类+@ConfigurationProperties的方式读取了配置文件中的配置信息。所有需要在DataSourceConfig类上添加@EnableConfigurationProperties注解并指出内部类。  
3. 然后分别为主数据源、从数据源配置实体管理器和事务管理器，配置的详情请参见RepositoryPrimaryConfig、RepositorySecondaryConfig。这里需要特别注意的几点：  
1) 在类上使用@EnableJpaRepositories注解指明实体管理器工厂Bean的名称、事务管理器的名称、该数据源的repository接口所在的包。  
2) 在主数据源的所有配置Bean上都添加了@Primary注解。  
3) 在配置实体管理器工厂Bean时，需要指明该数据源的实体类所在包。  
4. 在每个service类的Transcational注解中指定事务管理器的特定名称。  

##核心代码
数据源Bean配置类DataSourceConfig类  

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
	
主数据源实体管理器、事务管理器配置类RepositoryPrimaryConfig  

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
	import org.springframework.context.annotation.Primary;
	import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
	import org.springframework.orm.jpa.JpaTransactionManager;
	import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
	import org.springframework.transaction.PlatformTransactionManager;
	import org.springframework.transaction.annotation.EnableTransactionManagement;
	
	/**
	 * 主数据源的Repository配置
	 * 
	 * @author wuketao
	 *
	 */
	@Configuration
	@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactoryPrimary",
	        transactionManagerRef = "transactionManagerPrimary",
	        basePackages = {"com.ms.springdatajpamultidatasource.repository.primary"}) // 设置dao（repo）所在位置
	public class RepositoryPrimaryConfig {
	
	    @Autowired
	    private JpaProperties jpaProperties;
	
	    /**
	     * 注入主数据源Bean
	     */
	    @Autowired
	    @Qualifier("primaryDS")
	    private DataSource primaryDS;
	
	    /**
	     * 创建主数据源的实体管理器
	     * 
	     * @param builder
	     * @return
	     */
	    @Bean(name = "entityManagerPrimary")
	    @Primary
	    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
	        return entityManagerFactoryPrimary(builder).getObject().createEntityManager();
	    }
	
	    /**
	     * 创建主数据源的实体管理器工厂
	     * 
	     * @param builder
	     * @return
	     */
	    @Bean(name = "entityManagerFactoryPrimary")
	    @Primary
	    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary(EntityManagerFactoryBuilder builder) {
	        return builder.dataSource(primaryDS).properties(getVendorProperties(primaryDS))
	                .packages("com.ms.springdatajpamultidatasource.bean.primary") // 设置实体类所在位置
	                .persistenceUnit("primaryPersistenceUnit").build();
	    }
	
	    private Map<String, String> getVendorProperties(DataSource dataSource) {
	        return jpaProperties.getHibernateProperties(dataSource);
	    }
	
	    /**
	     * 创建主数据源的事务管理器
	     * 
	     * @param builder
	     * @return
	     */
	    @Bean(name = "transactionManagerPrimary")
	    @Primary
	    PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
	        return new JpaTransactionManager(entityManagerFactoryPrimary(builder).getObject());
	    }
	}
		
从数据源实体管理器、事务管理器配置类RepositorySecondaryConfig  
	
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
	import org.springframework.transaction.annotation.EnableTransactionManagement;
	
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
		