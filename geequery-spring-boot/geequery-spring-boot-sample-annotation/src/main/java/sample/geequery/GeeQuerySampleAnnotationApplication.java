/**
 *    Copyright 2015-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package sample.geequery;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import jef.database.datasource.SimpleDataSource;
import jef.database.support.InitDataExporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import sample.geequery.domain.City;
import sample.geequery.repos.CityRepository;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class})
public class GeeQuerySampleAnnotationApplication implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

//	@Bean
//	public DataSource getDataSource(){
//		return new SimpleDataSource("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8","root","admin");
//	}
	
	public static void main(String[] args) {
		SpringApplication.run(GeeQuerySampleAnnotationApplication.class, args);
	}

	@Autowired
	private ApplicationContext context;

	@Autowired
	private CityRepository cityMapper;
	
	@Autowired
	private EntityManagerFactory emf;



	@Override
	public void run(String... args) throws Exception {
		System.out.println(this.cityMapper.findByState("浙江"));
		System.out.println("运行测试完成");
	}

	
	protected void generateData(){
		new InitDataExporter(emf).export(City.class);
	}
}
