package com.github.geequery.entity;

import java.util.Map;

import com.github.geequery.common.log.LogUtil;
import com.github.geequery.tools.StringUtils;

public class MetadataFacade {
	public void clearMetadatas() {
		MetaHolder.clear();
	}

	public String getSchemaMapping() {
		return StringUtils.toString(MetaHolder.SCHEMA_MAPPING,",",":");
	}

	public void setSchemaMapping(String data) {
		Map<String,String> map=StringUtils.toMap(data,",", ":", 1);
		LogUtil.show("Update schema mapping to "+map);
		MetaHolder.SCHEMA_MAPPING.clear();
		MetaHolder.SCHEMA_MAPPING.putAll(map);
	}

	public String getSiteMapping() {
		return StringUtils.toString(MetaHolder.SITE_MAPPING,",",":");
	}

	public void setSiteMapping(String data) {
		Map<String,String> map=StringUtils.toMap(data,",", ":", -1);
		LogUtil.show("Update site mapping to "+map);
		MetaHolder.SITE_MAPPING.clear();
		MetaHolder.SITE_MAPPING.putAll(map);
	}
	
	public DefaultMetaLoader getDefaultMeta(){
		return DefaultMetaLoader.instance;
	}
	
}
