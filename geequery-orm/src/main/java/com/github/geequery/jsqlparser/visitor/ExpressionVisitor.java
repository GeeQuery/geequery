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

import com.github.geequery.jsqlparser.expression.AllComparisonExpression;
import com.github.geequery.jsqlparser.expression.AnyComparisonExpression;
import com.github.geequery.jsqlparser.expression.BooleanValue;
import com.github.geequery.jsqlparser.expression.CaseExpression;
import com.github.geequery.jsqlparser.expression.Column;
import com.github.geequery.jsqlparser.expression.DateValue;
import com.github.geequery.jsqlparser.expression.DoubleValue;
import com.github.geequery.jsqlparser.expression.Function;
import com.github.geequery.jsqlparser.expression.Interval;
import com.github.geequery.jsqlparser.expression.InverseExpression;
import com.github.geequery.jsqlparser.expression.JdbcParameter;
import com.github.geequery.jsqlparser.expression.JpqlParameter;
import com.github.geequery.jsqlparser.expression.LongValue;
import com.github.geequery.jsqlparser.expression.NullValue;
import com.github.geequery.jsqlparser.expression.Over;
import com.github.geequery.jsqlparser.expression.Parenthesis;
import com.github.geequery.jsqlparser.expression.StringValue;
import com.github.geequery.jsqlparser.expression.TimeValue;
import com.github.geequery.jsqlparser.expression.TimestampValue;
import com.github.geequery.jsqlparser.expression.WhenClause;
import com.github.geequery.jsqlparser.expression.operators.arithmetic.Addition;
import com.github.geequery.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import com.github.geequery.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import com.github.geequery.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import com.github.geequery.jsqlparser.expression.operators.arithmetic.Concat;
import com.github.geequery.jsqlparser.expression.operators.arithmetic.Division;
import com.github.geequery.jsqlparser.expression.operators.arithmetic.Mod;
import com.github.geequery.jsqlparser.expression.operators.arithmetic.Multiplication;
import com.github.geequery.jsqlparser.expression.operators.arithmetic.Subtraction;
import com.github.geequery.jsqlparser.expression.operators.conditional.AndExpression;
import com.github.geequery.jsqlparser.expression.operators.conditional.OrExpression;
import com.github.geequery.jsqlparser.expression.operators.relational.Between;
import com.github.geequery.jsqlparser.expression.operators.relational.EqualsTo;
import com.github.geequery.jsqlparser.expression.operators.relational.ExistsExpression;
import com.github.geequery.jsqlparser.expression.operators.relational.ExpressionList;
import com.github.geequery.jsqlparser.expression.operators.relational.GreaterThan;
import com.github.geequery.jsqlparser.expression.operators.relational.GreaterThanEquals;
import com.github.geequery.jsqlparser.expression.operators.relational.InExpression;
import com.github.geequery.jsqlparser.expression.operators.relational.IsNullExpression;
import com.github.geequery.jsqlparser.expression.operators.relational.LikeExpression;
import com.github.geequery.jsqlparser.expression.operators.relational.MinorThan;
import com.github.geequery.jsqlparser.expression.operators.relational.MinorThanEquals;
import com.github.geequery.jsqlparser.expression.operators.relational.NotEqualsTo;
import com.github.geequery.jsqlparser.statement.select.StartWithExpression;
import com.github.geequery.jsqlparser.statement.select.SubSelect;

public interface ExpressionVisitor {

    public void visit(NullValue nullValue);

    public void visit(Function function);

    public void visit(InverseExpression inverseExpression);

    public void visit(JpqlParameter parameter);
    
    public void visit(JdbcParameter parameter);

    public void visit(DoubleValue doubleValue);

    public void visit(LongValue longValue);
    
    public void visit(BooleanValue longValue);

    public void visit(DateValue dateValue);

    public void visit(TimeValue timeValue);

    public void visit(TimestampValue timestampValue);

    public void visit(Parenthesis parenthesis);

    public void visit(StringValue stringValue);

    public void visit(Addition addition);

    public void visit(Division division);

    public void visit(Multiplication multiplication);

    public void visit(Subtraction subtraction);

    public void visit(AndExpression andExpression);

    public void visit(OrExpression orExpression);

    public void visit(Between between);

    public void visit(EqualsTo equalsTo);

    public void visit(GreaterThan greaterThan);

    public void visit(GreaterThanEquals greaterThanEquals);

    public void visit(InExpression inExpression);

    public void visit(IsNullExpression isNullExpression);

    public void visit(LikeExpression likeExpression);

    public void visit(MinorThan minorThan);

    public void visit(MinorThanEquals minorThanEquals);

    public void visit(NotEqualsTo notEqualsTo);

    public void visit(Column tableColumn);

    public void visit(SubSelect subSelect);

    public void visit(CaseExpression caseExpression);

    public void visit(WhenClause whenClause);

    public void visit(ExistsExpression existsExpression);

    public void visit(AllComparisonExpression allComparisonExpression);

    public void visit(AnyComparisonExpression anyComparisonExpression);

    public void visit(Concat concat);

    public void visit(BitwiseAnd bitwiseAnd);

    public void visit(BitwiseOr bitwiseOr);

    public void visit(BitwiseXor bitwiseXor);

	public void visit(Interval interval);

	public void visit(StartWithExpression startWithExpression);

	public void visit(Mod mod);

	public void visit(Over over);

	public void visit(ExpressionList expressionList);
}
