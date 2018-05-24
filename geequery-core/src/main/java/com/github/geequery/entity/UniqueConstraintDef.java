package com.github.geequery.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.UniqueConstraint;

import com.github.geequery.dbmeta.Constraint;
import com.github.geequery.dbmeta.ConstraintType;
import com.github.geequery.dialect.DatabaseDialect;
import com.github.geequery.dialect.type.ColumnMapping;
import com.github.geequery.tools.StringUtils;

/**
 * 定义一个唯一约束
 * @author Jiyi
 *
 */
public class UniqueConstraintDef {
	public String name;

	public String[] columnNames;

	public UniqueConstraintDef(UniqueConstraint unique) {
		this.name=unique.name();
		this.columnNames=unique.columnNames();
	}

	public String name() {
		return name;
	}

	public String[] columnNames() {
		return columnNames;
	}
	
	public List<String> getColumnNames(EntityMetadata<?> meta, DatabaseDialect dialect) {
		List<String> columns=new ArrayList<String>(columnNames.length);
		for(int i=0;i<columnNames.length;i++){
			String name=columnNames[i];
			for(String s: StringUtils.split(name, ',')){//为了容错，这个很有可能配错
				ColumnMapping column=meta.findField(s);
				if(column==null){
					throw new NoSuchElementException("Field not found in entity "+meta.getName()+": "+s);
				}else{
					columns.add(column.getColumnName(dialect, true));
				}
			}
		}
		return columns;
	}
	
	public Constraint toConstraint(String tableName, EntityMetadata<?> meta, DatabaseDialect dialect){
		Constraint con = new Constraint();
		con.setName(name);
		con.setTableName(tableName);
		con.setColumns(getColumnNames(meta, dialect));
		con.setType(ConstraintType.U);
		return con;
	}
}
