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
package sample.geequery.mapper;

import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import sample.geequery.domain.City;
import sample.geequery.repos.CityRepository;

import com.github.geequery.springboot.test.autoconfigure.GeeQueryTest;

/**
 * Tests for {@link CityRepository}.
 */
@RunWith(SpringRunner.class)
@GeeQueryTest
public class CityRepositoryTest {

	@Autowired
	private CityRepository cityRepository;

	@Test
	public void findByStateTest() {
		City city = cityRepository.findByState("上海");
		Assert.assertThat(city.getName(), equalTo("上海"));
		Assert.assertThat(city.getState(), equalTo("上海"));
		Assert.assertThat(city.getCountry(), equalTo("中国"));
	}

}
