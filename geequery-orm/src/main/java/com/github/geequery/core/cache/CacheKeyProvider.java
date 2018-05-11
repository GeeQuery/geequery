package com.github.geequery.core.cache;

/**
 * CacheKey的提供者
 * @author jiyi
 *
 */
@FunctionalInterface
public interface CacheKeyProvider {
	CacheKey getCacheKey();
}
