/*
 * JEF - Copyright 2009-2010 Jiyi (mr.jiyi@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.geequery.entity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Table;

import com.alibaba.fastjson.util.IOUtils;
import com.github.geequery.Field;
import com.github.geequery.accelerator.bean.BeanAccessor;
import com.github.geequery.accelerator.bean.FastBeanWrapperImpl;
import com.github.geequery.annotation.BindDataSource;
import com.github.geequery.annotation.Comment;
import com.github.geequery.annotation.EasyEntity;
import com.github.geequery.common.Entry;
import com.github.geequery.common.log.LogUtil;
import com.github.geequery.core.DbUtils;
import com.github.geequery.core.config.DbCfg;
import com.github.geequery.dialect.type.ColumnMapping;
import com.github.geequery.tools.ArrayUtils;
import com.github.geequery.tools.JefConfiguration;
import com.github.geequery.tools.StringUtils;
import com.github.geequery.tools.reflect.BeanUtils;
import com.github.geequery.tools.reflect.FieldEx;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.querydsl.codegen.EntityType;

public final class TableMetadata<T> extends AbstractMetadata<T> {
	/**
	 * 记录当前Schema所对应的DO类。
	 */
	private Class<T> thisType;
	BeanAccessor beanAccessor;

	private List<ColumnMapping> pkFields;// 记录主键列

	private final Map<Field, String> fieldToColumn = new IdentityHashMap<Field, String>();// 提供Field到列名的转换
	private final Map<String, String> lowerColumnToFieldName = new HashMap<String, String>();// 提供Column名称到Field的转换，不光包括元模型字段，也包括了非元模型字段但标注了Column的字段(key全部存小写)

	/**
	 * 标准实体的构造
	 * 
	 * @param clz
	 * @param annos
	 */
	TableMetadata(Class<T> clz, AnnotationProvider annos) {
		this.thisType = clz;
		this.pkFields = Collections.emptyList();
		this.beanAccessor = FastBeanWrapperImpl.getAccessorFor(clz);
		initByAnno(clz, annos);
	}

	protected void initByAnno(Class<?> thisType, AnnotationProvider annos) {
		// schema初始化
		Table table = annos.getAnnotation(javax.persistence.Table.class);
		if (table != null) {
			if (table.schema().length() > 0) {
				schema = MetaHolder.getMappingSchema(table.schema());// 重定向
			}
			if (table.name().length() > 0) {
				tableName = table.name();
			}
			for (javax.persistence.Index index : table.indexes()) {
				this.indexes.add(IndexDef.valueOf(index));
			}
			for (javax.persistence.UniqueConstraint unique : table.uniqueConstraints()) {
				this.uniques.add(new UniqueConstraintDef(unique));
			}
		}
		if (tableName == null) {
			// 表名未指定，缺省生成
			boolean needTranslate = JefConfiguration.getBoolean(DbCfg.TABLE_NAME_TRANSLATE, false);
			if (needTranslate) {
				tableName = DbUtils.upperToUnderline(thisType.getSimpleName());
			} else {
				tableName = thisType.getSimpleName();
			}
		}
		BindDataSource bindDs = annos.getAnnotation(BindDataSource.class);
		if (bindDs != null) {
			this.bindDsName = MetaHolder.getMappingSite(StringUtils.trimToNull(bindDs.value()));
		}

		Cacheable cache = annos.getAnnotation(Cacheable.class);
		this.cacheable = cache != null && cache.value();

		EasyEntity entity = annos.getAnnotation(EasyEntity.class);
		if (entity != null) {
			this.useOuterJoin = entity.useOuterJoin();
		}
	}

	public Class<?> getThisType() {
		return thisType;
	}

	public Class<? extends IQueryableEntity> getContainerType() {
		return containerType;
	}

	public List<IndexDef> getIndexDefinition() {
		return indexes;
	}

	public List<Field> getPKField() {
		return new AbstractList<Field>() {
			@Override
			public Field get(int index) {
				return pkFields.get(index).field();
			}

			@Override
			public int size() {
				return pkFields.size();
			}
		};
	}

	public List<ColumnMapping> getPKFields() {
		return pkFields;
	}

	/**
	 * 将一个Java Field加入到列定义中
	 * 
	 * @param field
	 * @param column
	 */
	public void putJavaField(Field field, ColumnMapping type, String columnName, boolean isPk) {
		fields.put(field.toString(), field);
		lowerFields.put(field.toString().toLowerCase(), field);

		fieldToColumn.put(field, columnName);
		String lastFieldName = lowerColumnToFieldName.put(columnName.toLowerCase(), field.name());
		if (lastFieldName != null && !field.toString().equals(lastFieldName)) {
			throw new IllegalArgumentException(String.format("The field [%s] and [%s] in [%s] have a duplicate column name [%s].", lastFieldName, field.toString(),
					beanType.getName(), columnName));
		}

		if (isPk) {
			type.setPk(true);
			List<ColumnMapping> newPks = new ArrayList<ColumnMapping>(pkFields);
			newPks.add(type);
			Collections.sort(newPks, new Comparator<ColumnMapping>() {
				public int compare(ColumnMapping o1, ColumnMapping o2) {
					Integer i1 = -1;
					Integer i2 = -1;
					if (o1 instanceof Enum) {
						i1 = ((Enum<?>) o1.field()).ordinal();
					}
					if (o2 instanceof Enum) {
						i2 = ((Enum<?>) o2.field()).ordinal();
					}
					return i1.compareTo(i2);
				}
			});
			this.pkFields = Arrays.<ColumnMapping> asList(newPks.toArray(new ColumnMapping[newPks.size()]));
		}
		schemaMap.put(field, type);
		super.updateAutoIncrementAndUpdate(type);
		if (type.isLob()) {
			lobNames = ArrayUtils.addElement(lobNames, field, jef.database.Field.class);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Entity: [").append(beanType.getName()).append("]\n");
		for (ColumnMapping m : schemaMap.values()) {
			String fname = m.fieldName();
			sb.append("  ").append(fname);
			StringUtils.repeat(sb, ' ', 10 - fname.length());
			sb.append('\t').append(m.get());
			sb.append("\n");
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}


	/**
	 * 添加一个非元模型的 Column映射字段（一般用于分表辅助）
	 * 
	 * @param name
	 * @param column
	 */
	public void addNonMetaModelFieldMapping(String field, Column column) {
		String name = column.name();
		if (StringUtils.isEmpty(name)) {
			name = field;
		}
		lowerColumnToFieldName.put(name.toLowerCase(), field);
	}

	void setTableName(String tableName) {
		this.tableName = tableName;
	}

	void setSchema(String schema) {
		this.schema = schema;
	}

	/**
	 * 根据一个指定的实际数据库，核对metaData中的字段，如果发现某些可选的字段数据库里没有，就从元模型中删除来适应数据库
	 * 
	 * @param db
	 * @throws SQLException
	 */
//	public synchronized void removeNotExistColumns(OperateTarget db) throws SQLException {
//		Set<String> set = DebugUtil.getColumnsInLowercase(db, getTableName(true));
//		List<Field> removeColumn = new ArrayList<Field>();
//		for (Field field : fieldToColumn.keySet()) {
//			String column = fieldToColumn.get(field).toLowerCase();
//			if (!set.contains(column)) {
//				removeColumn.add(field);
//			}
//		}
//		for (Field field : removeColumn) {
//			schemaMap.remove(field);
//			// FIXME, others are not removed
//			LogUtil.show("The field [" + field.name() + "] was remove since column not exist in db.");
//		}
//		if (removeColumn.size() > 0)
//			metaFields = null;
//	}

	public Object newInstance() {
		return beanAccessor.newInstance();
	}

	public String getName() {
		return thisType.getName();
	}

	public String getSimpleName() {
		return thisType.getSimpleName();
	}

	public Field getFieldByLowerColumn(String columnLowercase) {
		return fields.get(lowerColumnToFieldName.get(columnLowercase));
	}

	private List<Class> parents;

	public void addParent(Class<?> processingClz) {
		if (parents == null) {
			parents = new ArrayList<Class>(3);
		}
		parents.add(processingClz);

	}

	public boolean containsMeta(EntityMetadata<T> type) {
		if (type == this)
			return true;
		if (parents == null)
			return false;
		for (Class clz : parents) {
			if (type.getThisType() == clz) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Map<String, String> getColumnComments() {
		// 先根据源码解析来获取注解
		Map<String, String> result = getFromSource();
		// 再分析注解中的备注信息
		{
			Comment comment = thisType.getAnnotation(Comment.class);
			if (comment != null) {
				result.put("#TABLE", comment.value());
			}
		}
		for (ColumnMapping column : this.getColumns()) {
			FieldEx field = BeanUtils.getField(thisType, column.fieldName());
			if (field == null) {
				continue;
			}
			Comment comment = field.getAnnotation(Comment.class);
			if (comment != null) {
				result.put(column.fieldName(), comment.value());
			}
		}
		return result;
	}

	/**
	 * 尝试从源码的注释中获得列的注解信息
	 * 
	 * @return
	 */
	private Map<String, String> getFromSource() {
		Map<String, String> result = new HashMap<String, String>();
		Class<?> type = thisType;
		URL url = this.getClass().getResource("/" + type.getName().replace('.', '/') + ".java");
		if (url == null) {
			url = getFixedPathSource(type);
		}

		if (url == null)
			return result;
		try {
			InputStream in = url.openStream();
			try {
				CompilationUnit unit = JavaParser.parse(in, "UTF-8");
				if (unit.getTypes().isEmpty())
					return result;
				TypeDeclaration typed = unit.getTypes().get(0);
				if (typed instanceof ClassOrInterfaceDeclaration) {
					ClassOrInterfaceDeclaration clz = (ClassOrInterfaceDeclaration) typed;
					String table = getContent(clz.getComment());
					if (table != null)
						result.put("#TABLE", table);
					for (BodyDeclaration body : typed.getMembers()) {
						if (body instanceof FieldDeclaration) {
							FieldDeclaration field = (FieldDeclaration) body;
							if (ModifierSet.isStatic(field.getModifiers())) {
								continue;
							}
							if (field.getVariables().size() > 1) {
								continue;
							}
							String name = field.getVariables().get(0).getId().getName();
							String javaDoc = getContent(field.getComment());
							if (javaDoc != null)
								result.put(name, javaDoc);
						}
					}
				}
			} finally {
				IOUtils.close(in);
			}
		} catch (ParseException e) {
			LogUtil.exception(e);
		} catch (IOException e) {
			LogUtil.exception(e);
		}
		return result;
	}

	private String getContent(com.github.javaparser.ast.comments.Comment comment) {
		if (comment == null)
			return null;
		String s = comment.getContent();
		return s.replaceAll("\\s*\\*", "").trim();
	}

	/**
	 * 在开发环境的标准Maven目录场景情况下，寻找到源代码，实现注解信息读取
	 * 
	 * @param type
	 * @return
	 */
	private URL getFixedPathSource(Class type) {
		String clzPath = "/" + type.getName().replace('.', '/') + ".class";
		URL url = this.getClass().getResource(clzPath);
		if (url == null)
			return null;
		String path = url.getPath();
		path = path.substring(0, path.length() - clzPath.length());
		File source = null;
		if (path.endsWith("/target/test-classes")) {
			source = new File(path.substring(0, path.length() - 20), "src/test/java");
		} else if (path.endsWith("/target/classes")) {
			source = new File(path.substring(0, path.length() - 15), "src/main/java");
		}
		if (source == null)
			return null;
		File java = new File(source, type.getName().replace('.', '/') + ".java");
		if (java.exists())
			try {
				return java.toURI().toURL();
			} catch (MalformedURLException e) {
				LogUtil.exception(e);
				return null;
			}
		return null;
	}
}
