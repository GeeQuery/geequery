package com.github.geequery.dbmeta;

import com.github.geequery.dialect.type.ColumnMapping;

public enum Case {
	UPPER {
		public String getObjectNameToUse(String name) {
			return name.toUpperCase();
		}
		public String getObjectNameToUse(ColumnMapping column) {
			return column.upperColumnName();
		}
		public boolean isCaseSensitive() {
			return false;
		}
	}, LOWER {
		public String getObjectNameToUse(String name) {
			return name.toLowerCase();
		}
		public String getObjectNameToUse(ColumnMapping column) {
			return column.lowerColumnName();
		}
		public boolean isCaseSensitive() {
			return false;
		}
	}, MIXED_SENSITIVE {
		public String getObjectNameToUse(String name) {
			return name;
		}
		public String getObjectNameToUse(ColumnMapping column) {
			return column.rawColumnName();
		}
		public boolean isCaseSensitive() {
			return true;
		}
	};
	public abstract String getObjectNameToUse(String name);
	public abstract String getObjectNameToUse(ColumnMapping column);
	public abstract boolean isCaseSensitive();
}
