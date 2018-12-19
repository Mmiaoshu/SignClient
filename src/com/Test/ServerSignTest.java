package com.Test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.rsa.CertificateUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import sun.misc.*;

/**
 * 云签章产品，服务端签章测试
 * 
 * @author ddw
 *
 */
public class ServerSignTest {

	// 文件base64数据
	public static String docBase64 = "";

	// 签名后的文档路径
//	public static String FilePath = "./1.pdf";
//	public static String FilePath = "./11411000747423482Y111000000060001.pdf";
//	public static String FilePath = "./1533627515749.ofd";
	public static String FilePath = "./5.ofd";
//	public static String FilePath = "./test.ofd";
	
	//public static String ServerHost = "localhost:44580";
//	public static String ServerHost = "47.92.151.182:8003";
	//XC测试地址
//	public static String ServerHost = "47.92.38.124:8003";
	//XC外网映射地址
//	public static String ServerHost = "222.143.158.4:8090";
	//JZ
//	public static String ServerHost = "59.227.149.133";
	//ZJT
//	public static String ServerHost = "10.1.2.14";
	//濮阳
	public static String ServerHost = "59.207.114.130";
	

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		long startTime = System.currentTimeMillis();
		float total = 0;
		
		docBase64 = FileEncodeBase64(FilePath);
		

		long endTime = System.currentTimeMillis();
		float seconds = (endTime - startTime) / 1000F;
		total = total + seconds;
		System.out.println("读取文件耗时：" + Float.toString(seconds) + "秒");
		float fileSize = docBase64.length() - (docBase64.length() / 8) * 2;
		fileSize = (fileSize / 1000) / 1000;
		System.out.println("文件数据大小：" + Float.toString(fileSize) + " MB");

		startTime = System.currentTimeMillis();
		System.out.println("开始构造云签章请求");
		String res = ServerSign(); // 构造云签章请求并等待服务器返回
		endTime = System.currentTimeMillis();
		seconds = (endTime - startTime) / 1000F;
		total = total + seconds;
		System.out.println("构造云签章请求并收到服务器返回共耗时：" + Float.toString(seconds) + "秒");
		
		// ErrorCode ，ErrorMsg ，SignDoc ，BusinessNum
		try {
			JSONObject jsonRes = JSONObject.fromObject(res);
			if (jsonRes.getString("ErrorCode").equals("0")) { // 获取错误码
				startTime = System.currentTimeMillis();
				System.out.println("签章成功");
				System.out.println("流水号：" + jsonRes.getString("BusinessNum"));
				String fileData = jsonRes.getString("SignDoc"); // 获取已签章文件的base64编码
				// 保存文件
				decoderBase64File(fileData, "./ServerSigned.ofd");
//				decoderBase64File(fileData, "./test-signed.ofd");
//				decoderBase64File(fileData, "./ServerSigned-1533.ofd");
//				decoderBase64File(fileData, "./ServerSigned.pdf");
				endTime = System.currentTimeMillis();
				seconds = (endTime - startTime) / 1000F;
				System.out.println("解码并保存文件耗时：" + Float.toString(seconds) + " ");
				total = total + seconds;

				startTime = System.currentTimeMillis();
				System.out.println("开始构造验证文档请求");
				res = VerifySign();
				endTime = System.currentTimeMillis();
				seconds = (endTime - startTime) / 1000F;
				System.out.println("验证文档返回值：" + res);
				System.out.println("构造验证文档请求并收到服务器返回共耗时：" + Float.toString(seconds));
				total = total + seconds;

			} else {
				System.out.println("出现错误");
				System.out.println(jsonRes.getString("ErrorCode"));
				System.out.println(jsonRes.getString("ErrorMsg")); // 打印错误信息
			}
		} catch (Exception ex) {
			System.out.println("错误数据：" + res);
		}

		System.out.println("全流程总耗时：" + Float.toString(total) + "秒");
		
