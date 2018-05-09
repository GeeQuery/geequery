package com.github.geequery.accelerator.bean;

import java.lang.reflect.Type;

import com.github.geequery.tools.reflect.Property;

/**
 * 适用hash算法
 * @author Administrator
 *
 */
public abstract class AbstractFastProperty implements Property{
	protected Type genericType;
	protected Class<?> rawType;
	public int n;
	public boolean isReadable() {
		return true;
	}

	public boolean isWriteable() {
		return true;
	}
	public Class<?> getType() {
		return rawType;
	}
	public Type getGenericType() {
		return genericType;
	}
}
