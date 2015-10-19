package com.why.hoolai.sango.cheater;

import java.io.File;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.why.hoolai.sango.amf.HoolaiAMFConnection;
import com.why.tool.json.JSONUtils;

import flex.messaging.io.amf.client.exceptions.ClientStatusException;

@SuppressWarnings({"unchecked", "deprecation", "unused"})
public class QQLogin {

	public static void main(String[] args) throws Exception {
		//这个浏览器模式必须在IE8的模式下，方可正确解析页面的JS，其他的浏览器模式有些方法属性不兼容
		WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_8);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);

		HtmlPage page = webClient.getPage("http://xui.ptlogin2.qq.com/cgi-bin/xlogin?proxy_url=http%3A//qzs.qq.com/qzone/v6/portal/proxy.html&daid=5&pt_qzone_sig=1&hide_title_bar=1&low_login=0&qlogin_auto_login=1&no_verifyimg=1&link_target=blank&appid=549000912&style=22&target=self&s_url=http%3A//qzs.qq.com/qzone/v5/loginsucc.html?para=izone&pt_qr_app=%E6%89%8B%E6%9C%BAQQ%E7%A9%BA%E9%97%B4&pt_qr_link=http%3A//z.qzone.com/download.html&self_regurl=http%3A//qzs.qq.com/qzone/v6/reg/index.html&pt_qr_help_link=http%3A//z.qzone.com/download.html");
		//HtmlPage page = webClient.getPage("http://i.qq.com/");
		//HtmlPage page = webClient.getPage("http://www.baidu.com/");

		
//		String js = IOUtils.toString(new FileReader(findJsFile("/c_login_2.js")));
//		page.executeJavaScript(js);
		
//		System.out.println(page.executeJavaScript("pt.plogin.hasSubmit"));
//		String loginUrl = page.executeJavaScript("pt.plogin.getSubmitUrl('login')").getJavaScriptResult().toString();
////		System.out.println(loginUrl);
//		System.out.println(page.executeJavaScript("$.http.loadScript('"+loginUrl+"')"));
//		System.out.println(page.executeJavaScript("pt.plogin.hasSubmit"));
//		
//		js = IOUtils.toString(new FileReader(findJsFile("/qzfl_v8_2.1.43.js")));
//		page.executeJavaScript(js);
//		
//		js = IOUtils.toString(new FileReader(findJsFile("/appcanvas_new_src.js")));
//		page.executeJavaScript(js);
		
		HtmlTextInput InputUserName = (HtmlTextInput) page.getElementById("u");
		HtmlPasswordInput InputPassword = (HtmlPasswordInput) page.getElementById("p");
		InputUserName.setText("174708164");
		InputPassword.setText("15811001945");
		HtmlSubmitInput hb = (HtmlSubmitInput) page.getElementById("login_button");
		page = hb.click();
//		System.out.println(page.asXml());	// 登录已经成功，可以打印页面为xml文件来判断是否登录进去
		
//		System.out.println(page.executeJavaScript("QZONE"));
		long g_tk = new BigDecimal(page.executeJavaScript("QZONE.FP.getACSRFToken()").getJavaScriptResult().toString()).longValue();
//		System.out.println(ACSRFToken);
//		System.out.println(page.executeJavaScript("console.info(QZFL.pluginsDefine.getACSRFToken('dfgdfg'))"));
		
//		testExecuteJsCode(page);
//		System.out.println("after executeJavaScript");
		
		String platformInfoUrl = "http://appframe.qq.com/cgi-bin/qzapps/appframe.cgi?appid=613&qz_ver=6&pf=qzone&via=QZ.MYAPP&v=1&g_tk="+g_tk;
		System.out.println(platformInfoUrl);
		page = webClient.getPage(platformInfoUrl);
		String platformInfo = page.getWebResponse().getContentAsString();
		int startIndex = platformInfo.lastIndexOf("{");
		int endIndex = platformInfo.indexOf("}");
		platformInfo = "{"+platformInfo.substring(startIndex+1, endIndex).trim()+"}";
		
		HashMap<String, String> platformInfoMap = JSONUtils.fromJSON(platformInfo, HashMap.class);
		String openid = platformInfoMap.get("openid");
		String openkey = platformInfoMap.get("openkey");
		String pfkey = platformInfoMap.get("pfkey");
		
		String sangoUrl = "http://sango.qzone.qzoneapp.com/index.jsp?qz_height=900&qz_width=760&" +
				"openid="+openid+"&openkey="+openkey+"&pf=qzone&pfkey="+pfkey+"&qz_ver=8&appcanvas=1&qz_style=25&params=";
		System.out.println(sangoUrl);
		
		//因为在sangoUrl页面有JS执行报错，其实只需要获取token和userId即可根本不需要执行JS
		//还可以设置浏览器模式为IE9~IE11或者火狐系列，方可正确解析sangoUrl页面上的JS
		webClient.getOptions().setJavaScriptEnabled(false);
		page = webClient.getPage(sangoUrl);
		String token = page.getHtmlElementById("token").getAttribute("value");
		String userId = page.getHtmlElementById("userId").getAttribute("value");
		System.out.println("token="+token+",userId="+userId);
		
		//这下面注释的代码是原来想模拟点击Qzone主页面的“胡莱三国”链接跳转到胡莱三国，然后获取token和userId
