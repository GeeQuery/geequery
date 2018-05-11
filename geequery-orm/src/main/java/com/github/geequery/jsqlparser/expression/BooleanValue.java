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
import com.github.geequery.tools.StringUtils;

/**
 * Every number without a point or an exponential format is a LongValue
 */
public class BooleanValue implements Expression, SqlValue {

	private boolean value;

	public BooleanValue() {
	}

	public BooleanValue(boolean value) {
		this.value = value;
	}

	public void accept(ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}

	public Boolean getValue() {
		return value;
	}

	public void setValue(boolean d) {
		value = d;
	}

	public String getStringValue() {
		return String.valueOf(value);
	}

	public void setStringValue(String stringValue) {
		this.value = StringUtils.toBoolean(stringValue, false);
	}

	public String toString() {
		return String.valueOf(value);
	}

	public void appendTo(StringBuilder sb) {
		sb.append(String.valueOf(value));
	}

	public ExpressionType getType() {
		return ExpressionType.value;
	}

	public Object formatNumber(BigDecimal negate) {
		return negate.intValue() != 0;
	}
}
