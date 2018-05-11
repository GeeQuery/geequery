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

import java.util.List;

import jef.tools.ArrayUtils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sample.geequery.repos.CityDao;
import sample.geequery.repos.HotelDao;

@SpringBootApplication
public class SampleXmlApplication implements CommandLineRunner {

	public static void main(String[] args) {
		//模拟通过命令行注入参数
		List<String> argList=ArrayUtils.asList(args);
		argList.add("--geequery.repos=sample.geequery.repos");
		argList.add("--geequery.packagesToScan=sample.geequery.domain");
		argList.add("--logging.level.root=INFO");
		SpringApplication.run(SampleXmlApplication.class, argList.toArray(new String[argList.size()]));
	}

	private final CityDao cityDao;

	private final HotelDao hotelDao;

	public SampleXmlApplication(CityDao cityDao, HotelDao hotelMapper) {
		this.cityDao = cityDao;
		this.hotelDao = hotelMapper;
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(this.cityDao.findById(1).orElse(null));
		System.out.println(this.hotelDao.findById(1));
	}

}
