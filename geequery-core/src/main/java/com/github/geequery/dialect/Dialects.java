package com.github.geequery.dialect;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.github.geequery.common.log.LogUtil;
import com.github.geequery.core.config.DbCfg;
import com.github.geequery.tools.IOUtils;
import com.github.geequery.tools.JefConfiguration;
import com.github.geequery.tools.StringUtils;

public class Dialects {
	/**
	 * 所有已经构建的Dialect，缓存
	 */
	private static final Map<String, DatabaseDialect> ITEMS = new HashMap<String, DatabaseDialect>();
	/**
	 * 根据RDBMS名称获得数据库方言
	 * 
	 * @param dbmsName
	 * @return
	 */
	public static DatabaseDialect getDialect(String dbmsName) {
		dbmsName = dbmsName.toLowerCase();
		DatabaseDialect profile = ITEMS.get(dbmsName);
		if (profile != null)
			return profile;
		profile = lookupDialect(dbmsName);
		return profile;
	}

	private synchronized static DatabaseDialect lookupDialect(String dbmsName) {
		Map<String, String> dialectMappings = initDialectMapping();
		String classname = dialectMappings.remove(dbmsName);
		if (classname == null) {
			throw new IllegalArgumentException("the dbms '" + dbmsName + "' is not supported yet");
		}
		try {
			Class<?> c = Class.forName(classname);
			DatabaseDialect result = (DatabaseDialect) c.newInstance();
			ITEMS.put(dbmsName, result);
			return result;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			LogUtil.exception(e);
			throw new IllegalArgumentException("the Dialect class can't be created:" + classname);
		}
	}

	private static Map<String, String> initDialectMapping() {
		URL url = Dialects.class.getResource("/META-INF/dialect-mapping.properties");
		if (url == null) {
			LogUtil.fatal("Can not found Dialect Mapping File. /META-INF/dialect-mapping.properties");
		}
		Map<String, String> config = IOUtils.loadProperties(url);
		String file = JefConfiguration.get(DbCfg.DB_DIALECT_CONFIG);
		if (StringUtils.isNotEmpty(file)) {
			url = Dialects.class.getClassLoader().getResource(file);
			if (url == null) {
				LogUtil.warn("The custom dialect mapping file [{}] was not found.", file);
			} else {
				Map<String, String> config1 = IOUtils.loadProperties(url);
				config.putAll(config1);
			}
		}
		return config;
	}
}
