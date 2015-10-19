package com.why.tool.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CASTest {

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
	private static volatile int record = 0;
	
	public static void main(String[] args) throws InterruptedException{
		System.out.println("calSum:"+calSum());
		
		for(int i=0;i<threadNum;i++){
			int begin = length/threadNum*i + 1;
			int end = length/threadNum*(i+1);
			//System.out.println("begin:"+begin+",end:"+end);
			new CollectThread(begin, end).start();
		}
		
		while(record != threadNum){
			TimeUnit.SECONDS.sleep(1);
		}
		
		System.out.println("sum:"+sum.get());
	}
	
	private static class CollectThread extends Thread{

		private final int begin;
		private final int end;
		
		CollectThread(int begin, int end){
			this.begin = begin;
			this.end = end;
		}
		
		@Override
		public void run() {
			for(int i=begin;i<=end;i++){
				int val = sum.addAndGet(i);
				//System.out.println(val);
			}
			record++;
		}
		
	}
	
}
