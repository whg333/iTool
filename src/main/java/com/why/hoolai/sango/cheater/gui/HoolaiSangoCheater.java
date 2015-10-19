package com.why.hoolai.sango.cheater.gui;

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
public class HoolaiSangoCheater {
	
	private static final String URL = "http://sango.qzone.qzoneapp.com/messagebroker/amf";
	private static final String AUTH_KEY = "authSecret";
	private static final String IDENTIFYING_CODE_KEY = "idf_c_key";
	
	private static final String QZ_PLATFORM = "qzone";
	private static final int APP_ID = 613;

	public static void main(String[] args) throws Exception {
		WebClient webClient = initWebClient();
		long g_tk = loginQzoneAndGetGTK(webClient, "174708164", "15811001945");
		PlatformInfo platformInfo = requestPlatformInfo(webClient, APP_ID, g_tk);
		LoginInfo loginInfo = requestSangoLoginInfo(webClient, platformInfo);
		doCheatLogic(platformInfo, loginInfo);
	}
	
	private static WebClient initWebClient(){
		//这个浏览器模式必须在IE8的模式下，方可正确解析页面的JS，其他的浏览器模式有些方法属性不兼容
		WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_8);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		return webClient;
	}
	
	private static long loginQzoneAndGetGTK(WebClient webClient, String qq, String pwd) throws Exception{
		String loginQzoneUrl = "http://xui.ptlogin2.qq.com/cgi-bin/xlogin?"
			+ "proxy_url=http%3A//qzs.qq.com/qzone/v6/portal/proxy.html&daid=5&pt_qzone_sig=1&hide_title_bar=1&"
			+ "low_login=0&qlogin_auto_login=1&no_verifyimg=1&link_target=blank&appid=549000912&style=22&target=self&"
			+ "s_url=http%3A//qzs.qq.com/qzone/v5/loginsucc.html?para=izone&pt_qr_app=%E6%89%8B%E6%9C%BAQQ%E7%A9%BA%E9%97%B4&"
			+ "pt_qr_link=http%3A//z.qzone.com/download.html&self_regurl=http%3A//qzs.qq.com/qzone/v6/reg/index.html&"
			+ "pt_qr_help_link=http%3A//z.qzone.com/download.html";
		
		HtmlPage page = webClient.getPage(loginQzoneUrl);
		HtmlTextInput InputUserName = (HtmlTextInput) page.getElementById("u");
		HtmlPasswordInput InputPassword = (HtmlPasswordInput) page.getElementById("p");
		InputUserName.setText(qq);
		InputPassword.setText(pwd);
		//InputUserName.setText("guojianjiong@hoolai.com");
		//InputPassword.setText("jiong_hoolai_098");
		HtmlSubmitInput hb = (HtmlSubmitInput) page.getElementById("login_button");
		page = hb.click();
		//System.out.println(page.asXml());	// 登录已经成功，可以打印页面为xml文件来判断是否登录进去
		
		//System.out.println(page.executeJavaScript("QZONE"));
		long g_tk = new BigDecimal(page.executeJavaScript("QZONE.FP.getACSRFToken()").getJavaScriptResult().toString()).longValue();
		//System.out.println(page.executeJavaScript("console.info(QZFL.pluginsDefine.getACSRFToken('dfgdfg'))"));
		return g_tk;
	}
	
	public static PlatformInfo requestPlatformInfo(WebClient webClient, int appId, long g_tk) throws Exception{
		String platformInfoUrl = "http://appframe.qq.com/cgi-bin/qzapps/appframe.cgi?appid="+appId+"&qz_ver=6&pf=qzone&via=QZ.MYAPP&v=1&g_tk="+g_tk;
		System.out.println(platformInfoUrl);
		
		HtmlPage page = webClient.getPage(platformInfoUrl);
		String platformInfo = page.getWebResponse().getContentAsString();
		int startIndex = platformInfo.lastIndexOf("{");
		int endIndex = platformInfo.indexOf("}");
		platformInfo = "{"+platformInfo.substring(startIndex+1, endIndex).trim()+"}";
		//System.out.println(platformInfo);
		HashMap<String, String> platformInfoMap = JSONUtils.fromJSON(platformInfo, HashMap.class);
		String openid = platformInfoMap.get("openid");
		String openkey = platformInfoMap.get("openkey");
		String pfkey = platformInfoMap.get("pfkey");
		return new PlatformInfo(openid, openkey, pfkey);
	}
	
	private static LoginInfo requestSangoLoginInfo(WebClient webClient, PlatformInfo platformInfo) throws Exception{
		String sangoUrl = "http://sango.qzone.qzoneapp.com/index.jsp?qz_height=900&qz_width=760" +
			"&openid="+platformInfo.openid+"&openkey="+platformInfo.openkey+
			"&pf=qzone&pfkey="+platformInfo.pfkey+"&qz_ver=8&appcanvas=1&qz_style=25&params=";
		System.out.println(sangoUrl);
		
		//因为在sangoUrl页面有JS执行报错，其实只需要获取token和userId即可根本不需要执行JS
		//还可以设置浏览器模式为IE9~IE11或者火狐系列，方可正确解析sangoUrl页面上的JS
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage page = webClient.getPage(sangoUrl);
		String token = page.getHtmlElementById("token").getAttribute("value");
		String userIdStr = page.getHtmlElementById("userId").getAttribute("value");
		System.out.println("token="+token+",userIdStr="+userIdStr);
		return new LoginInfo(token, userIdStr);
	}
	
	private static final class PlatformInfo{
		private String openid;
		private String openkey;
		private String pfkey;
		private String pf = QZ_PLATFORM;
		private String supportPlatformPf = QZ_PLATFORM;
		public PlatformInfo(String openid, String openkey, String pfkey){
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
	
	private static final class LoginInfo{
		private final String token;
		private final String userIdStr;
		public LoginInfo(String token, String userIdStr) {
			this.token = token;
			this.userIdStr = userIdStr;
		}
		public String getToken() {
			return token;
		}
		public String getUserIdStr() {
			return userIdStr;
		}
	}
	
	private static void doCheatLogic(PlatformInfo platformInfo, LoginInfo loginInfo) throws Exception {
		String requestInfoStr = JSONUtils.toJSONwithOutNullProp(platformInfo);
		System.out.println(requestInfoStr);
		HoolaiAMFConnection connection = skipValidateConnection(loginInfo.token);
		Object result = null;
		try {
			result = connection.call("UserService.getUserInfo", "0", platformInfo.openid, requestInfoStr, loginInfo.token, false);
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
	
	private static final HoolaiAMFConnection skipValidateConnection(String token) throws ClientStatusException {
		HoolaiAMFConnection connection = new HoolaiAMFConnection();
		connection.connect(URL);
		connection.addAmfHeader(AUTH_KEY, true, token);
		connection.addAmfHeader(IDENTIFYING_CODE_KEY, true, "");
		return connection;
	}

}