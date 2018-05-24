package cases;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class GeeQueryConfiuration {
	
	

	@Bean
	public DataSource dataSource() {
		// implementation omitted
		return null;
	}

	@Bean
	public SqlSessionFactory myBatis() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource());
		return factoryBean.getObject();
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean()
	public EntityManagerFactory entityManagerFactory(DataSource dataSource, Environment env) {
		SessionFactoryBean bean = new org.easyframe.enterprise.spring.SessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.afterPropertiesSet();
		bean.set
		return bean.getObject();
	}
	
	/*
	@Bean
	public com.querydsl.sql.Configuration querydslConfiguration() {
		SQLTemplates templates = H2Templates.builder().build(); // change to
																// your
																// Templates
		com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(templates);
		configuration.setExceptionTranslator(new SpringExceptionTranslator());
		return configuration;
	}

	@Bean
	public SQLQueryFactory queryFactory() {
		Provider<Connection> provider = new SpringConnectionProvider(dataSource());
		return new SQLQueryFactory(querydslConfiguration(), provider);
	}
*/
}
