/**
 *    Copyright 2015-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.geequery.spring.boot.autoconfigure;

import java.util.Properties;

import jef.common.log.LogUtil;
import jef.database.DbCfg;
import jef.database.ORMConfig;
import jef.database.support.DbInitHandler;
import jef.tools.JefConfiguration;

import org.easyframe.enterprise.spring.TransactionMode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for GeeQuery.
 *
 * @author Joey
 */
@ConfigurationProperties(prefix = GeeQueryProperties.GQ_PREFIX)
public class GeeQueryProperties {

	public static final String GQ_PREFIX = "geequery";

	/**
	 * 命名查询所在的文件
	 */
	private String namedQueryFile;

	/**
	 * 命名查询所在的表
	 */
	private String namedQueryTable;

	/**
	 * 事务支持类型
	 * 
	 * @see #setTransactionMode(String)
	 */
	private TransactionMode transactionMode = TransactionMode.JPA;

	/**
	 * 指定扫描若干包,配置示例如下—— <code><pre>
	 * &lt;list&gt;
	 *  &lt;value&gt;org.easyframe.test&lt;/value&gt;
	 *  &lt;value&gt;org.easyframe.entity&lt;/value&gt;
	 * &lt;/list&gt;
	 * </pre></code>
	 */
	private String[] packagesToScan;

	/**
	 * 扫描已知的若干注解实体类，配置示例如下—— <code><pre>
	 * &lt;list&gt;
	 *  &lt;value&gt;org.easyframe.testp.jta.Product&lt;/value&gt;
	 *  &lt;value&gt;org.easyframe.testp.jta.Users&lt;/value&gt;
	 * &lt;/list&gt;
	 * </pre></code>
	 */
	private String[] annotatedClasses;

	/**
	 * 指定扫描若干表作为动态表，此处配置表名，名称之间逗号分隔
	 */
	private String dynamicTables;

	/**
	 * 是否将所有未并映射的表都当做动态表进行映射。
	 */
	private boolean registeNonMappingTableAsDynamic;

	/**
	 * 扫描到实体后，如果数据库中不存在，是否建表 <br>
	 * 默认开启
	 */
	private boolean createTable = true;

	/**
	 * 扫描到实体后，如果数据库中存在对应表，是否修改表 <br>
	 * 默认开启
	 */
	private boolean alterTable = true;

	/**
	 * 扫描到实体后，如果准备修改表，如果数据库中的列更多，是否允许删除列 <br>
	 * 默认关闭
	 */
	private boolean allowDropColumn = false;

	/**
	 * 自定义一个类，当数据库连上后干一些初始化的事情。
	 * 
	 * @see DbInitHandler
	 */
	private String dbInitHandler;
	/**
	 * 在建表后插入初始化数据
	 * <p>
	 * 允许用户在和class相同的位置创建一个 <i>class-name</i>.txt的文件，记录了表中的初始化数据。
	 * 开启此选项后，在初始化建表时会插入这些数据。
	 */
	private boolean initData = true;
	
	
	private boolean showSql = true;
	

	/**
	 * 是否使用数据库初始化记录表
	 */
	private boolean useDataInitTable = JefConfiguration.getBoolean(DbCfg.USE_DATAINIT_FLAG_TABLE, false);;
	
	/**
	 * 初始化数据文件扩展名
	 */
	private String initDataExtension=JefConfiguration.get(DbCfg.INIT_DATA_EXTENSION, "txt");;

	private String initDataCharset = "UTF-8";

	public boolean isCacheDebug() {
		return ORMConfig.getInstance().isCacheDebug();
	}

	public void setCacheDebug(boolean cacheDebug) {
		ORMConfig.getInstance().setCacheDebug(cacheDebug);
	}

	public boolean isCacheLevel1() {
		return ORMConfig.getInstance().isCacheLevel1();
	}

	/**
	 * 
	 * @param cache
	 * @return this
	 * @see DbCfg#CACHE_LEVEL_1
	 */
	public void setCacheLevel1(boolean cache) {
		ORMConfig.getInstance().setCacheLevel1(cache);
	}

	public String getInitDataCharset() {
		return initDataCharset;
	}

	public void setInitDataCharset(String initDataCharset) {
		this.initDataCharset = initDataCharset;
	}

	public String getDbInitHandler() {
		return dbInitHandler;
	}

	public void setDbInitHandler(String dbInitHandler) {
		this.dbInitHandler = dbInitHandler;
	}

	/**
	 * 获得全局缓存的生存时间，单位秒。 全局缓存是类似于一级缓存的一个存储结构，可以自动分析数据库操作关联性并进行数据刷新。
	 * 
	 * @return 秒数
	 * @see DbCfg#CACHE_GLOBAL_EXPIRE_TIME
	 */
	public int getGlobalCacheLiveTime() {
		return ORMConfig.getInstance().getCacheLevel2();
	}

