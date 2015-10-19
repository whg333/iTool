package com.why.tool.bit;

public class BitUtils {
	
	/**
	 * 把右数第index位变成1     
	 * @param number
	 * @param index  位置从1开始
	 * @return    
	 */
	public static int  fromTheRighTo1(int number,int index){
		return number | (1<<(index-1));
	}
	
	/**
	 * 把右数第index位变成0   
	 * @param number  
	 * @param index  位置从1开始
	 * @return
	 */
	public static int  fromTheRighTo0(int number,int index){
		return number &~ (1<<(index-1));
	}
	
	
	/**
	 * 返回从右数第index位的 值
	 * @param number
	 * @param index
	 * @return
	 */
	public static int indexOf(int number,int index){
		return number>>(index-1)&1;
	}

}
