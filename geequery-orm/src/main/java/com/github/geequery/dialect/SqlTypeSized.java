package com.github.geequery.dialect;

public interface SqlTypeSized {
	int getLength();

	int getPrecision();

	int getScale();
}
