package com.github.geequery.entity;

import java.sql.Connection;

import javax.inject.Provider;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.querydsl.sql.H2Templates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;

@Configuration
public class JdbcConfiguration {

	@Bean
	public DataSource dataSource() {
		// implementation omitted
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean
	public SqlSessionFactory myBatis() {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource()
		SqlSessionFactory sessionFactory = factoryBean.getObject();
	}

	
	
	org.mybatis.spring.SqlSessionFactoryBean
	
	/**
	 * QueryDSL配置
	 * @return
	 */
	@Bean
	public com.querydsl.sql.Configuration querydslConfiguration() {
		SQLTemplates templates = H2Templates.builder().build(); // change to your Templates
		com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(templates);
		configuration.setExceptionTranslator(new SpringExceptionTranslator());
		return configuration;
	}

	/**
	 * QueryDSL的配置
	 * @return
	 */
	@Bean
	public SQLQueryFactory queryFactory() {
		Provider<Connection> provider = new SpringConnectionProvider(dataSource());
		return new SQLQueryFactory(querydslConfiguration(), provider);
	}

}