package com.github.geequery.dialect;

public interface SqlTypeVersioned {
	
	boolean isVersion();
	
	ColumnType setVersion(boolean flag);
}
