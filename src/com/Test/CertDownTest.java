package com.Test;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

/**
 * map3 证书下载测试
 * @author ddw
 *
 */
public class CertDownTest {
	public static String DownUrl="http://map3service.hnca.com.cn/HNCADownloadCert";
	public static void main(String[] args) {
		 
//		{"Result":"0","Message":"0","RefCode":"6CCAD848A72C027E","AuthCode":"9CWCZU5HRMFFTUSG","BusinessNum":"1000001195811835"}
		
//		String P10="MIHTMHwCAQEwGjEYMAkGA1UEBgwCY24wCwYDVQQDDAR0ZXN0MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgEEIvfiKk2OjvGL6YRwBEstGYNLrX8xqyuHBl3tHYn9iZRN4Oz54X94QLn5sDRCia1zmVBWPvAIiIUdF3wNz6qsFqAAMAoGCCqBHM9VAYN1A0cBMEQCIIuaFAkoTOAFXIxtXcNxmK4rPj0o/S+GRt6qQ29p9eWXAiAl8t+cSYuJALdciyGlD1vv0N6zxt0pxWRnAenSBI8tUg==";
//		String DoubleP10="MIHTMHwCAQEwGjEYMAkGA1UEBgwCY24wCwYDVQQDDAR0ZXN0MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgEEIvfiKk2OjvGL6YRwBEstGYNLrX8xqyuHBl3tHYn9iZRN4Oz54X94QLn5sDRCia1zmVBWPvAIiIUdF3wNz6qsFqAAMAoGCCqBHM9VAYN1A0cBMEQCIPpyaKoy/P7thskwUBTWuqM6VS0AEU0qbUGggf59rSZ3AiAv6D5rgCJMmG75Gg0UosCTIcGEM3nDruBj2hn5+E8DLg==";
		
		String P10="MIHTMHwCAQEwGjEYMAkGA1UEBgwCY24wCwYDVQQDDAR0ZXN0MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgIEqeLYtxObJkTdHytghSCPRmEFvO8TPN/T99HNgZa3Zt2zGDgSXdhmLuH8kNH1MOvPDPZ+xWMDfAhsM7W3jhA8LKAAMAoGCCqBHM9VAYN1A0cBMEQCIL3Ehy4e9W0piw9H3Auuf0xJm23tmq/RYH9xcqkuCOIHAiDmUaxl+0fPGEfFF3KihsQ8bzZrVShkq42H+EUdB92Log==";
		String DoubleP10="MIHTMHwCAQEwGjEYMAkGA1UEBgwCY24wCwYDVQQDDAR0ZXN0MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgIEqeLYtxObJkTdHytghSCPRmEFvO8TPN/T99HNgZa3Zt2zGDgSXdhmLuH8kNH1MOvPDPZ+xWMDfAhsM7W3jhA8LKAAMAoGCCqBHM9VAYN1A0cAMEQCIBAyzCYKykTiec4l9rYkP41U+YFpDnh/88VG/YAPIij1AiA2wStvop3xr9Bk+HhRi8blRvz3OdzMyjBBUdBnABxVyQ==";
			
//		String P10="MIHTMHwCAQEwGjEYMAkGA1UEBgwCY24wCwYDVQQDDAR0ZXN0MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAErPJCPAYPE6QipgOk/raryjiGkQrtDVufMVjNn1MpqebnF9qRRmgYLavINN9GsIA643L6i06oOut54tHZb9U8TaAAMAoGCCqBHM9VAYN1A0cBMEQCIOeavcYaoRzbyGo+pC9etblm2hnwmDxFHUq65Pefmy9KAiAOFex6YBXCqZqt2gFogvvjqgKMnj236xXqcdEYOvQT0g==";
//		String DoubleP10="MIHTMHwCAQEwGjEYMAkGA1UEBgwCY24wCwYDVQQDDAR0ZXN0MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAErPJCPAYPE6QipgOk/raryjiGkQrtDVufMVjNn1MpqebnF9qRRmgYLavINN9GsIA643L6i06oOut54tHZb9U8TaAAMAoGCCqBHM9VAYN1A0cBMEQCIOVmwMtC+K4GSKbjnhKoxd11oUfzuCQ8n9ql+Qe9pjMGAiAsJPEsx9B4OrWMTcZdMVEjAzQjh0fJpDZPj2H9rau9qg==";
		
		JSONObject json =new JSONObject();
		 json.put("ApplyId","3d8601da73b904615ef034cdfc9bd2ad");	
		 json.put("RefCode","6CCAD848A72C027E");
		 json.put("AuthCode","9CWCZU5HRMFFTUSG");
		 json.put("BusinessNum","1000001195811835");
		 json.put("P10",P10);
		 json.put("DoubleP10",DoubleP10);
		 
		 System.out.println(json.toString());
		 String res = HttpUtils.PostJson(DownUrl, json.toString());
		 System.out.println(res);
		 
	}

}