	/**
	 * 设置全局缓存的生存时间，单位秒。 全局缓存是类似于一级缓存的一个存储结构，可以自动分析数据库操作关联性并进行数据刷新。
	 * 
	 * @param second
	 * @return this
	 * @see DbCfg#CACHE_GLOBAL_EXPIRE_TIME
	 */
	public void setGlobalCacheLiveTime(int second) {
		ORMConfig.getInstance().setCacheLevel2(second);
	}

	public void setUseSystemOut(boolean flag) {
		LogUtil.useSlf4j = !flag;
	}

	/**
	 * 是否启用数据初始化信息记录表。 如果启用，会自动在数据库中创建表 allow_data_initialize，其中
	 * do_init设置为0时，启动时不进行数据初始化。 如果设置为1，启动时进行数据初始化。
	 * 
	 * @return whether use the datainit table or not.
	 */
	public boolean isUseDataInitTable() {
		return useDataInitTable;
	}

	/**
	 * 设置是否启用数据初始化信息记录表。 如果启用，会自动在数据库中创建表 allow_data_initialize，其中
	 * do_init设置为0时，启动时不进行数据初始化。 如果设置为1，启动时进行数据初始化。
	 * 
	 * @param useDataInitTable
	 *            whether use the datainit table or not.
	 * @return this
	 */
	public void setUseDataInitTable(boolean useDataInitTable) {
		this.useDataInitTable = useDataInitTable;
	}

	// private static final ResourcePatternResolver resourceResolver = new
	// PathMatchingResourcePatternResolver();

	/**
	 * Externalized properties for gqquery configuration.
	 */
	private Properties configurationProperties;

//	/**
//	 * A Configuration object for customize default settings. If
//	 * {@link #configLocation} is specified, this property is not used.
//	 */
//	@NestedConfigurationProperty
//	private ORMConfig configuration;

	/**
	 * @since 1.2.0
	 */
	public Properties getConfigurationProperties() {
		return configurationProperties;
	}

	/**
	 * @since 1.2.0
	 */
	public void setConfigurationProperties(Properties configurationProperties) {
		this.configurationProperties = configurationProperties;
	}

	public String getNamedQueryFile() {
		return namedQueryFile;
	}

	public void setNamedQueryFile(String namedQueryFile) {
		this.namedQueryFile = namedQueryFile;
	}

	public String getNamedQueryTable() {
		return namedQueryTable;
	}

	public void setNamedQueryTable(String namedQueryTable) {
		this.namedQueryTable = namedQueryTable;
	}

	public TransactionMode getTransactionMode() {
		return transactionMode;
	}

	public void setTransactionMode(TransactionMode transactionMode) {
		this.transactionMode = transactionMode;
	}

	public String[] getPackagesToScan() {
		return packagesToScan;
	}

	public void setPackagesToScan(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public String[] getAnnotatedClasses() {
		return annotatedClasses;
	}

	public void setAnnotatedClasses(String[] annotatedClasses) {
		this.annotatedClasses = annotatedClasses;
	}

	public String getDynamicTables() {
		return dynamicTables;
	}

	public void setDynamicTables(String dynamicTables) {
		this.dynamicTables = dynamicTables;
	}

	public boolean isRegisteNonMappingTableAsDynamic() {
		return registeNonMappingTableAsDynamic;
	}

	public void setRegisteNonMappingTableAsDynamic(boolean registeNonMappingTableAsDynamic) {
		this.registeNonMappingTableAsDynamic = registeNonMappingTableAsDynamic;
	}

	public boolean isCreateTable() {
		return createTable;
	}

	public void setCreateTable(boolean createTable) {
		this.createTable = createTable;
	}

	public boolean isAlterTable() {
		return alterTable;
	}

	public void setAlterTable(boolean alterTable) {
		this.alterTable = alterTable;
	}

	public boolean isAllowDropColumn() {
		return allowDropColumn;
	}

	public void setAllowDropColumn(boolean allowDropColumn) {
		this.allowDropColumn = allowDropColumn;
	}

	public boolean isInitData() {
		return initData;
	}

	public void setInitData(boolean initData) {
		this.initData = initData;
	}
//	public ORMConfig getConfiguration() {
//		return configuration;
//	}
//
//	public void setConfiguration(ORMConfig configuration) {
//		this.configuration = configuration;
//	}

	public boolean isShowSql() {
		return showSql;
	}

	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
	}

	public String getInitDataExtension() {
		return initDataExtension;
	}

	public void setInitDataExtension(String initDataExtension) {
		this.initDataExtension = initDataExtension;
	}
}
