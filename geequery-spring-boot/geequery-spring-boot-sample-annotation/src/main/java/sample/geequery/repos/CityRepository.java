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
package sample.geequery.repos;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sample.geequery.domain.City;

import com.github.geequery.springdata.annotation.Query;
import com.github.geequery.springdata.repository.GqRepository;

@Repository
public interface CityRepository extends GqRepository<City, Long> {

	@Query("select * from city where state =:state")
	City findByState(@Param("state") String state);

}
