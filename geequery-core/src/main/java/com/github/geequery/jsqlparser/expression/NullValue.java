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
package com.github.geequery.jsqlparser.expression;

import java.math.BigDecimal;

import com.github.geequery.jsqlparser.visitor.Expression;
import com.github.geequery.jsqlparser.visitor.ExpressionType;
import com.github.geequery.jsqlparser.visitor.ExpressionVisitor;
import com.github.geequery.jsqlparser.visitor.SqlValue;

/**
 *  A "NULL" in a sql statement
 */
public class NullValue implements Expression ,SqlValue{
	static NullValue instance=new NullValue();
	
	private NullValue(){};
	
	public static NullValue getInstance(){
		return instance;
	}
	
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public String toString() {
        return "NULL";
    }

	public void appendTo(StringBuilder sb) {
		sb.append("NULL");
	}
	
	public ExpressionType getType() {
		return ExpressionType.value;
	}

	public Object getValue() {
		return null;
	}

	public Object formatNumber(BigDecimal negate) {
		throw new UnsupportedOperationException();
	}
}
