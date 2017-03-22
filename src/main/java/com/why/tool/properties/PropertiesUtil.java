package com.why.tool.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * 操作properties文件的工具类
 */
public class PropertiesUtil {

	private Properties properties;
	
	private boolean available = true;
	
	/**
	 * 是否是有效的properties文件
	 * @return
	 */
	public boolean available(){
		return available;
	}
	
	public PropertiesUtil(Properties p) {
		this.properties = p;
	}

	/**
	 * 相对于classpath的路径
	 * @param filePath
	 */
	public PropertiesUtil(String filePath) {
		properties = new Properties();
		
		InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(filePath);
		if(in == null){
			in = ClassLoader.getSystemResourceAsStream(filePath);
		}
		
		if(in == null){
			available = false;
		} else {
			try {
				properties.load(in);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public String getProperty(String key) {
		String property = properties.getProperty(key);
		if(property != null){
		    return property.trim();
		}
		return null;
	}
	
	public int getPropertyAsInt(String key) {
		return Integer.parseInt(getProperty(key));
	}

	public boolean getPropertyAsBoolean(String key) {
		return Boolean.parseBoolean(getProperty(key));
	}

	public byte getPropertyAsByte(String key) {
		return Byte.parseByte(getProperty(key));
	}

	public long getPropertyAsLong(String key) {
		return Long.parseLong(getProperty(key));
	}

	public short getPropertyAsShort(String key) {
		return Short.parseShort(getProperty(key));
	}

	public float getPropertyAsFloat(String key) {
		return Float.parseFloat(getProperty(key));
	}

	public double getPropertyAsDouble(String key) {
		return Double.parseDouble(getProperty(key));
	}

	public Set<Object> keySet() {
	    return properties.keySet();
	}
	
}
