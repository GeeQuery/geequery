package com.github.geequery.core;

import com.github.geequery.DbMetaData;


/**
 * 
 * @author jiyi
 *
 */
public interface SessionFactory {
	
	/**
	 * 
	 * @return
	 */
	Session getSession();
	
	DbMetaData getMetaData();
}
