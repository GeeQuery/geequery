package com.github.geequery.dialect.type;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import jef.accelerator.bean.BeanAccessor;
import jef.accelerator.bean.FastBeanWrapperImpl;
import jef.database.Field;
import jef.database.ORMConfig;

import com.github.geequery.dialect.ColumnType;
import com.github.geequery.dialect.ColumnType.GUID;
import com.github.geequery.entity.IQueryableEntity;

import jef.database.meta.EntityType;
import jef.database.meta.EntityMetadata;
import jef.database.wrapper.clause.InsertSqlClause;
import jef.database.wrapper.processor.InsertStep.GUIDGenerateCallback;
import jef.database.wrapper.processor.InsertStep.SingleKeySetCallback;
import jef.tools.Assert;
import jef.tools.StringUtils;
import jef.tools.reflect.Property;

public class AutoGuidMapping extends VarcharStringMapping {
	
	private boolean removeDash;
	private Property accessor;

	@Override
	public void init(Field field, String columnName, ColumnType type, EntityMetadata meta) {
		super.init(field, columnName, type, meta);
		GUID cType = (GUID) type;
		removeDash = cType.isRemoveDash();

		// 初始化访问器
		BeanAccessor ba = FastBeanWrapperImpl.getAccessorFor(meta.getThisType());
		if(meta.getType()==EntityType.TUPLE){
			Assert.isTrue(ba.getPropertyNames().contains(field.name()));
		}
		accessor = ba.getProperty(field.name());
	}

	public void processInsert(Object value, InsertSqlClause result, List<String> cStr, List<String> vStr, boolean smart, IQueryableEntity obj) throws SQLException {
		String columnName = this.getColumnName(result.profile,true);
		String key;
		if (value != null && ORMConfig.getInstance().isManualSequence() && obj.isUsed(field)) {// 手动指定
			key = String.valueOf(value);
		} else {
			key = UUID.randomUUID().toString();
			if (removeDash)
				key = StringUtils.remove(key, '-');
			result.getCallback().addProcessor(new SingleKeySetCallback(accessor, key));
		}
		cStr.add(columnName);
		vStr.add(String.valueOf(key));
	}

	@Override
	public void processPreparedInsert(IQueryableEntity obj, List<String> cStr, List<String> vStr, InsertSqlClause result, boolean smart) {
		String columnName = this.getColumnName(result.profile, true);

		Object value = accessor.get(obj);
		if (value != null && ORMConfig.getInstance().isManualSequence() && obj.isUsed(field)) {
			//DO nothing
		} else {
			result.getCallback().addProcessor(new GUIDGenerateCallback(accessor, removeDash));
		}
		cStr.add(columnName);
		vStr.add("?");
		result.addField(this);

	}
}
