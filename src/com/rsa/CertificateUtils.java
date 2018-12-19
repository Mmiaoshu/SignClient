package com.rsa;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Certificate;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.java_websocket.WebSocket.READYSTATE;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import sun.security.mscapi.*;

public class CertificateUtils {
	//pfx证书路径
	private final static String CertPath="./111.pfx";
	
	//证书密码
	private final static String password="1";  
	
	//证书别名
	private final static String alias="c=cn,st=河南省,l=郑州市,o=信睿报告平台,ou=ctfz0kyu,cn=信睿报告平台";
	
	private static String  message = "";
	
	/**
	 * RSA数字签名
	 * @param srcdata  需要签名的数据原文
	 * @return
	 */
	public static String RSASignData(String srcdata){
		InputStream in;
		try {
			in = new FileInputStream(new File(CertPath));
			  ByteArrayOutputStream out = new ByteArrayOutputStream();
		        KeyStore keyStore = KeyStore.getInstance("PKCS12");
		        keyStore.load(in, password.toCharArray());
		        keyStore.store(out, password.toCharArray());
		        String b64 = Base64.encodeBase64String(out.toByteArray());
		        String alieString = keyStore.aliases().nextElement();
		        X509Certificate x509cert = (X509Certificate)keyStore.getCertificate(alieString);
		        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alieString, password.toCharArray());
//		        System.out.println(privateKey);
//		        System.out.println(x509cert.getSigAlgName());
		        
		        //创建签名
		        Signature sig = Signature.getInstance(x509cert.getSigAlgName());
		     // 用指定的私钥进行初始化
				sig.initSign(privateKey);
				// 添加要签名的信息
				sig.update(srcdata.getBytes());
				// 返回签名的字节数组
				byte[] binaryData = sig.sign();
		        String signBase64 = Base64.encodeBase64String(binaryData);
		        return signBase64;
		        
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "";
		}
	}
	
	/**
	 * RSA数字签名
	 * @param srcdata  需要签名的数据原文
	 * @return
	 */
	public static byte[] RSASignData(byte[] srcdata){
		InputStream in;
		try {
			in = new FileInputStream(new File(CertPath));
			  ByteArrayOutputStream out = new ByteArrayOutputStream();
		        KeyStore keyStore = KeyStore.getInstance("PKCS12");
		        keyStore.load(in, password.toCharArray());
		        keyStore.store(out, password.toCharArray());
		        String b64 = Base64.encodeBase64String(out.toByteArray());
		        String alieString = keyStore.aliases().nextElement();
		        X509Certificate x509cert = (X509Certificate)keyStore.getCertificate(alieString);
		        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alieString, password.toCharArray());
//		        System.out.println(privateKey);
//		        System.out.println(x509cert.getSigAlgName());
		        
		        //创建签名
		        Signature sig = Signature.getInstance(x509cert.getSigAlgName());
		     // 用指定的私钥进行初始化
				sig.initSign(privateKey);
				// 添加要签名的信息
				sig.update(srcdata);
				// 返回签名的字节数组
				byte[] binaryData = sig.sign();
		        return binaryData;
		        
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
	}
	
	public static byte[] RSASignHash(byte[] btHash)  
            throws Exception {  
		InputStream in;
		try {
			in = new FileInputStream(new File(CertPath));
			  ByteArrayOutputStream out = new ByteArrayOutputStream();
		        KeyStore keyStore = KeyStore.getInstance("PKCS12");
		        keyStore.load(in, password.toCharArray());
		        keyStore.store(out, password.toCharArray());
		        String b64 = Base64.encodeBase64String(out.toByteArray());
		        String alieString = keyStore.aliases().nextElement();
		        X509Certificate x509cert = (X509Certificate)keyStore.getCertificate(alieString);
		        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alieString, password.toCharArray());
		        //根据RFC3447 RSA Cryptography Specifications Version 2.1 中9.2节对签名的填充规范，在做NONEwithRSA签名前需要对数据进行填充
		        //（因为收到的已经是做过散列的hashData，所以不能直接调用Signature signature = Signature.getInstance("Sha1withRSA")，必须手动进行填充，然后使用NONEwithRSA模式）
		        ///MD2:     (0x)30 20 30 0c 06 08 2a 86 48 86 f7 0d 02 02 05 00 04 10 || H.
		        //MD5:     (0x)30 20 30 0c 06 08 2a 86 48 86 f7 0d 02 05 05 00 04 10 || H.
		        //SHA-1:   (0x)30 21 30 09 06 05 2b 0e 03 02 1a 05 00 04 14 || H.
		        //SHA-256: (0x)30 31 30 0d 06 09 60 86 48 01 65 03 04 02 01 05 00 04 20 || H.
		        //SHA-384: (0x)30 41 30 0d 06 09 60 86 48 01 65 03 04 02 02 05 00 04 30 || H.
		        //SHA-512: (0x)30 51 30 0d 06 09 60 86 48 01 65 03 04 02 03 05 00 04 40 || H.
		        byte[] asn_sha1=ArrayUtils.addAll(new byte[] { (byte)0x30, (byte)0x21, (byte)0x30, (byte)0x09, (byte)0x06, (byte)0x05, 
		        		(byte)0x2b, (byte)0x0e, (byte)0x03, (byte)0x02, (byte)0x1a, (byte)0x05, (byte)0x00, (byte)0x04, (byte)0x14}, btHash);
		        byte[] asn_sha256=ArrayUtils.addAll(new byte[] { (byte)0x30, (byte)0x31, (byte)0x30, (byte)0x0d, (byte)0x06, (byte)0x09, (byte)0x60, 
		        		(byte)0x86, (byte)0x48, (byte)0x01, (byte)0x65, (byte)0x03, (byte)0x04, (byte)0x02, (byte)0x01, (byte)0x05, (byte)0x00, (byte)0x04, (byte)0x20}, btHash);
		        Signature signature = Signature.getInstance("NONEwithRSA");
		        signature.initSign(privateKey);
		        signature.update(asn_sha1);
		                     
		        byte[] ret = signature.sign();
		        return ret;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
    }  
	
	public static String GetCertID() throws Exception 	{
		WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://127.0.0.1:2012/CertInfo")) {
			@Override
			public void onOpen(ServerHandshake arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMessage(String arg0) {
				// TODO Auto-generated method stub
				message = arg0;
			}
			
			@Override
			public void onError(Exception arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClose(int arg0, String arg1, boolean arg2) {
				// TODO Auto-generated method stub
				
			}
		};
		message = "";
		webSocketClient.connect();
		while(!webSocketClient.getReadyState().equals(READYSTATE.OPEN)){
			Thread.sleep(100);			
	    }
		webSocketClient.send("123");
		while (message == null || message.isEmpty()) {
			Thread.sleep(100);			
		}
		webSocketClient.close();
		JSONObject jsonObj = JSONObject.fromObject(message);
		JSONArray certinfoList = jsonObj.getJSONArray("CertInfolist");
		if (certinfoList.size() != 1) {
			System.out.println("未插入UKEY或插有多个UKEY！请增加代码进行枚举操作！");
			return null;
		}
		else {
			JSONObject certInfo = certinfoList.getJSONObject(0);
			System.out.println("UKEY序列号为" + certInfo.get("CertID").toString());
			return certInfo.get("CertID").toString();
		}
	}
	
	public static ArrayList<String> GetCertAndPicBase64(String certID) throws Exception 	{
		WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://127.0.0.1:2012/SealAndCertBase64")) {
			@Override
			public void onOpen(ServerHandshake arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMessage(String arg0) {
				// TODO Auto-generated method stub
				message = arg0;
			}
			
			@Override
			public void onError(Exception arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClose(int arg0, String arg1, boolean arg2) {
				// TODO Auto-generated method stub
				
			}
		};
		message = "";
		webSocketClient.connect();
		while(!webSocketClient.getReadyState().equals(READYSTATE.OPEN)){
			Thread.sleep(100);			
	    }
		webSocketClient.send(certID);
		while (message == null || message.isEmpty()) {
			Thread.sleep(100);			
		}
		webSocketClient.close();
		JSONObject jsonObj = JSONObject.fromObject(message);
		System.out.println(message);
		if (jsonObj.containsKey("error") && !jsonObj.get("error").toString().isEmpty()) {
			System.out.println("获取UKEY信息错误！");
			return null;
		}
		else {
			ArrayList<String> certAndPic = new ArrayList<String>();
			certAndPic.add(jsonObj.get("CertBase64").toString());
			certAndPic.add(jsonObj.get("CertSeal").toString());
			return certAndPic;
		}
	}
	
	public static JSONObject SignData(String certID, String strpin, String SigndataHash) throws Exception 	{
		WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://127.0.0.1:2012/SignData")) {
			@Override
			public void onOpen(ServerHandshake arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMessage(String arg0) {
				// TODO Auto-generated method stub
				message = arg0;
			}
			
			@Override
			public void onError(Exception arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClose(int arg0, String arg1, boolean arg2) {
				// TODO Auto-generated method stub
				
			}
		};
		message = "";
		JSONObject sendObject = new JSONObject();
		sendObject.put("SigndataHash", "\"" + SigndataHash + "\"");
		sendObject.put("CertID", certID);
		sendObject.put("strpin", strpin);
		webSocketClient.connect();
		while(!webSocketClient.getReadyState().equals(READYSTATE.OPEN)){
			Thread.sleep(100);			
	    }
		webSocketClient.send(sendObject.toString());
		while (message == null || message.isEmpty()) {
			Thread.sleep(100);			
		}
		webSocketClient.close();
		JSONObject jsonObj = JSONObject.fromObject(message);
		System.out.println(message);
		if (jsonObj.containsKey("error") && !jsonObj.get("error").toString().isEmpty()) {
			System.out.println("获取UKEY信息错误！");
			return null;
		}
		else {
			JSONObject signMsgs = jsonObj.getJSONObject("signMsgs");
			return signMsgs;
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		String certID = GetCertID();
		ArrayList<String> certAndPic = GetCertAndPicBase64(certID);
		System.out.println("证书base64:"+ certAndPic.get(0));
		System.out.println("印章base64:"+ certAndPic.get(1));
		
		String signData = RSASignData("123456");
		System.out.println(signData);
//		String path = "/Users/ddw/Documents/检测行业/开发文档/云签章/C=CN,ST=河南省,L=郑州市,O=信睿报告平台,OU=CTFZ0KYU,CN=信睿报告平台.pfx";
//        String password="11111111";
//        String srcdata = "123456";
//		InputStream in;
//		try {
//			in = new FileInputStream(new File(path));
//			  ByteArrayOutputStream out = new ByteArrayOutputStream();
//		        KeyStore keyStore = KeyStore.getInstance("PKCS12");
//		        keyStore.load(in, password.toCharArray());
//		        keyStore.store(out, password.toCharArray());
//		        String b64 = Base64.encodeBase64String(out.toByteArray());
//		        System.out.println(b64);
//		        Enumeration<String> aliases = keyStore.aliases();
//		        String enumeration = null;
//		        while (true) {
//		            try {
//		                enumeration = aliases.nextElement();
//		                System.out.println(enumeration);
//		            } catch (NoSuchElementException e) {
//		                System.out.println("读取完毕");
//		                break;
//		            }
//		        }
//		        
//		        java.security.cert.Certificate cert =  keyStore.getCertificate(enumeration);
//		        X509Certificate x509cert = (X509Certificate)keyStore.getCertificate(enumeration);
//		        System.out.println(cert.toString());
//		        PrivateKey privateKey = (PrivateKey) keyStore.getKey(enumeration, password.toCharArray());
//		        System.out.println(privateKey);
//		        System.out.println(x509cert.getSigAlgName());
//		        
//		        //创建签名
//		        Signature sig = Signature.getInstance(x509cert.getSigAlgName());
//		     // 用指定的私钥进行初始化
//				sig.initSign(privateKey);
//				// 添加要签名的信息
//				sig.update(srcdata.getBytes());
//				// 返回签名的字节数组
//				byte[] binaryData = sig.sign();
//		        String signBase64 = Base64.encodeBase64String(binaryData);
//		        System.out.println(signBase64);
//		        
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
      
		
	}

}
