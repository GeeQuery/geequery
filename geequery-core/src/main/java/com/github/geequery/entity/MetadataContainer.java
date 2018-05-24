package com.github.geequery.entity;

/**
 * 所有自带EntityMetadata信息的字段
 * 
 * @author jiyi
 *
 */
public interface MetadataContainer {
	/**
	 * 获得所在表的Metadata信息。
	 * 
	 * @return
	 */
	<T>  EntityMetadata<T> getMeta();
}
