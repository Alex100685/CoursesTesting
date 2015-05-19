package com.andre.mvc.database;

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
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Андрей on 17.05.2015.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(value = "com.andre.mvc.database.crm.repository",
                        entityManagerFactoryRef = "primaryEntityManagerFactory",
                        transactionManagerRef = "primaryTransactionManager")
@PropertySource("classpath:application.properties")
public class PrimaryDataBaseConfig {

    private static final String PROPERTY_NAME_DATABASE_DRIVER_PRIMARY = "db1.driver";
    private static final String PROPERTY_NAME_DATABASE_PASSWORD_PRIMARY = "db1.password";
    private static final String PROPERTY_NAME_DATABASE_URL_PRIMARY = "db1.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME_PRIMARY = "db1.username";

    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN1 = "entitymanager.packages.to.scan1";

    private static final String PACKAGES_TO_SCAN_FOR_ENTITY_MANAGER = "com.andre.mvc.database.crm";

    @Resource
    private Environment env;

    @Bean
    public DataSource primaryDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER_PRIMARY));
        dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL_PRIMARY));
        dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME_PRIMARY));
        dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD_PRIMARY));

        return dataSource;
    }

//    @Bean
//    public LocalSessionFactoryBean primarySessionFactory() {
//        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
//        sessionFactoryBean.setDataSource(primaryDataSource());
//        sessionFactoryBean.setPackagesToScan(env.getRequiredProperty(PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN1));
//        sessionFactoryBean.setHibernateProperties(hibProperties());
//        return sessionFactoryBean;
//    }

//    private Properties hibProperties() {
//        Properties properties = new Properties();
//        properties.put(PROPERTY_NAME_HIBERNATE_DIALECT, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
//        properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));
//        return properties;
//    }

//    @Bean
//    public HibernateTransactionManager primaryTransactionManager() {
//        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//        transactionManager.setSessionFactory(primarySessionFactory().getObject());
//        return transactionManager;
//    }

    @Bean
    @Autowired
    public JpaTransactionManager primaryTransactionManager(@Qualifier("primaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

//    @Bean
//    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory() {
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        vendorAdapter.setDatabase(Database.MYSQL);
//        vendorAdapter.setGenerateDdl(true);
//        vendorAdapter.setShowSql(true);
//        LocalContainerEntityManagerFactoryBean factory =
//                new LocalContainerEntityManagerFactoryBean();
//        factory.setJpaVendorAdapter(vendorAdapter);
//        factory.setPackagesToScan(PACKAGES_TO_SCAN_FOR_ENTITY_MANAGER);
//        factory.setDataSource(primaryDataSource());
//        return factory;
//    }

    @Bean
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(primaryDataSource());
        em.setPackagesToScan(PACKAGES_TO_SCAN_FOR_ENTITY_MANAGER);

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
