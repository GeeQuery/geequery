package jef.database.meta;

import com.github.geequery.entity.IQueryableEntity;

public interface ExtensionConfigFactory {
	/**
	 * 
	 * @param q the instance.
	 * @return
	 */
	ExtensionConfig getExtension(IQueryableEntity q);
	/**
	 * 得到扩展的数据
	 * @return
	 */
	ExtensionConfig getExtension(String extensionName);
	
	/**
	 * 得到扩展的数据
	 * @return
	 */
	ExtensionConfig getDefault();
}
