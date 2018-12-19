package com.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;

import com.rsa.CertificateUtils;

import sun.misc.BASE64Encoder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.util.UUID;
/**
 * 云签章产品测试，客户端签名，服务端合成签章
 * @author ddw
 *
 */
public class ClientSignTest {

	public static String ImgPath = "./财务章.gif";
	public static String CerPath = "./111.cer";
//	public static String FilePath = "./1.pdf";
	public static String FilePath = "./5.ofd";
//	public static String FilePath = "./1533627515749.ofd";
//	public static String ServerHost = "47.92.151.182:8003";
	public static String ServerHost = "47.92.38.124:8003";
	//JZ
//	public static String ServerHost = "59.227.149.133";
	//JZ
//	public static String CertData = "MIIC5TCCAoigAwIBAgIQTFIY6LjjSXvIT/sg6z7h3DAMBggqgRzPVQGDdQUAMGYxCzAJBgNVBAYTAkNOMQ4wDAYDVQQIDAVIZU5hbjESMBAGA1UEBwwJWmhlbmdaaG91MSQwIgYDVQQKDBtIZU5hbiBDZXJ0aWZpY2F0ZSBBdXRob3JpdHkxDTALBgNVBAMMBEhOQ0EwHhcNMTcwOTE5MDE1MzEyWhcNMTgwOTE5MDE1MzEyWjBtMQswCQYDVQQGEwJDTjESMBAGA1UECAwJ6ZmV6KW/55yBMS0wKwYDVQQKDCTpmZXopb/kuK3ms73mlZnogrLnp5HmioDmnInpmZDlhazlj7gxGzAZBgNVBAMMEjkxNjEwMTMzTUE2VTVOVFhYWDBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABCHxV8xMrZ7A6mdS9ned4QCoYF0yVDtQantdeGv/pbCjQ/0JDAaSEAi8pqNZiOWjxptoDvHQA/x1UQBq7nvvCOSjggENMIIBCTAfBgNVHSMEGDAWgBSF6SJCjmIZ2wSWAxIx3BGQ3w7GtTCBuQYDVR0fBIGxMIGuMDWgM6AxpC8wLTELMAkGA1UEBhMCQ04xDDAKBgNVBAsMA0NSTDEQMA4GA1UEAwwHY3JsMzA2NjB1oHOgcYZvbGRhcDovLzIxOC4yOC4xNi4xMDE6Mzg3L0NOPWNybDMwNjYsT1U9Q1JMLEM9Q04/Y2VydGlmaWNhdGVSZXZvY2F0aW9uTGlzdD9iYXNlP29iamVjdGNsYXNzPWNSTERpc3RyaWJ1dGlvblBvaW50MAsGA1UdDwQEAwIGwDAdBgNVHQ4EFgQUxzfb3I6dhreHa6rmsT1oOMqRoPAwDAYIKoEcz1UBg3UFAANJADBGAiEAvdeZGaaINJ3RNfpYwnLoFaI4RgEE+lc6f7SyzIXnLZICIQDvSqaEA+4gE08xpMMAwztCSYXnzuSKbUhh2E4cI1ypHw==";
	//
	public static String CertData = "MIIDGzCCAsCgAwIBAgIQeJe4cIFFJps3PQGuepTvLzAMBggqgRzPVQGDdQUAMGYxCzAJBgNVBAYTAkNOMQ4wDAYDVQQIDAVIZU5hbjESMBAGA1UEBwwJWmhlbmdaaG91MSQwIgYDVQQKDBtIZU5hbiBDZXJ0aWZpY2F0ZSBBdXRob3JpdHkxDTALBgNVBAMMBEhOQ0EwHhcNMTgwODAzMDU0OTA1WhcNMjMwODAzMDU0OTA1WjCBgzELMAkGA1UEBhMCQ04xEjAQBgNVBAgMCeays+WNl+ecgTEPMA0GA1UEBwwG6YOR5beeMRgwFgYDVQQKDA/pg5HlpKfkuozpmYTpmaIxGzAZBgNVBAsMEuivgeS5pueuoeeQhuW5s+WPsDEYMBYGA1UEAwwP6LaF57qn566h55CG5ZGYMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEOWGjbD0wreVITmXjYV4CaDfbAw2vk+s2E14ENbC2fFIOc4Tm/78kxLBPTp4f/zHJfOgO/q/JYJxD/o/SFBbvSKOCAS4wggEqMIG5BgNVHR8EgbEwga4wNaAzoDGkLzAtMQswCQYDVQQGEwJDTjEMMAoGA1UECwwDQ1JMMRAwDgYDVQQDDAdjcmw1MTg1MHWgc6Bxhm9sZGFwOi8vMjE4LjI4LjE2LjEwMTozODcvQ049Y3JsNTE4NSxPVT1DUkwsQz1DTj9jZXJ0aWZpY2F0ZVJldm9jYXRpb25MaXN0P2Jhc2U/b2JqZWN0Y2xhc3M9Y1JMRGlzdHJpYnV0aW9uUG9pbnQwHQYDVR0OBBYEFOA0mxuAvD9bL+qAbKuvFdmdJHh7MB8GA1UdIwQYMBaAFIXpIkKOYhnbBJYDEjHcEZDfDsa1MAsGA1UdDwQEAwIGwDAPBgUqVgsHCwQGEwQwMDAwMA4GBSpWCwcPBAUMA+aXoDAMBggqgRzPVQGDdQUAA0cAMEQCIHoBddVcdadjPfbNe6SkQSdOgkJ0ZgpK8CKh0TC1cK0MAiBa+Iyzk52QeNiTuY0P4HLlII0tmWtUIgmcBYxgM3nBlw==";
//	public static String ImgData = "iVB.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";
//	public static String ImgData = "";
	public static String ImgData = "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";
	
