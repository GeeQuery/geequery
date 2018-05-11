package com.github.geequery;

import com.querydsl.core.types.Path;

/**
 * EF当中的Field，其实就是PathType为PROPERTY的Path
 * @author jiyi
 *
 * @param <T>
 */
@Deprecated
public interface Field<T> extends Path<T>{
}
