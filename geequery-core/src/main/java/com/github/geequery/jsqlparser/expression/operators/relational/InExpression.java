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
package com.github.geequery.jsqlparser.expression.operators.relational;

import java.util.Iterator;
import java.util.List;

import com.github.geequery.jsqlparser.visitor.Expression;
import com.github.geequery.jsqlparser.visitor.ExpressionType;
import com.github.geequery.jsqlparser.visitor.ExpressionVisitor;
import com.github.geequery.jsqlparser.visitor.Ignorable;
import com.github.geequery.jsqlparser.visitor.ItemsList;
import com.github.geequery.jsqlparser.visitor.Notable;

public class InExpression implements Expression,Ignorable,Notable {
    //变量绑定值是否为空
    private final ThreadLocal<Boolean> isEmpty = new ThreadLocal<Boolean>();
    
    private List<Expression> leftExpression;
    
    public boolean isEmpty() {
    	Boolean e=isEmpty.get();
		return e!=null && e;
	}
    
    private ItemsList itemsList;

    private boolean not = false;

    public InExpression() {
    }

    public InExpression(List<Expression> leftExpression, ItemsList itemsList) {
        setLeftExpression(leftExpression);
        setItemsList(itemsList);
    }

    public ItemsList getItemsList() {
        return itemsList;
    }

    public List<Expression> getLeftExpression() {
        return leftExpression;
    }

    public void setEmpty(Boolean isEmpty) {
		this.isEmpty.set(isEmpty);
	}
    
    public void setItemsList(ItemsList list) {
        itemsList = list;
    }

    public void setLeftExpression(List<Expression> expression) {
        leftExpression = expression;
    }

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean b) {
        not = b;
    }

    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    public String toString() {
    	StringBuilder sb=new StringBuilder();
    	appendTo(sb);
    	return sb.toString();
    }

	public void appendTo(StringBuilder sb) {
	    if(leftExpression!=null){
	        boolean isList=leftExpression.size()>1;
	        if(isList)sb.append('(');
	        Iterator<Expression> iter=leftExpression.iterator();
	        if(iter.hasNext()){
	            iter.next().appendTo(sb);
	        }
	        while(iter.hasNext()){
	            sb.append(',');
	            iter.next().appendTo(sb);
	        }
	        if(isList)sb.append(')');
	    }
		sb.append(' ');
		if(not)sb.append("NOT ");
		sb.append("IN ");
		itemsList.appendTo(sb);
	}

	public ExpressionType getType() {
		return ExpressionType.in;
	}

    public Expression getSingleLeftExpression() {
        if(leftExpression!=null && leftExpression.size()>1){
            throw new UnsupportedOperationException();
        }
        return leftExpression.get(0);
    }
}
