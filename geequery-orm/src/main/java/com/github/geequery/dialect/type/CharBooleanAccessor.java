package com.github.geequery.dialect.type;

import java.sql.SQLException;
import java.sql.Types;

import jef.database.jdbc.result.IResultSet;

final class CharBooleanAccessor implements ResultSetAccessor{

	public Object jdbcGet(IResultSet rs, int n) throws SQLException {
		String s=rs.getString(n);
		if(s!=null && s.length()>0){
			char c=s.charAt(0);
			return Boolean.valueOf(c=='1' || c=='T');
		}
		return null;
	}

	public Class<?> getReturnType() {
		return Boolean.class;
	}

	public boolean applyFor(int type) {
		return Types.CHAR==type || Types.VARCHAR==type;
	}
	

}
