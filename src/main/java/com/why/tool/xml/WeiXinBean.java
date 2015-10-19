package com.why.tool.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

public class WeiXinBean {
	
	//公共字段
	private String ToUserName;
	private String FromUserName;
	private String CreateTime;
	private String MsgType;
	private String MsgId;
	
	//文本消息-text字段
	private String Content;
	
	//图片消息-image字段
	private String PicUrl;
	private String MediaId;
	
	//地理位置消息-location字段
	private String Location_X;
	private String Location_Y;
	private String Scale;
	private String Label;
	
	//链接消息-link字段
	private String Title;
	private String Description;
	private String Url;
	
	//事件推送-event字段
	private String Event;
	private String EventKey;
	
	//回复图文消息字段
	private String ArticleCount;
	private List<Item> Articles = new ArrayList<Item>(MAX_ARTICLES_COUNT);
	
	private static final int MAX_ARTICLES_COUNT = 10;
	
	private static final XStream xstream;
	static{
		xstream = new XStream();
		xstream.alias("xml", WeiXinBean.class);
		xstream.alias("item", Item.class);
	}
	
	public static WeiXinBean fromXML(String xml){
		return (WeiXinBean)xstream.fromXML(xml);
	}
	
	public static String toXML(WeiXinBean weiXinBean){
		return xstream.toXML(weiXinBean);
	}
	
	public void addArticle(Item item){
		Articles.add(item);
	}

	public int getArticlesCount() {
		return Articles.size();
	}
	
	public void refreshCount(){
		ArticleCount = Articles.size()+"";
	}

	public List<Item> getArticles() {
		return Articles;
	}

	public void setArticles(List<Item> articles) {
		Articles = articles;
	}
	
	public static void main(String[] args) {
		WeiXinBean weiXinBean = WeiXinBean.fromXML(testXml());
		System.out.println(weiXinBean);
		
		weiXinBean = new WeiXinBean();
		//weiXinBean.setMsgId("2");
		//weiXinBean.setContent("123");
		weiXinBean.setToUserName("1");
		weiXinBean.setFromUserName("2");
		weiXinBean.setCreateTime("123456");
		weiXinBean.setMsgType("news");
		
		Item item = new Item();
		item.setDescription("description");
		item.setPicUrl("picUrl");
		item.setTitle("title");
		item.setUrl("url");
		weiXinBean.addArticle(item);
		
		item = new Item();
		item.setDescription("description2");
		item.setPicUrl("picUr2l");
		item.setTitle("title2");
		item.setUrl("url2");
		weiXinBean.addArticle(item);
		
		weiXinBean.refreshCount();
		
		String xml = WeiXinBean.toXML(weiXinBean);  
		System.out.println(xml);
	}
	
	public static String testXml(){
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<ToUserName><![CDATA[toUser]]></ToUserName>");
		sb.append("<FromUserName><![CDATA[fromUser]]></FromUserName>");
		sb.append("<CreateTime>1348831860</CreateTime>");
		sb.append("<MsgType><![CDATA[text]]></MsgType>");
		sb.append("<Content><![CDATA[this is a test]]></Content>");
		sb.append("<MsgId>1234567890123456</MsgId>");
		sb.append("</xml>");
		return sb.toString();
	}
	
	public static String newsXml(WeiXinBean weiXinBean){
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<ToUserName><![CDATA[toUser]]></ToUserName>");
		sb.append("<FromUserName><![CDATA[fromUser]]></FromUserName>");
		sb.append("<CreateTime>1348831860</CreateTime>");
		sb.append("<MsgType><![CDATA[news]]></MsgType>");
		sb.append("<ArticleCount>1</ArticleCount>");
		sb.append("<Articles>");
		sb.append("<item>");
		sb.append("<Title><![CDATA[title1]]></Title>");
		sb.append("<Description><![CDATA[description1]]></Description>");
		sb.append("<PicUrl><![CDATA[picurl]]></PicUrl>");
		sb.append("<Url><![CDATA[url]]></Url>");
		sb.append("</item>");
		sb.append("<Articles>");
		sb.append("</xml>");
		return sb.toString();
	}
	
	public WeiXinBean(){
		
	}
	
	public String getToUserName() {
		return ToUserName;
	}
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	public String getMsgId() {
		return MsgId;
	}
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getPicUrl() {
		return PicUrl;
	}
	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
	public String getMediaId() {
		return MediaId;
	}
	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
	public String getLocation_X() {
		return Location_X;
	}
	public void setLocation_X(String locationX) {
		Location_X = locationX;
	}
	public String getLocation_Y() {
		return Location_Y;
	}
	public void setLocation_Y(String locationY) {
		Location_Y = locationY;
	}
	public String getScale() {
		return Scale;
	}
	public void setScale(String scale) {
		Scale = scale;
	}
	public String getLabel() {
		return Label;
	}
	public void setLabel(String label) {
		Label = label;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public String getEvent() {
		return Event;
	}
	public void setEvent(String event) {
		Event = event;
	}
	public String getEventKey() {
		return EventKey;
	}
	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}
	public String getArticleCount() {
		return ArticleCount;
	}
	public void setArticleCount(String articleCount) {
		ArticleCount = articleCount;
	}

	@Override
	public String toString() {
		return "WeiXinBean [ToUserName=" + ToUserName + ", FromUserName="
				+ FromUserName + ", CreateTime=" + CreateTime + ", MsgType="
				+ MsgType + ", MsgId=" + MsgId + ", Content=" + Content
				+ ", PicUrl=" + PicUrl + ", MediaId=" + MediaId
				+ ", Location_X=" + Location_X + ", Location_Y=" + Location_Y
				+ ", Scale=" + Scale + ", Label=" + Label + ", Title=" + Title
				+ ", Description=" + Description + ", Url=" + Url + ", Event="
				+ Event + ", EventKey=" + EventKey + ", ArticleCount="
				+ ArticleCount + ", Articles=" + Articles + "]";
	}
	
}
