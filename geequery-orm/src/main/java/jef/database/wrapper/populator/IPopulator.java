/*
 * JEF - Copyright 2009-2010 Jiyi (mr.jiyi@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jef.database.wrapper.populator;

import java.sql.SQLException;

import jef.database.jdbc.result.IResultSet;
import com.github.geequery.tools.reflect.BeanWrapper;

/**
 * 封装一段ResultSet到javabean的逻辑。
 * @author jiyi
 *
 */
public interface IPopulator {
	public void process(BeanWrapper wrapper, IResultSet rs) throws SQLException;
}
