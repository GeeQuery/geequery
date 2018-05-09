package com.github.geequery.tools.csvreader;

public interface Codec<T> {
	String toString(T t);

	T fromString(String s);
}
