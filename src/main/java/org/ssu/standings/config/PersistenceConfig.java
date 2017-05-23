package org.ssu.standings.config;

import liquibase.integration.spring.SpringLiquibase;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan({"org.ssu.standings.dao"})
@EnableJpaRepositories("org.ssu.standings.dao.repository")
@PropertySource("classpath:db.properties")
public class PersistenceConfig {
    @Resource
    private Environment environment;


    @Bean(name = "dataSource")
    DataSource getDataSource() {
        BasicDataSource connectionDataSource = new BasicDataSource();
        connectionDataSource.setDriverClassName(environment.getProperty("hibernate.driver"));
        connectionDataSource.setUrl(environment.getProperty("hibernate.url"));
        connectionDataSource.setUsername(environment.getProperty("hibernate.username"));
        connectionDataSource.setPassword(environment.getProperty("hibernate.password"));
        return connectionDataSource;
    }

    @Bean(name = "sessionFactory")
    LocalSessionFactoryBean getSessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        sessionFactory.setPackagesToScan("org.ssu.standings.dao.entity");
        sessionFactory.setHibernateProperties(new Properties() {{
            setProperty("hibernate.hbm2ddl.auto", environment.getProperty("hibernate.hbm2ddl.auto"));
            setProperty("hibernate.dialect", environment.getProperty("hibernate.dialect"));
            setProperty("hibernate.globally_quoted_identifiers", "true");
            setProperty("hibernate.show_sql", "false");
        }});
        return sessionFactory;
    }


    @Bean(name = "entityManagerFactory")
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(dataSource);
        entityManager.setPackagesToScan("org.ssu.standings.dao.entity");

        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        entityManager.setJpaProperties(new Properties() {{
            setProperty("hibernate.hbm2ddl.auto", environment.getProperty("hibernate.hbm2ddl.auto"));
            setProperty("hibernate.dialect", environment.getProperty("hibernate.dialect"));
            setProperty("hibernate.show_sql", "false");
            setProperty("current_session_context_class", "thread");
            setProperty("hibernate.enable_lazy_load_no_trans", "true");
        }});
        return entityManager;
    }

    @Bean(name = "transactionManager")
    @Autowired
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:liquibase-changeLog.xml");
        liquibase.setDataSource(getDataSource());
        return liquibase;
    }
}
