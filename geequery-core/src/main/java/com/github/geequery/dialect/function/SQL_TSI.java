package com.github.geequery.dialect.function;

/**
 * 描述标准SQL函数timestampadd timestampdiff 中的辅助单位
 * 
 * @author jiyi
 *
 */
public enum SQL_TSI {
	FRAC_SECOND, SECOND, MINUTE, HOUR, DAY, WEEK, MONTH, QUARTER, YEAR;
	private String exp;

	SQL_TSI() {
		this.exp = "SQL_TSI_" + name();
	}

	public String get() {
		return exp;
	}
}
