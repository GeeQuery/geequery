package jef.database.meta;

import com.github.geequery.annotation.PartitionTable;
import com.github.geequery.entity.IQueryableEntity;

/**
 * 分表规则加载器
 * @author Administrator
 *
 */
public interface PartitionStrategyLoader {
	PartitionTable get(Class<? extends IQueryableEntity> clz);
}
