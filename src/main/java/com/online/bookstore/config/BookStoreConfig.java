package com.online.bookstore.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableTransactionManagement
@EnableSwagger2
@EnableJpaRepositories(basePackages = { "com.online.bookstore.repository" })
@ComponentScan(basePackages = { "com.online.bookstore" })
@SpringBootApplication
public class BookStoreConfig {

	protected static final Logger confLogger = LoggerFactory.getLogger(BookStoreConfig.class);

	@Bean
	public DataSource dataSource(Environment env) {
		confLogger.debug("------------> Initializing DataSource");
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(env.getProperty("spring.datasource.url"));
		config.setUsername(env.getProperty("spring.datasource.username"));
		config.setPassword(env.getProperty("spring.datasource.password"));
		config.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
		config.setConnectionTimeout(Long.valueOf(env.getProperty("spring.datasource.hikari.connectionTimeout")));
		config.setMaximumPoolSize(Integer.valueOf(env.getProperty("spring.datasource.hikari.maximumPoolSize")));
		config.setPoolName(env.getProperty("spring.datasource.hikari.poolName"));
		config.setIdleTimeout(Long.valueOf(env.getProperty("spring.datasource.hikari.idleTimeout")));
		config.setMaxLifetime(Long.valueOf(env.getProperty("spring.datasource.hikari.maxLifetime")));
		config.setMinimumIdle(Integer.valueOf(env.getProperty("spring.datasource.hikari.minimumIdle")));

		config.addDataSourceProperty("implicitCachingEnabled", "true");
		config.addDataSourceProperty("fastConnectionFailoverEnabled", "true");
		return new HikariDataSource(config);
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(Environment env) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource(env));
		em.setPackagesToScan("com.online.bookstore.model");
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties(env));
		em.afterPropertiesSet();
		return em;
	}

	private Properties additionalProperties(Environment env) {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto",
				env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
		properties.setProperty("hibernate.jdbc.time_zone",
				env.getRequiredProperty("spring.jpa.properties.hibernate.jdbc.time_zone"));
		properties.setProperty("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
		properties.setProperty("hibernate.show_sql", env.getProperty("spring.jpa.properties.hibernate.show_sql"));
		properties.setProperty("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql"));
		return properties;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		confLogger.debug("------------> Initializing propertySourcesPlaceholderConfigurer");
		PropertySourcesPlaceholderConfigurer p = new PropertySourcesPlaceholderConfigurer();
		p.setIgnoreUnresolvablePlaceholders(true);
		return p;
	}

}
