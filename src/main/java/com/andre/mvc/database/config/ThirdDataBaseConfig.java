package com.andre.mvc.database.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Velichko on 07.06.2015.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(value = "com.andre.mvc.database.hometask.repository",
        entityManagerFactoryRef = "thirdEntityManagerFactory",
        transactionManagerRef = "thirdTransactionManager")
@PropertySource("classpath:application.properties")
public class ThirdDataBaseConfig {

    private static final String PROPERTY_NAME_DATABASE_DRIVER_THIRD = "db3.driver";
    private static final String PROPERTY_NAME_DATABASE_PASSWORD_THIRD = "db3.password";
    private static final String PROPERTY_NAME_DATABASE_URL_THIRD = "db3.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME_THIRD = "db3.username";

    private static final String PACKAGES_TO_SCAN_FOR_ENTITY_MANAGE = "com.andre.mvc.database.hometask";

    @Resource
    private Environment env;

    @Bean
    public DataSource thirdDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER_THIRD));
        dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL_THIRD));
        dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME_THIRD));
        dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD_THIRD));

        return dataSource;
    }

    @Bean
    @Autowired
    public JpaTransactionManager thirdTransactionManager(@Qualifier("thirdEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean thirdEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(thirdDataSource());
        em.setPackagesToScan(PACKAGES_TO_SCAN_FOR_ENTITY_MANAGE);

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        return em;
    }

    public Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        return properties;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
