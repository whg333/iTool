package com.why.tool.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CASTest2 {

	private static final int threadNum = 5;
	private static final int length = 1000000;
	
	private static final int[] nums = new int[length];
	static{
		for(int i=1;i<=length;i++){
			nums[i-1] = i;
		}
	}
	
	private static int calSum(){
		int sum = 0;
		for(int i=0;i<length;i++){
			sum += nums[i];
		}
		return sum;
	}
	
	private static final AtomicInteger sum = new AtomicInteger();
	private static volatile AtomicInteger record = new AtomicInteger();
	
	public static void main(String[] args) throws InterruptedException{
		System.out.println("calSum:"+calSum());
		
		for(int i=0;i<threadNum;i++){
			new CollectThread().start();
		}
		
		while(record.get() != length){
			TimeUnit.SECONDS.sleep(1);
		}
		
		System.out.println("sum:"+sum.get());
	}
	
	private static class CollectThread extends Thread{

		@Override
		public void run() {
			//有check操作，不对record加锁的话可能会被出现线程切换导致出错，所以还不如CASTest的做法
			synchronized (record) {
				while(record.get() < length){
					int val = record.getAndIncrement();
					//System.out.println(val);
					sum.addAndGet(nums[val]);
				}
			}
		}
		
	}
	
}
