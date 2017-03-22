package com.why.tool.operation;

import com.why.tool.string.StringUtil;
import com.why.tool.time.TimeUtil;

public class TokenProducer {
	
    private static final String produceTokenAttachStr = "             huaTeng@why.com          ";
    
    private static final String worldBattleProduceTokenAttachStr = "           worldbattle.sango@why.com          ";
    
    private static final String worldBattleProduceTokenAttachStrV2 = "           worldbattleV2.sango@why.com          ";
    
    private static final String weixinProduceTokenAttachStr = "            weixin.huateng@why.com            ";
    
    private static final String businessProduceTokenAttachStr = "      startbusiness.huateng@why.com         ";
    
    public static String produceToken(String userIdStr) {
        return StringUtil.encryptToMd5(userIdStr + produceTokenAttachStr);
    }
    
    public static String produceWorldBattleToken(String userIdStr) {
        if(userIdStr == null) {
            userIdStr = "";
        }
        return StringUtil.encryptToMd5(userIdStr + worldBattleProduceTokenAttachStr);
    }
    
    public static String produceWorldBattleTokenV2(String userIdStr) {
        if(userIdStr == null) {
            userIdStr = "";
        }
        return StringUtil.encryptToMd5(userIdStr + worldBattleProduceTokenAttachStrV2);
    }
    
    public static String produceGetUserInfoToken(String openid) {
        return StringUtil.encryptToMd5(openid + produceTokenAttachStr);
    }
    
    public static String produceLoginIdentifyingCodeToken(String userIdStr) {
        return StringUtil.encryptToMd5(userIdStr + produceTokenAttachStr + "hoolai&*^*huai~@12"+TimeUtil.currentTimeMillis());
    }
    
    public static String produceBusinessToken(String userIdStr) {
        return StringUtil.encryptToMd5(userIdStr + businessProduceTokenAttachStr + "hoolai&*^*huai~@12"+TimeUtil.currentTimeMillis());
    }
    
    public static String produceWeiXinToken(){
    	return StringUtil.encryptToMd5(weixinProduceTokenAttachStr + "hoolai&*^*huai~@12"+TimeUtil.currentTimeMillis());
    }
    
    public static void main(String[] args) {
    	for(int i=0;i<5;i++){
    		System.out.println(produceWeiXinToken());
    	}
	}
}
