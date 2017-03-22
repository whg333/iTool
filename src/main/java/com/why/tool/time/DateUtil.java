package com.why.tool.time;

import java.text.ParseException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class DateUtil {

	private static final long time_difference = TimeUnit.HOURS.toMillis(5);
	
	private static final long actual_time_disfference = TimeUnit.HOURS.toMillis(8);
	
	public static final String DAY = "yyyy-MM-dd";
	
	public static final String SECONDS = "HH:mm:ss";
	
	public static final String DAY_SECONDS = "yyyy-MM-dd HH:mm:ss";
	
	public static final String YEAR_MONTH = "yyyyMM";
	
	public static final String YEAR_MONTH_DAY = "yyyyMMdd";
	
	public static long currentTimeMimute(){
		return timeMimute(TimeUtil.currentTimeMillis());
	}
	
	public static long timeMimute(long millis){
		return TimeUnit.MILLISECONDS.toMinutes(millis);
	}
	
	/** 四舍五入时间到当前分钟上 */
	public static long roundMinuteCurrentTimeMillis(){
		long now = TimeUtil.currentTimeMillis();
		return roundMinuteTimeMillis(now);
	}
	
	/** 四舍五入时间到分钟上 */
	public static long roundMinuteTimeMillis(long time){
		Calendar nowCal = Calendar.getInstance();
		nowCal.setTimeInMillis(time);
		long nowSecond = nowCal.get(Calendar.SECOND);
		
		long mimutes = TimeUnit.MILLISECONDS.toMinutes(time);
		if(nowSecond > 30){
			mimutes++;
		}
		return TimeUnit.MINUTES.toMillis(mimutes);
	}
	
	/** 当前小时的毫秒 */
	public static long currentHourMillis(){
		return hourMillis(TimeUtil.currentTimeHour());
	}
	
	public static long hourMillis(long hour){
		return TimeUnit.HOURS.toMillis(hour);
	}
	
	public static long hour(long milli){
		return TimeUnit.MILLISECONDS.toHours(milli);
	}
	
	public static String f(long millis, String format){
		return DateUtils.format(millis, format);
	}
	
/*	public static int getTodayIntValue(long millis) {
        return (int) TimeUnit.MILLISECONDS.toDays(millis);
    }*/
	
	/**
     * 返回当前距1970年1月1日的天数(3点为界)
     * 
     * @return
     */
    public static int getTodayIntValue() {
        return (int) TimeUnit.MILLISECONDS.toDays(currentTimeMillisAtBeijing());
    }
    
    /**
     * 返回nDaysAtferToday距1970年1月1日的天数(0点为界)
     * 
     * @return
     */
    public static int getBeiJingCurrentDayIntValue() {
        return (int) (TimeUnit.MILLISECONDS.toDays(currentTimeMillisAtBeijingActual()));
    }
	
	/**
     * 返回nDaysAtferToday距1970年1月1日的天数(0点为界)
     * 
     * @return
     */
    public static int getBeiJingBeforeDayIntValue(int nDaysAfterToday) {
        return (int) (TimeUnit.MILLISECONDS.toDays(currentTimeMillisAtBeijingActual()) - nDaysAfterToday);
    }
    
    /**
     * 返回nDaysAtferToday距1970年1月1日的天数(0点为界)
     * 
     * @return
     */
    public static int getBeiJingAfterDayIntValue(int nDaysAfterToday) {
        return (int) (TimeUnit.MILLISECONDS.toDays(currentTimeMillisAtBeijingActual()) + nDaysAfterToday);
    }
    
    /**
     * 当前时间加上5小时的时差（3点为界）
     * 
     * @return
     */
    public static long currentTimeMillisAtBeijing() {
        return TimeUtil.currentTimeMillis() + time_difference;
    }
    
    /**
     * 当前时间加上8小时的时差（0点为界）
     * 
     * @return
     */
    private static long currentTimeMillisAtBeijingActual() {
        return TimeUtil.currentTimeMillis() + actual_time_disfference;
    }
    
    public static String currentYearMonthDay(){
    	return f(TimeUtil.currentTimeMillis(), YEAR_MONTH_DAY);
    }
    
    public static String currentYearMonth(){
    	return f(TimeUtil.currentTimeMillis(), YEAR_MONTH);
    }
    
    public static String currentYearMonthDaySeconds(long time){
    	return f(time, DAY_SECONDS);
    }
    
    public static long parseTime(String timeStr){
    	try {
			return DateUtils.parse(timeStr, DAY_SECONDS).getTime();
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
    }
    
    public static long parseDayTime(String dayTimeStr){
    	try {
			return DateUtils.parse(dayTimeStr, DAY).getTime();
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
    }
    
    public static int getTodayDay(){
    	Calendar now = Calendar.getInstance();
    	now.setTimeInMillis(currentTimeMimute());
    	return now.get(Calendar.DAY_OF_MONTH);
    } 
    
    
    public static int getTodayDayByTime(long millis){
    	Calendar now = Calendar.getInstance();
    	now.setTimeInMillis(millis);
    	return now.get(Calendar.DAY_OF_YEAR);
    } 
    
    public static String getTowMonthAgoYearMonth(){
    	Calendar now = Calendar.getInstance();  
    	now.setTimeInMillis(currentTimeMimute());
		now.add(Calendar.MONTH, -2);
		return f(now.getTimeInMillis(), YEAR_MONTH);
    }
    
    
    /**
     * 计算两时间之间天数 以三点为界限
     * @param satrTime	开始时间
     * @param endTime	结束时间
     * @return	两时间之间天数
     */
    @Deprecated
    public static int towDayTimeByThreeHour(long satrTime,long endTime){
		Calendar starTimeCalendar = Calendar.getInstance();
		starTimeCalendar.setTimeInMillis(satrTime);
		int starHour = starTimeCalendar.get(Calendar.HOUR_OF_DAY);
		if (starHour < 3) {
			starTimeCalendar.setTimeInMillis(satrTime - TimeUnit.DAYS.toMillis(1));
		} else { //以凌晨3点为界作为第二天的开始
			starTimeCalendar.setTimeInMillis(satrTime);
		}
		
		Calendar endTimeCalendar = Calendar.getInstance();
		endTimeCalendar.setTimeInMillis(endTime);
		int endHour = endTimeCalendar.get(Calendar.HOUR_OF_DAY);
		if (endHour < 3) {
			endTimeCalendar.setTimeInMillis(endTime - TimeUnit.DAYS.toMillis(1));
		} else { //以凌晨3点为界作为第二天的开始
			endTimeCalendar.setTimeInMillis(endTime);
		}
		int day = endTimeCalendar.get(Calendar.DAY_OF_YEAR) - starTimeCalendar.get(Calendar.DAY_OF_YEAR);
		/*System.out.println("createTime:"+ DateUtils.format(satrTime, "yyyy-MM-dd HH:mm:ss"));
		System.out.println("endTime:"+ DateUtils.format(endTime, "yyyy-MM-dd HH:mm:ss"));
		System.err.println("day:" + day);*/
		return day;
	}
    
    @Deprecated
    public static long changeTimeToThreeHour(long time){
    	String timeStr = DateUtils.format(time, "yyyy-MM-dd");
    	String threeTimeStr = " 03:00:00";
    	String newTimeStr = timeStr + threeTimeStr;
    	long t = parseTime(newTimeStr);
    	if(time < t){
    		Calendar timeCalendar = Calendar.getInstance();
    		timeCalendar.setTimeInMillis(t + TimeUnit.DAYS.toMillis(-1));
    		t = timeCalendar.getTimeInMillis();
    	}
    	return t;
    }
    
    public static void main(String[] args) {
    	/*System.out.println(TimeUtil.currentTimeMillis());
		System.out.println(f(TimeUnit.SECONDS.toMillis(1478598888L), DAY_SECONDS));
		
		int currDay = getTodayIntValue();
		System.out.println(currDay);
		System.out.println(f(TimeUnit.DAYS.toMillis(17116), DAY_SECONDS));
		
		Date date = new Date(System.currentTimeMillis());
		Calendar now = Calendar.getInstance();  
		
		now.add(Calendar.MONTH, -2);
		System.err.println("年: " + now.get(Calendar.YEAR));  
        System.err.println("月: " + (now.get(Calendar.MONTH) + 1) + "");  
        System.err.println("日: " + now.get(Calendar.DAY_OF_MONTH));  
        System.err.println("时: " + now.get(Calendar.HOUR_OF_DAY));  
        System.err.println("分: " + now.get(Calendar.MINUTE));  
        System.err.println("秒: " + now.get(Calendar.SECOND));  
        System.err.println("当前时间毫秒数：" + now.getTimeInMillis());  
        System.err.println(now.getTime());  
        
        System.err.println(getTowMonthAgoYearMonth());*/
    	long time = DateUtil.parseTime("2017-01-17 02:59:53");
    	long time2 = DateUtil.parseTime("2017-01-17 03:00:03");
    	
    	//System.err.println(towDayTimeByThreeHour(time2, Activities.calEndTime(time, -1)));
	}
	
}