	//JZ
//	public static String SignData = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA3tVE6a195Bh+cWByhVB8c7A/1R7b3Bw5KlPcvBb/EtQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAlJaN0Bra/t1vob7GpkBI8V5gr0wXgERH28n8yTaqQuE=";
	//
	public static String SignData = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAClp2K7dhz7EO81sM/28SZwDLMvgAD5HnNmyP5FmSf0RAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAByX3kAJxeRKd5dEvvaSBAvF3VaZGlqIEnlKgSEXWXUw=";
	//public static String ServerHost = "localhost:44580";
	
	public static void main(String[] args) throws Exception {
//		{"SigndataHash":"{"digestMessages":[{"signUniqueId":"39e2f476bba53f533b7940caa420f167",
//		"fileUniqueId":"2e4fe65e-2175-4","hashData":"flIHbmrKfD4+NxkHcGMLH+w1owbJUdBriEsO/nr+btI=",
//		"clientSignData":"MEYCIQBjQHCtBB83s2hzRqfbkrHy1Mti+H7JFL+MalpdnrFLoQIhAPgrqSjUjYNuXiT4ZNPj7JXqtoACCexzyuNRgfgANeF5"}]}","
//				+ ""CertID":"3B663686651786E8A766D5573ED29AA3","strpin":"111111"}
		JSONObject jsonObj = new JSONObject();
		//http://59.227.149.133/AppInfo
		//
		jsonObj.put("appId", "0950609e-2116-11e8-b2ca-00163e0027a9");
		//JZ
//		jsonObj.put("appId", "8988be92-85a9-11e8-9d0c-005056974753");
//		jsonObj.put("sealImg", FileEncodeBase64(ImgPath));
//		jsonObj.put("sealImg", CertificateUtils.GetCertAndPicBase64(CertificateUtils.GetCertID()).get(1));
		jsonObj.put("sealImg", ImgData);
		jsonObj.put("sealWidth", "120");
		jsonObj.put("sealHeight", "120");
		//signCert:客户端使用的签名UKey证书base64,需要在云签章平台授权后，方能使用。
		jsonObj.put("signCert", CertData);
		//jsonObj.put("signCert", CertificateUtils.GetCertAndPicBase64(CertificateUtils.GetCertID()).get(0));
		//jsonObj.put("signCert", FileEncodeBase64(CerPath));
		JSONArray clientSignMessages = new JSONArray();
		JSONObject clientSignMessage = new JSONObject();
		
		//流水号
		UUID businessNum = UUID.randomUUID();
		String busnum = businessNum.toString();
		System.out.println("busnum：" + busnum);
		//OFD
		clientSignMessage.put("fileUniqueId", busnum);
		//PDF
//		clientSignMessage.put("fileUniqueId", "1F3E51D3-827B-42A4-A113-20360255A41A");
//		clientSignMessage.put("keyword", "电子签章");//PDF
		clientSignMessage.put("keyword", "Hold");//OFD
		clientSignMessage.put("ruleType", "个人");
		//clientSignMessage.put("ruleType", "时间");
		clientSignMessage.put("heightMoveSize", "-10");//上正下负
		clientSignMessage.put("moveSize", "10");//右正左负
		clientSignMessages.add(clientSignMessage);
		jsonObj.put("clientSignMessages", clientSignMessages);
		JSONObject documentInfo = new JSONObject();
		documentInfo.put("docuName", "2e4fe65e-2175-4");
		documentInfo.put("fileDesc", "描述");
		String ssssss = FileEncodeBase64(FilePath).toString();
//		System.out.println(ssssss);
		documentInfo.put("docBase64", FileEncodeBase64(FilePath));
		jsonObj.put("documentInfo", documentInfo);
		
		String json=jsonObj.toString();		
		String urlpath = "http://" + ServerHost +"/api/ClientSignApi/ApplyHash";
		System.out.println("开始发送客户端签名第一个数据包，等待服务器返回文件哈希");
		long startTime = System.currentTimeMillis();	
		
		//json = readToString("./applyhash.json");
		
		String res = HttpUtils.PostJson(urlpath, json);
		System.out.print("res:"+ res + "\n");
		long endTime = System.currentTimeMillis();
		float seconds = (endTime - startTime) / 1000F;
		System.out.println("发送客户端签名第一个数据包耗时:" + Float.toString(seconds));
		boolean isReturnError = true;
		try{
			int a = Integer.parseInt(res);
		}
		catch(Exception e){
			isReturnError = false;
			
		}
		if (isReturnError == false) {
			jsonObj = JSONObject.fromObject(res);
			JSONArray digestMessages = jsonObj.getJSONArray("digestMessages");
			JSONObject secondJson = digestMessages.getJSONObject(0);
			String hashData = secondJson.getString("hashData");
			System.out.print("hashData:" + hashData + "\n");
			
			secondJson.put("clientSignData", SignData);
			jsonObj.put("digestMessages", digestMessages);
			
			String jsonStr = jsonObj.toString();
			System.out.print("jsonStr:" + jsonStr + "\n");
			
			urlpath = "http://" + ServerHost + "/api/ClientSignApi/MergeStamp";
			
			System.out.println("开始发送客户端签名第二个数据包，等待服务器返回签章后的文件");
			startTime = System.currentTimeMillis();
			
			res = HttpUtils.PostJson(urlpath, jsonStr);
			endTime = System.currentTimeMillis();
			seconds = (endTime - startTime) / 1000F;
			System.out.println("发送客户端签名第二个数据包耗时:" + Float.toString(seconds));
			
//			System.out.println(res);
			
//			JSONArray secondJsonArray = new JSONArray();
//			for(int i=0;i<digestMessages.size();i++){
//				JSONObject eachDigest = digestMessages.getJSONObject(i);
//				byte[] hash = Base64.decodeBase64(eachDigest.get("hashData").toString()) ;
//				eachDigest.element("clientSignData", Base64.encodeBase64String(CertificateUtils.RSASignHash(hash)));
//				secondJsonArray.add(eachDigest);
//			}
//			String certID = CertificateUtils.GetCertID();
//			System.out.print("res1:"+res);
//			JSONObject secondJson = CertificateUtils.SignData(certID, "111111", res);
//			secondJson.put("clientSignData", SignData);
//			System.out.print("secondJson:"+secondJson.toString());
//			//secondJson.put("digestMessages", secondJsonArray);
//			urlpath = "http://" + ServerHost + "/api/ClientSignApi/MergeStamp";
//			
//			System.out.println("开始发送客户端签名第二个数据包，等待服务器返回签章后的文件");
//			startTime = System.currentTimeMillis();
//			json = secondJson.toString();
//			
//			
//			//json = readToString("./mergestamp.json");
//			
//			res = HttpUtils.PostJson(urlpath, json);
//			endTime = System.currentTimeMillis();
//			seconds = (endTime - startTime) / 1000F;
//			System.out.println("发送客户端签名第二个数据包耗时:" + Float.toString(seconds));
		}
		isReturnError = true;
		try{
			int a = Integer.parseInt(res);
		}
		catch(Exception e){
			isReturnError = false;
//			System.out.println("文档Base64:" + res.toString());
		}
		if (isReturnError == false) {
			FileOutputStream fos = new FileOutputStream("./ClientSigned.ofd");
//			FileOutputStream fos = new FileOutputStream("./ClientSigned-1533.ofd");
//			FileOutputStream fos = new FileOutputStream("./ClientSigned.pdf");
			fos.write(Base64.decodeBase64(res));
			fos.close();
			System.out.println("Signed File has been writen Successfully!");
		}
		System.out.println("Program End!!!!!!!!!!!!!!!!!!!!!!!!");
		return;
	}
	
	public static String readToString(String fileName) {  
        String encoding = "UTF-8";  
        File file = new File(fileName);  
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
            return new String(filecontent, encoding);  
        } catch (Exception e) {  
            System.err.println("The OS does not support " + encoding);  
            e.printStackTrace();  
            return null;  
        }  
    } 
	
	public static String FileEncodeBase64(String filepath) {
		File file = new File(filepath);
		try {
			InputStream fr = new FileInputStream(file);
			byte[] b = new byte[(int) file.length()];

			fr.read(b);
			fr.close();

			String s = new BASE64Encoder().encode(b);
			return s;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

}