		//PDF->OFD测试
		
		
	}

	public static String VerifySign() {
		String url = "http://"  + ServerHost +"/api/VerifyApi/VerifyStamp";
//		 docBase64= FileEncodeBase64("./ServerSigned.pdf");
//		 docBase64= FileEncodeBase64("./ServerSigned-1533.pdf");
		 docBase64= FileEncodeBase64("./ServerSigned.ofd");
//		 docBase64= FileEncodeBase64("./test-signed.ofd");
//		 docBase64= FileEncodeBase64("./55.ofd");
		 
//		docBase64 = FileEncodeBase64("/Users/ddw/Downloads/北京CA的签章文件1.pdf");
		String RequestId = String.valueOf(java.util.UUID.randomUUID());
		RequestId = RequestId.substring(0, 8);

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("RequestId", RequestId);
		jsonObj.put("DocData", docBase64);

		String json = jsonObj.toString();
		long startTime = System.currentTimeMillis();		
		String res = PostJson(url,json);
		long endTime = System.currentTimeMillis();
		float seconds = (endTime - startTime) / 1000F;
		System.out.println( "服务器验证耗时:"  +  Float.toString(seconds)   );

		return res;
	}

	/**
	 * 对服务端签章报文数据进行签名
	 * 
	 * @param businessNum
	 *            流水号
	 * @param rule
	 *            签章规则字符串
	 * @return
	 */
	public static String sign(String businessNum, String rule) {
		String srcdata = businessNum + rule;
		String signData = CertificateUtils.RSASignData(srcdata);
		return signData;
	}

	/**
	 * 支持多个盖章规则，带数据签名的签章接口 2017-11-16
	 * 
	 * @return
	 */
	public static String ServerSign() {
		String url = "http://" + ServerHost +"/api/ServerSignApi/ApplyStamp";
		JSONObject jsonObj = new JSONObject();
		String businessNum = String.valueOf(java.util.UUID.randomUUID());
		businessNum = businessNum.substring(0, 8);
		jsonObj.put("businessNum", businessNum); // 流水号，不固定20位以内数字或英文字符，重复的流水号会导致合并签章失败
		//外网测试
//		String appId="0950609e-2116-11e8-b2ca-00163e0027a9";
		//XC
//		String appId="66e85eb6-b248-11e8-95ce-286ed488ca2b";
		//JZ
//		String appId="8988be92-85a9-11e8-9d0c-005056974753";
		//ZJT
//		String appId="ccbb575e-cb98-11e8-a099-fa163e054c51";
		//濮阳
		String appId="1d828766-dc3b-11e8-a9fa-286ed488c6b4";
//		appId=appId.toUpperCase();
		jsonObj.put("appId", appId); // 固定值     
		jsonObj.put("policyType", "1"); // 固定值 
//		-----------------------------------------
	
//		-----------------------------------------
		//JZ服务端证书序列号（正式）
//		String sn = "3A246DB032B300AEC63A1A59F506C12E";
		//测试
//		String sn = "2EABE957DAE3E052B0B13A3F8D59D2C0";
		//XC
//		String sn = "4338699B1AAD027DB042D7FAEE72906B";
		//濮阳
		String sn = "56FC80D3EB618474BB037EDCE9932487";
		//测试
//		String sn = "56FC80D3EB618474BB037EDCE9932487";
//		String sn = "";
		sn = sn.toUpperCase();
		jsonObj.put("channelId", sn); // 固定值,服务端证书序列号
//		jsonObj.put("userCert", "58981ABDE4EB873AE065EC513BBA1F8C"); // policyType=1 时，这个值为调用者的证书序列号
//		jsonObj.put("userCert", "");//-1055用户权限不足
		//JZ测试
//		jsonObj.put("userCert", "7897B8708145269B373D01AE7A94EF2F"); // policyType=1 时，这个值为调用者的证书序列号
		//XC
//		jsonObj.put("userCert", "7897B8708145269B373D01AE7A94EF2F");
		//濮阳
		jsonObj.put("userCert", "00E061E6C7940F2787B68019F391DAD3");
		
		JSONObject documentInfo = new JSONObject();
//		documentInfo.put("docuName", "testdoc.pdf"); // 文档名称
		documentInfo.put("docuName", "testdoc.ofd"); // 文档名称
		documentInfo.put("fileDesc", ""); // 文档说明
		documentInfo.put("docBase64", docBase64); // 文档base64编码  docBase64
		jsonObj.put("documentInfo", documentInfo);

		JSONArray ruleList = new JSONArray(); // 签章规则，应用程序根据需要动态添加内容
		//JZ
//		ruleList.add("HNCA_10000006");//首页，单页签章
//		ruleList.add("HNCA_10000007");//关键字
		//外网测试
//		ruleList.add("HNCA_10000071");//测试单页1
//		ruleList.add("HNCA_10000073");//测试单页2
//		ruleList.add("HNCA_10000058");
//		ruleList.add("HNCA_10000066");
		//ZJT
//		ruleList.add("HNCA_10000001");
		//XC
//		ruleList.add("HNCA_10000001");
		//濮阳
		ruleList.add("HNCA_10000002");
		ruleList.add("HNCA_10000003");

		jsonObj.put("ruleList", ruleList);

//		String rule = "";
//		for (int i = 0; i < ruleList.size(); i++) {
//			rule = rule + ruleList.getString(i);
//		}
//
//		String signData = sign(businessNum, rule);
//		jsonObj.put("signData", signData);

		String strJson = jsonObj.toString();
		//System.out.println(strJson);
		long startTime = System.currentTimeMillis();		
		String res = PostJson(url,strJson);
		long endTime = System.currentTimeMillis();
		float seconds = (endTime - startTime) / 1000F;
		System.out.println( "服务器签章耗时:"  +  Float.toString(seconds)   );
		res = res.trim();
		return res;
	}
	
