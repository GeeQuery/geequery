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
package com.github.geequery.jsqlparser.visitor;

import com.github.geequery.jsqlparser.expression.Table;
import com.github.geequery.jsqlparser.statement.SqlAppendable;
import com.github.geequery.jsqlparser.statement.select.SelectExpressionItem;

/**
 * Anything between "SELECT" and "FROM"<BR>
 * (that is, any column or expression etc to be retrieved with the query)
 */
public interface SelectItem extends SqlAppendable{

    public void accept(SelectItemVisitor selectItemVisitor);
    
    /**
     * 返回查询表达式。如果是AllColumn * /t.*等格式则抛出异常，因此在调用此方法之前要先按isAllColumns进行判断
     * @return
     */
    public SelectExpressionItem getAsSelectExpression();
    
    
    /**
     * 如果是AllColumn类型 * /t.*返回其所属表。如果是Expression类型会抛出异常
     * @return
     */
    public Table getTableOfAllColumns();

	/**
	 * 拼入对象
	 * @param sb
	 * @param noGroupFunc
	 */
	public String getStringWithoutGroupFuncAndAlias();
	
	/**
	 * 是否为 t.*, *等格式的
	 * @return
	 */
	public boolean isAllColumns();
	
	
}
