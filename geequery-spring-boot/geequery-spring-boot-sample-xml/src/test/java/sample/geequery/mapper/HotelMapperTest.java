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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import sample.geequery.domain.Hotel;
import sample.geequery.repos.HotelDao;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link HotelMapper}.
 */
@RunWith(SpringRunner.class)
public class HotelMapperTest {

  @Autowired
  private HotelDao hotelMapper;

  @Test
  public void selectByCityIdTest() {
    Hotel hotel = hotelMapper.findById(1);
    assertThat(hotel.getName(),equalTo("Conrad Treasury Place"));
    assertThat(hotel.getAddress(),equalTo("William & George Streets"));
    assertThat(hotel.getZip(),equalTo("4001"));
  }

}
