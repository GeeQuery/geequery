package com.github.geequery.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * 当数据类型为int, long, short, char等基础类型时。如果Entity的修改记录了该值是设置过的，
 * 那么认为是有效值。<br>
 * 如果无记录，那么不等于UnsavedValue的值认为是有效值。
 * @author jiyi
 *
 */
@Target({ FIELD })
@Retention(RUNTIME)
public @interface UnsavedValue {
	String value();
	
	String NullOrEmpty="null+";
	
	String MinusNumber="<0";
	
	String ZeroAndMinus="<=0";
}
