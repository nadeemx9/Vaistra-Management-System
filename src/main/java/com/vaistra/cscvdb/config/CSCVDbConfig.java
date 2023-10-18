package com.vaistra.cscvdb.config;

import org.springframework.beans.factory.annotation.Autowired;
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
        entityManagerFactoryRef = "cscvdbEntityManagerFactoryBean",
        transactionManagerRef = "cscvdbTransactionManagerBean",
        basePackages = {"com.vaistra.cscvdb.repositories"}
)
public class CSCVDbConfig {
    @Autowired
    private Environment environment;

    @Primary
    @Bean("cscvdbDataSource")
    public DataSource dataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl(environment.getProperty("spring.datasource.cscvdb.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.cscvdb.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.cscvdb.password"));

        return dataSource;
    }

    @Primary
    @Bean("cscvdbEntityManagerFactoryBean")
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

        entityManagerFactoryBean.setPackagesToScan("com.vaistra.cscvdb.entities");

        return entityManagerFactoryBean;
    }

    @Primary
    @Bean("cscvdbTransactionManagerBean")
    public PlatformTransactionManager transactionManager()
    {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());

        return transactionManager;
    }
}
