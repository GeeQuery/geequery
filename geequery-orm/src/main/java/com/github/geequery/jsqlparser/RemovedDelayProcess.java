package com.github.geequery.jsqlparser;

import com.github.geequery.jsqlparser.statement.select.Limit;
import com.github.geequery.jsqlparser.statement.select.StartWithExpression;

public class RemovedDelayProcess {
	public StartWithExpression startWith;
	public Limit limit;

	public RemovedDelayProcess(Limit delayLimit, StartWithExpression delayStartWith) {
		this.limit = delayLimit;
		this.startWith = delayStartWith;
	}

	public boolean isValid() {
		return startWith != null || limit != null;
	}
}
