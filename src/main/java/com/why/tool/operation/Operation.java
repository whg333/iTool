package com.why.tool.operation;

import org.apache.log4j.spi.ErrorCode;

import com.schooner.MemCached.MemcachedItem;
import com.why.tool.exception.BusinessException;
import com.why.tool.time.TimeUtil;

public class Operation {

    private LimitationRepo limitationRepo;
    
    private long userId;
    
    private boolean checked = false;
    
    private long currentTime = TimeUtil.currentTimeMillis();
    
    public Operation(long userId) {
        this.userId = userId;
    }
    
    public void setLimitationRepo(LimitationRepo limitationRepo) {
        this.limitationRepo = limitationRepo;
    }

    /** 限制用户1秒内不能同时进行多次操作，结合当前工作线程和数据库实现 */
    public void checkAndLock() {
        if(checked) return;
        
        if(lockTime()) {
        	return;
        }
        
        MemcachedItem item = limitationRepo.findOperationTime(userId);
        if (item == null && lockTime()) {
            return;
        } else if (item != null) {
        	long expiretime = (Long) (item.getValue());
        	//TODO 所以当用户的某次操作在1秒内未执行完的话，我们是不让他进行下次操作呢？还是缓存在一个操作队列中？
        	//TODO 1秒都未执行完的话，或许可以认为要么业务执行代码写得有问题或者DB层面出问题了导致的响应过慢
        	//System.out.println("currentTime="+DateUtil.f(currentTime, DateUtil.DAY_SECONDS)+", expiretime="+DateUtil.f(expiretime, DateUtil.DAY_SECONDS));
	        if ((currentTime > (expiretime + 1000) || (expiretime > currentTime + 1000))) {
	            limitationRepo.saveOpenrationTime(userId, currentTime);
	            return;
	        }
        }
        
        //throw new BusinessException(ErrorCode.CAN_NOT_OPERATING_MUTLI_THINGS);
        throw new IllegalAccessError("CAN_NOT_OPERATING_MUTLI_THINGS");
    }

	private boolean lockTime() {
		boolean added = limitationRepo.addOperationTime(userId, currentTime);
        if(added){
            checked = true;
            return true;
        }
        return false;
	}

    public void unlock(){
        limitationRepo.removeOperationTime(userId);
    }
    
}