//		HtmlElement sangoA = null;
//		HtmlElement ele = page.getHtmlElementById("tab_applist_show");
//		Iterator<DomElement> ulIt = ele.getChildElements().iterator();
//		while(ulIt.hasNext()){
//			DomElement li = ulIt.next();
//			Iterator<DomElement> liIt = li.getChildElements().iterator();
//			while(liIt.hasNext()){
//				HtmlElement a = (HtmlElement)liIt.next();
//				if(a.getAttribute("data-id").equals("613")){
//					sangoA = a;
//					System.out.println("found 613 sango app...");
//				}
//			}
//		}
//		page = sangoA.click();
//		System.out.println(page.asText());
		
		String requestInfoStr = requestInfoStr(openid, openkey, pfkey);
		System.out.println(requestInfoStr);
		HoolaiAMFConnection connection = skipValidateConnection(token);
		Object result = null;
		try {
			result = connection.call("UserService.getUserInfo", "0", openid, requestInfoStr, token, false, "", "");
			//result = connection.call("OffLineUnionBattleService.dismissUnionCountry", userId+"", 3, "");
		} finally {
			connection.close();
		}
		if(result != null){
			System.out.println(result);
			Object officerList = ((Map<String, Object>)result).get("officerList");
			System.out.println(officerList);
			System.out.println(Array.get(officerList, 0));
		}
	}
	
	private static final String URL = "http://sango.qzone.qzoneapp.com/messagebroker/amf";
	private static final String AUTH_KEY = "authSecret";
	private static final String IDENTIFYING_CODE_KEY = "idf_c_key";
	
	private static final String QZ_PLATFORM = "qzone";
	
	private static String requestInfoStr(String openid, String openkey, String pfkey){
		return JSONUtils.toJSONwithOutNullProp(new RequestInfo(openid, openkey, pfkey));
	}
	
	private static final class RequestInfo{
		private String openid;
		private String openkey;
		private String pfkey;
		private String pf = QZ_PLATFORM;
		private String supportPlatformPf = QZ_PLATFORM;
		public RequestInfo(String openid, String openkey, String pfkey){
			this.openid = openid;
			this.openkey = openkey;
			this.pfkey = pfkey;
		}
		public String getOpenid() {
			return openid;
		}
		public void setOpenid(String openid) {
			this.openid = openid;
		}
		public String getOpenkey() {
			return openkey;
		}
		public void setOpenkey(String openkey) {
			this.openkey = openkey;
		}
		public String getPfkey() {
			return pfkey;
		}
		public void setPfkey(String pfkey) {
			this.pfkey = pfkey;
		}
		public String getPf() {
			return pf;
		}
		public void setPf(String pf) {
			this.pf = pf;
		}
		public String getSupportPlatformPf() {
			return supportPlatformPf;
		}
		public void setSupportPlatformPf(String supportPlatformPf) {
			this.supportPlatformPf = supportPlatformPf;
		}
	}
	
	private static final HoolaiAMFConnection skipValidateConnection(String token) throws ClientStatusException {
		HoolaiAMFConnection connection = new HoolaiAMFConnection();
		connection.connect(URL);
		connection.addAmfHeader(AUTH_KEY, true, token);
		connection.addAmfHeader(IDENTIFYING_CODE_KEY, true, "");
		return connection;
	} 
	
	private static File findJsFile(String path){
		String jsPath = ListEngineFactoryDemo.class.getResource(path).getPath();
		File file = new File(jsPath);
		if(!file.exists()){
			System.out.println("\n"+file.getAbsolutePath()+" not exists");
			throw new RuntimeException(file.getAbsolutePath()+" not exists");
		}
		return file;
	}
	
	private static void testExecuteJsCode(HtmlPage page){
		page.executeJavaScript(" var strname = 'Key' ");
		page.executeJavaScript("function sayHello(   ) { " 
                    + " print('Hello '+strname+'!');return 'my name is '+strname;" + "}");
		ScriptResult sr = page.executeJavaScript("sayHello()");
		System.out.println(sr.getJavaScriptResult());
	}

}