package com.Test;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

/**
 * 证书业务申请测试类
 * @author ddw
 *
 */
public class CertApplyTest {
	public static String ApplyUrl="http://map3service.hnca.com.cn/HNCACertService";
	/**
	 * 新制证书
	 */
	public static String NewCert(){
//		{"ApplyId":"3d8601da73b904615ef034cdfc9bd2ad","DN":"CN=7,O=延期测试,C=CN",
//		"StartDate":"20171016000000000","EndDate":"20181016000000000",
//		"ItemId":"58005","CertTypeId":"1","Expands":[],"BusinessNum":"1000001195811811","Type":"1001","CertSN":""}
		
		 JSONObject json =new JSONObject();
		 json.put("ApplyId", "3d8601da73b904615ef034cdfc9bd2ad");
		 json.put("DN", "CN=16,OU=test,C=CN");
		 json.put("StartDate","20171016000000000");
		 json.put("EndDate","20181016000000000");
		 json.put("ItemId","58005");
		 json.put("CertTypeId","1");
		 json.put("Expands",new JSONArray());
		 json.put("BusinessNum","1000009295811834");
		 json.put("Type","1001");
		 json.put("CertSN","");
		 
		 System.out.println(json.toString());
		 String res = HttpUtils.PostJson(ApplyUrl, json.toString());
		 return res;
	}
	
	/**
	 * 延期证书
	 * @return
	 */
	public static String DeferCert(){
		 JSONObject json =new JSONObject();
		 json.put("ApplyId", "3d8601da73b904615ef034cdfc9bd2ad");
		 json.put("DN", "CN=16,OU=test,C=CN");
		 json.put("StartDate","20171016000000000");
		 json.put("EndDate","20181116000000000");
		 json.put("ItemId","58005");
		 json.put("CertTypeId","1");
		 json.put("Expands",new JSONArray());
		 json.put("BusinessNum","1000001195811835");
		 json.put("Type","1002");
		 json.put("CertSN","19C74FDA4DE016D8");
		 
		 System.out.println(json.toString());
		 String res = HttpUtils.PostJson(ApplyUrl, json.toString());
		 return res;
	}
	
	/**
	 * 变更证书
	 * @return
	 */
	public static String UpdateCert(){
		 JSONObject json =new JSONObject();
		 json.put("ApplyId", "3d8601da73b904615ef034cdfc9bd2ad");
		 json.put("DN", "CN=10,O=test1,C=CN");
		 json.put("StartDate","20171016000000000");
		 json.put("EndDate","20181116000000000");
		 json.put("ItemId","58005");
		 json.put("CertTypeId","1");
		 json.put("Expands",new JSONArray());
		 json.put("BusinessNum","1000001195811821");
		 json.put("Type","1003");
		 json.put("CertSN","263FD02C0FFFCEBE");
		 
		 System.out.println(json.toString());
		 String res = HttpUtils.PostJson(ApplyUrl, json.toString());
		 return res;
	}
	
	/**
	 * 注销证书
	 * @return
	 */
	public static String CancelCert(){
		 JSONObject json =new JSONObject();
		 json.put("ApplyId", "3d8601da73b904615ef034cdfc9bd2ad");
		 json.put("DN", "CN=14,OU=test,O=test,L=郑州,ST=河南,C=CN");
		 json.put("StartDate","20171016000000000");
		 json.put("EndDate","20181116000000000");
		 json.put("ItemId","58005");
		 json.put("CertTypeId","1");
		 json.put("Expands",new JSONArray());
		 json.put("BusinessNum","1000001195811830");
		 json.put("Type","1004");
		 json.put("CertSN","4F023B8C50DA924A");
		 
		 System.out.println(json.toString());
		 String res = HttpUtils.PostJson(ApplyUrl, json.toString());
		 return res;
	}
	

	public static void main(String[] args) {
		String res = "";
//		
//		res=NewCert();
//		System.out.println(res);
//		{"Result":"0","Message":"请求成功","RefCode":"54384D62CAFF472B","AuthCode":"B7LF7UB4E95TDRZS","BusinessNum":"1000001195811811"}

		res= DeferCert();
		System.out.println(res);
		
//		res= UpdateCert();
//		System.out.println(res);
		
//		res= CancelCert();
//		System.out.println(res);
		
		
	}

}
