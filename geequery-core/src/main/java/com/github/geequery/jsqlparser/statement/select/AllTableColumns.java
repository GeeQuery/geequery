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
package com.github.geequery.jsqlparser.statement.select;

import com.github.geequery.jsqlparser.expression.Table;
import com.github.geequery.jsqlparser.visitor.SelectItem;
import com.github.geequery.jsqlparser.visitor.SelectItemVisitor;

public class AllTableColumns implements SelectItem {

    private Table table;

    public AllTableColumns() {
    }

    public AllTableColumns(Table tableName) {
        this.table = tableName;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public void accept(SelectItemVisitor selectItemVisitor) {
        selectItemVisitor.visit(this);
    }

    public String toString() {
        return table + ".*";
    }
    //在slect项中使用 t.*时，其table的name是t，而不是alias.
	public void appendTo(StringBuilder sb) {
		table.appendTo(sb);
		sb.append(".*");
	}

	public SelectExpressionItem getAsSelectExpression() {
		throw new IllegalStateException();
	}

	public boolean isAllColumns() {
		return true;
	}

	public Table getTableOfAllColumns() {
		return table;
	}

	public String getStringWithoutGroupFuncAndAlias() {
		 return table + ".*";
	}
}
