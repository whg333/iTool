package com.why.tool.operation;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>描述：为每个Http工作线程绑定对应的Http Header内容，用于验证Http请求是否包含token</p>
 * @author whg
 * @date 2016-3-18 下午07:24:40
 */
public class HttpHeaderContext {

	private static ThreadLocal<HttpServletRequest> httpRequests = new ThreadLocal<HttpServletRequest>();
	private static ThreadLocal<Map<String, String>> httpHeaders = new ThreadLocal<Map<String, String>>();
	
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = httpRequests.get();
		if(request == null){
			return null;
		}
		return request;
	}
	
	public static String getHeader(String key) {
		Map<String, String> httpHeaderMap = httpHeaders.get();
		if(httpHeaderMap == null){
			return null;
		}
		return httpHeaderMap.get(key);
	}
	
	public static void addHttp(HttpServletRequest request, Map<String, String> httpHeaderMap){
		//System.out.println(Thread.currentThread().getName()+" addHeaders...");
		httpRequests.set(request);
		httpHeaders.set(httpHeaderMap);
	}
	
	public static void removeHttp(){
		httpRequests.remove();
		httpHeaders.remove();
		//System.out.println(Thread.currentThread().getName()+" removeHeaders...");
	}
	
}
