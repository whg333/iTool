package com.why.tool.net;

/**
 * HTTP响应的属性
 * @author qizhi
 *
 */
public class ResponseProperty {

    private int responseCode;// HTTP响应消息状态码
    private String responseMessage;// 与来自服务器的响应代码一起返回的 HTTP响应消息（如果有）
    private String content;// HTTP连接获取的内容

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
