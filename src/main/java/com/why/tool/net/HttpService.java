package com.why.tool.net;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import com.why.tool.string.StringUtil;


public class HttpService {
    
    private static int read_time_out = 10000;// 默认10秒超时
    
    public HttpService() {
        super();
    }
    
    public HttpService(int timeout) {
        read_time_out = timeout;
    }

	public String doPost(String url, String postData, RequestProperty... rps){
        return service(false, url, postData, "POST", rps);
    }

    public String doPost(String url, String postData, String charSet, RequestProperty... rps){
        return service(false, url, postData, "POST", charSet, rps);
    }
    
    /**
     * Post请求,自动判断是否为https连接,返回响应内容实体
     * @param url 连接地址，开头需要协议名，例如：http, https
     * @param bodayData 请求数据
     * @param charSet 发送请求以及接收响应的内容的字符编码
     * @param rps 请求头信息
     * @return 包含响应内容、状态码等属性的对象
     */
    public ResponseProperty doPostOfResponse(String url, String bodayData, String charSet, RequestProperty... rps){
        if(url == null) throw new IllegalArgumentException("url is null!");
        url = url.trim();
        return serviceOfResponse(isHTTPS(url), url, bodayData, "POST", charSet, rps);
    }

    /**
     * Get请求,自动判断是否为https连接,返回响应内容实体
     * @param url 带有参数的连接地址，开头需要协议名，例如：http, https
     * @param charSet 发送请求以及接收响应的内容的字符编码
     * @param rps 请求头信息
     * @return 包含响应内容、状态码等属性的对象
     */
    public ResponseProperty doGetOfResponse(String url, String charSet, RequestProperty... rps){
        if(url == null) throw new IllegalArgumentException("url is null!");
        url = url.trim();
        return serviceOfResponse(isHTTPS(url), url, null, "GET", charSet, rps);
    }

    /**
     * Put请求（用于RESTful的需求）,自动判断是否为https连接,返回响应内容实体
     * @param url 带有参数的连接地址，开头需要协议名，例如：http, https
     * @param bodyData 请求数据
     * @param charSet 发送请求以及接收响应的内容的字符编码
     * @param rps 请求头信息
     * @return 包含响应内容、状态码等属性的对象
     */
    public ResponseProperty doPutOfResponse(String url, String bodyData, String charSet, RequestProperty... rps){
        if(url == null) throw new IllegalArgumentException("url is null!");
        url = url.trim();
        return serviceOfResponse(isHTTPS(url), url, bodyData, "PUT", charSet, rps);
    }
    
    private boolean isHTTPS(String url){
        char c = url.charAt(4);
        return c == 's' || c == 'S';
    }
    
    public String doGet(String url, RequestProperty... rps){
        return service(false, url, null, "GET", rps);
    }
    
    public String doHttpsPost(String url, String postData) {
    	return service(true, url, postData, "POST");
    }

    public String doHttpsPostEvaluateRespCode(String url, String postData, RequestProperty... rps) {
        return evaluateResponseCodeService(true, url, postData, "POST", rps);
    }
    
    public String doHttpsGet(String url) {
    	return service(true, url, null, "GET");
    }

