package com.github.geequery.jsqlparser.visitor;

import java.math.BigDecimal;

public interface SqlValue {
	Object getValue();

	Object formatNumber(BigDecimal negate);
}
