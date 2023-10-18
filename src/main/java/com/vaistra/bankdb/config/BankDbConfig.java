package com.vaistra.bankdb.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "bankdbEntityManagerFactoryBean",
        transactionManagerRef = "bankdbTransactionManagerBean",
        basePackages = {"com.vaistra.bankdb.repositories"}
)
public class BankDbConfig {
    @Autowired
    private Environment environment;

//    @Primary
    @Bean("bankdbDataSource")
    public DataSource dataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl(environment.getProperty("spring.datasource.bankdb.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.bankdb.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.bankdb.password"));

        return dataSource;
    }

//    @Primary
    @Bean("bankdbEntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean()
    {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setDataSource(dataSource());

        Map<String, String> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show-sql", "true");
        props.put("hibernate.dialect","org.hibernate.dialect.PostgreSQLDialect");
        entityManagerFactoryBean.setJpaPropertyMap(props);

        JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(adapter);

        entityManagerFactoryBean.setPackagesToScan("com.vaistra.bankdb.entities");

        return entityManagerFactoryBean;
    }

//    @Primary
    @Bean("bankdbTransactionManagerBean")
    public PlatformTransactionManager transactionManager()
    {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());

        return transactionManager;
    }

}
