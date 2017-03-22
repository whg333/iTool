package com.why.tool.operation;

import com.schooner.MemCached.MemcachedItem;

public interface LimitationRepo {

	MemcachedItem findOperationTime(long userId);
	
	void saveOpenrationTime(long userId, long value);
	
	boolean addOperationTime(long userId, long value);
	
	boolean removeOperationTime(long userId);
	
}
