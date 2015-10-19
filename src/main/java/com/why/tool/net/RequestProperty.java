package com.why.tool.net;

/**
 * 
 * http请求的rqeust Property
 * 
 * header?
 * 
 * @author luzj
 *
 */
public class RequestProperty {
    
    private final String key;
    
    private final String value;
    
    public RequestProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
    
}
