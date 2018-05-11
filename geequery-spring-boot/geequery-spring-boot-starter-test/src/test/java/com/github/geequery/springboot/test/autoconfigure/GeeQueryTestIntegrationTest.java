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
package com.github.geequery.springboot.test.autoconfigure;

import org.easyframe.enterprise.spring.CommonDao;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.github.geequery.springboot.test.domain.Sample;

/**
 * Integration tests for {@link GeeQueryTest}.
 */
@RunWith(SpringRunner.class)
@GeeQueryTest(includeFilters = {@ComponentScan.Filter(Service.class)})
@TestPropertySource(properties = {
		  "geequery.repos=com.github.geequery.springboot.test",
		  "geequery.packagesToScan=com.github.geequery.springboot.test",
		  "logging.level.root=WARN",
		  "logging.level.GeeQuery=INFO",
		  "logging.pattern.console=%d %t[%-5p] %c{0} - %m%n"
		  
})
public class GeeQueryTestIntegrationTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Autowired
  private CommonDao sqlSession;

  @Autowired
  private ApplicationContext applicationContext;
  
  @Autowired
  private ExampleService service;

  @Test
  public void testSqlSession() {
	  Sample s=new Sample();
	  s.setName("User");
	  sqlSession.insert(s);
	  //再查出来
	  Sample sample=service.findById(s.getId());
	  Assert.assertThat(sample, CoreMatchers.notNullValue());
    
  }

  @Test
  public void testAutoConfigureComponents() {
    this.applicationContext.getBean(JpaTransactionManager.class);
    this.applicationContext.getBean(TransactionInterceptor.class);
    // @AutoConfigureCache
    this.applicationContext.getBean(CacheManager.class);
    this.applicationContext.getBean(CacheInterceptor.class);
  }

  @Test
  public void didNotInjectExampleComponent() {
    this.thrown.expect(NoSuchBeanDefinitionException.class);
    this.applicationContext.getBean(ExampleComponent.class);
  }

}
