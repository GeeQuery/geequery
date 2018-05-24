package com.github.geequery.common.wrapper;

import java.io.Serializable;

public interface IHolder<T> extends Serializable{
	T get();
	void set(T obj);
}