//	//PDF转OFD
//	public static String PDFToOFD() {
//		String url = "http://" + ServerHost +"/api/ConvertApi/PdfOfdConvert";
//		JSONObject jsonObj = new JSONObject();
//		System.out.println("PDF->OFD测试开始");
//		String RequestId = String.valueOf(java.util.UUID.randomUUID());
//		RequestId = RequestId.substring(0, 8);
//		jsonObj.put("RequestId", RequestId);
//		jsonObj.put("RequestId", RequestId); // 流水号，不固定20位以内数字或英文字符，重复的流水号会导致合并签章失败
//		//外网测试
////		String appId="0950609e-2116-11e8-b2ca-00163e0027a9";
//		//XC
//		String appId="66e85eb6-b248-11e8-95ce-286ed488ca2b";
//		//JZ
////		String appId="8988be92-85a9-11e8-9d0c-005056974753";
//		//ZJT
////		String appId="ccbb575e-cb98-11e8-a099-fa163e054c51";
////		appId=appId.toUpperCase();
//		jsonObj.put("appId", appId); // 固定值     
//
////		-----------------------------------------
//	
////		-----------------------------------------
//		//JZ服务端证书序列号（正式）
////		String sn = "3A246DB032B300AEC63A1A59F506C12E";
//		//测试
////		String sn = "2EABE957DAE3E052B0B13A3F8D59D2C0";
//		//XC
//		String sn = "4338699B1AAD027DB042D7FAEE72906B";
//		//测试
////		String sn = "56FC80D3EB618474BB037EDCE9932487";
////		String sn = "";
//		sn = sn.toUpperCase();
//		jsonObj.put("channelId", sn); // 固定值,服务端证书序列号
//		jsonObj.put("userCert", "58981ABDE4EB873AE065EC513BBA1F8C"); // policyType=1 时，这个值为调用者的证书序列号
////		jsonObj.put("userCert", "");//-1055用户权限不足
//		//JZ测试
////		jsonObj.put("userCert", "7897B8708145269B373D01AE7A94EF2F"); // policyType=1 时，这个值为调用者的证书序列号
//		//XC
//		jsonObj.put("userCert", "7897B8708145269B373D01AE7A94EF2F");
//		
//		JSONObject documentInfo = new JSONObject();
////		documentInfo.put("docuName", "testdoc.pdf"); // 文档名称
//		documentInfo.put("docuName", "testdoc.ofd"); // 文档名称
//		documentInfo.put("fileDesc", ""); // 文档说明
//		documentInfo.put("docBase64", docBase64); // 文档base64编码  docBase64
//		jsonObj.put("documentInfo", documentInfo);
//
//		JSONArray ruleList = new JSONArray(); // 签章规则，应用程序根据需要动态添加内容
//		//JZ
////		ruleList.add("HNCA_10000006");//首页，单页签章
////		ruleList.add("HNCA_10000007");//关键字
//		//外网测试
////		ruleList.add("HNCA_10000071");//测试单页1
////		ruleList.add("HNCA_10000073");//测试单页2
////		ruleList.add("HNCA_10000058");
////		ruleList.add("HNCA_10000066");
//		//ZJT
////		ruleList.add("HNCA_10000001");
//		//XC 
//		ruleList.add("HNCA_10000001");
//
//		jsonObj.put("ruleList", ruleList);
//
////		String rule = "";
////		for (int i = 0; i < ruleList.size(); i++) {
////			rule = rule + ruleList.getString(i);
////		}
////
////		String signData = sign(businessNum, rule);
////		jsonObj.put("signData", signData);
//
//		String strJson = jsonObj.toString();
//		//System.out.println(strJson);
//		long startTime = System.currentTimeMillis();		
//		String res = PostJson(url,strJson);
//		long endTime = System.currentTimeMillis();
//		float seconds = (endTime - startTime) / 1000F;
//		System.out.println( "服务器签章耗时:"  +  Float.toString(seconds)   );
//		res = res.trim();
//		return res;
//	}


	public static String PostJson(String urlpath, String json) {
		String strUrl = urlpath;

		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1 ");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// conn.setRequestProperty("Content-Type",
			// "application/json;charset=utf-8");
			conn.setRequestProperty("Accept-Charset", "utf-8");

			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.write(json.getBytes("utf-8"));
			out.flush();
			out.close();

			InputStream instream = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "utf-8"));

			String line = "";
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				line = new String(line.getBytes(), "utf-8");
				sb.append(line);
			}

			reader.close();
			conn.disconnect();

			return sb.toString();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return "";
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

	/**
	 * 将Base64字符解码保存文件
	 * 
	 * @param base64Code
	 * @param targetPath
	 * @throws IOException
	 */
	public static void decoderBase64File(String base64Code, String targetPath) throws IOException {
		byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
		FileOutputStream out = new FileOutputStream(targetPath);
		out.write(buffer);
		out.close();
	}

}
