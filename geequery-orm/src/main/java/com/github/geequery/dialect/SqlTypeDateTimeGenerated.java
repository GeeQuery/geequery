package com.github.geequery.dialect;

import jef.database.annotation.DateGenerateType;

public interface SqlTypeDateTimeGenerated {
	DateGenerateType getGenerateType();

	ColumnType setGenerateType(DateGenerateType dateGenerateType);
}