    private String service(boolean isHttps, String url, String postData, String method, RequestProperty... rps){
            HttpURLConnection conn = null;
            try {
                boolean doOutput = !StringUtil.isEmpty(postData);
                conn = isHttps ? createHttpsConn(url, method, doOutput) : createHttpConn(url, method, doOutput);
                
                fillProperties(conn, rps);
                
                if(doOutput) writeMsg(conn, postData);
                
                return readMsg(conn);
            } catch (Exception ex) {
                //ingore 
                //just print out
                ex.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                    conn = null;
                }
            }
        return null;
    }

    private String service(boolean isHttps, String url, String postData, String method, String charSet, RequestProperty... rps){
        HttpURLConnection conn = null;
        try {
            boolean doOutput = !StringUtil.isEmpty(postData);
            conn = isHttps ? createHttpsConn(url, method, doOutput) : createHttpConn(url, method, doOutput);

            fillProperties(conn, rps);

            if(doOutput) writeMsg(conn, postData);

            return readMsg(conn, charSet);
        } catch (Exception ex) {
            //ingore
            //just print out
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return null;
    }
    
    /**
     * HTTP连接服务，返回更多响应内容
     * @param isHttps
     * @param url
     * @param postData Post请求body体内容，如果参数为null或者空串则被忽略
     * @param method
     * @param charSet 发送请求的内容以及返回响应的内容的字符编码，默认为"UTF8"
     * @param rps 请求的Header属性
     * @return 返回响应内容对象{@link ResponseProperty}，也可能返回null
     */
    private ResponseProperty serviceOfResponse(boolean isHttps, String url, String postData, String method, String charSet, 
            RequestProperty... rps){
        ResponseProperty rp = new ResponseProperty();
        HttpURLConnection conn = null;
        try {
            //1. create connection
            boolean doOutput = !StringUtil.isEmpty(postData);
            conn = isHttps ? createHttpsConn(url, method, doOutput) : createHttpConn(url, method, doOutput);
            //2. set request headers
            fillProperties(conn, rps);
            //3. write body content of post request
            if(StringUtil.isBlank(charSet)){
                charSet = "UTF8";
            }
            if(doOutput) writeMsg(conn, postData, charSet);
            //4. get response
            rp.setContent(readMsg(conn, charSet));
            rp.setResponseCode(conn.getResponseCode());
            rp.setResponseMessage(conn.getResponseMessage());
            
        } catch (Exception ex) {
            ex.printStackTrace();
            if(conn != null){
                try {
                    rp.setResponseCode(conn.getResponseCode());
                    rp.setResponseMessage(conn.getResponseMessage());
                } catch (IOException e) {
                    rp = null;
                }
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return rp;
    }

    private String evaluateResponseCodeService(boolean isHttps, String url, String postData, String method, RequestProperty... rps){
        HttpURLConnection conn = null;
        try {
            boolean doOutput = !StringUtil.isEmpty(postData);
            conn = isHttps ? createHttpsConn(url, method, doOutput) : createHttpConn(url, method, doOutput);

            fillProperties(conn, rps);

            if(doOutput) writeMsg(conn, postData);

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return String.valueOf(conn.getResponseCode());
            }
            return readMsg(conn);
        } catch (Exception ex) {
            //ingore
            //just print out
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return null;
    }

    private HttpURLConnection createHttpConn(String url, String method, boolean doOutput) throws IOException {
        URL dataUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) dataUrl.openConnection();
        conn.setReadTimeout(read_time_out);
        conn.setRequestMethod(method);
        conn.setDoOutput(doOutput);
        conn.setDoInput(true);
        return conn;
    }

    private String readMsg(HttpURLConnection conn) throws IOException {
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
            StringBuilder sb = new StringBuilder();
            
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            return sb.toString();
        } finally {
            if(reader != null){
                reader.close();
            }
        }
    }

    private String readMsg(HttpURLConnection conn, String charSet) throws IOException {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            inputStream = conn.getInputStream();
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024]; // 1024 is enough
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, len);
            }

            if ("UTF8".equalsIgnoreCase(charSet) || "UTF-8".equalsIgnoreCase(charSet)
                    || "GBK".equalsIgnoreCase(charSet) || "GB2312".equalsIgnoreCase(charSet)) {
                return byteArrayOutputStream.toString(charSet);
            }
            String result = byteArrayOutputStream.toString();
            return result;
        } finally {
            if(inputStream != null) {
                inputStream.close();
            }
            if(byteArrayOutputStream != null){
                byteArrayOutputStream.close();
            }
        }
    }

    private void writeMsg(HttpURLConnection conn, String postData) throws IOException {
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        dos.write(postData.getBytes());
        dos.flush();
        dos.close();
    }

    private void writeMsg(HttpURLConnection conn, String postData, String charSet) throws IOException {
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        if ("UTF8".equalsIgnoreCase(charSet) || "UTF-8".equalsIgnoreCase(charSet)
                || "GBK".equalsIgnoreCase(charSet) || "GB2312".equalsIgnoreCase(charSet)) {
            dos.write(postData.getBytes(charSet));
        }else{
            dos.write(postData.getBytes());
        }
        dos.flush();
        dos.close();
    }

    private void fillProperties(HttpURLConnection conn, RequestProperty... rps) {
        if(rps == null){
            return;
        }

        for (RequestProperty rp : rps) {
            if(rp == null){
                continue;
            }
            conn.addRequestProperty(rp.getKey(), rp.getValue());
        }
    }
    
    
//    public String httpsPost(String url, String postData) {
//        HttpURLConnection conn = null;
//        try {
//            boolean doOutput = !StringUtil.isEmpty(postData);
//            conn = createHttpsConn(url, "POST", doOutput);
//
//            if (doOutput)
//                writeMsg(conn, postData);
//
//            return readMsg(conn);
//        } catch (Exception ex) {
//            // ingore
//            // just print out
//            ex.printStackTrace();
//        } finally {
//            if (conn != null) {
//                conn.disconnect();
//                conn = null;
//            }
//        }
//        return null;
//    }
    

    private HttpURLConnection createHttpsConn(String url, String method, boolean doOutput) throws Exception {
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };

        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        trustAllHttpsCertificates();

        URL dataUrl = new URL(url);
        
        HttpURLConnection conn = (HttpURLConnection) dataUrl.openConnection();
        conn.setReadTimeout(read_time_out);
        conn.setRequestMethod(method);
        conn.setDoOutput(doOutput);
        conn.setDoInput(true);
        return conn;
    }
    
    private static void trustAllHttpsCertificates() throws Exception {

        //  Create a trust manager that does not validate certificate chains:

        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        
        javax.net.ssl.TrustManager tm = new miTM();

        trustAllCerts[0] = tm;

        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");

        sc.init(null, trustAllCerts, null);

        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(
                sc.getSocketFactory());

    }
    
    public static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) throws
                java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) throws
                java.security.cert.CertificateException {
            return;
        }
    }

}
