package com.Client;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.codehaus.xfire.client.Client;

/**
 * 封装后的签章服务器调用类
 * @author 党大伟 2017-6-3
 *
 */
public class SignClient {
	// 签章服务器地址
	private static String url = "http://10.0.0.65/qzfw/service/QzfwFtpSignService?wsdl";

	private static Object[] callWs(String name, Object[] params) {
		Object[] rt = new Object[] {};
		try {
			Client c = new Client(new URL(url));
			rt = c.invoke(name, params);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return rt;
	}
	
	/**
	 * 个人用户申请
	 * @param YHID	用户 ID，由外部应用系统生成
	 * @param SJH	个人手机号
	 * @param XM	个人姓名
	 * @param SFZH	身份证号
	 * @return
	 */
	public static String RegisteredUser(String YHID,String SJH,String XM,String SFZH){
		String sign = "";
		Object[] param = new Object[] { YHID, SJH, XM, SFZH, sign };
		Object[] ur = callWs("RegisteredUser", param);
		System.out.println(String.valueOf(ur.length));
		System.out.println(ur[0].toString());

		if (ur.length > 0)
			return ur[0].toString();
		else
			return "";
	}

	/**
	 * 企业用户申请证书接口
	 * 
	 * @param YHID
	 *            用户 ID，由外部应用系统生成
	 * @param WTRSJH
	 *            委托人手机号
	 * @param WTRXM
	 *            委托人姓名
	 * @param WTRSFZH
	 *            委托人身份证号
	 * @param QYMC
	 *            企业名称
	 * @return 成功:0 失败:1,失败信息
	 */
	public static String RegisteredWTUser(String YHID, String WTRSJH, String WTRXM, String WTRSFZH, String QYMC) {
		String sign = "";
		Object[] param = new Object[] { YHID, WTRSJH, WTRXM, WTRSFZH, QYMC, sign };
		Object[] ur = callWs("RegisteredWTUser", param);
		System.out.println(String.valueOf(ur.length));
		System.out.println(ur[0].toString());

		if (ur.length > 0)
			return ur[0].toString();
		else
			return "";

	}

	/**
	 * 申请印章接口
	 * 
	 * @param YHID
	 *            用户 ID，由外部应用系统生成
	 * @param TPBASE64
	 *            印章图片base64编码，由外部应用系统生成
	 * @param YZMC
	 *            印章名称
	 * @return 成功:0,印章 ID 失败:1,失败信息
	 */
	public static String SealApplication(String YHID, String TPBASE64, String YZMC) {
		String sign = "";
		TPBASE64=URLEncoder.encode(TPBASE64);	//进行url编码防止特殊字符才传输中被过滤掉
		Object[] param = new Object[] { YHID, TPBASE64, YZMC, sign };
		Object[] ur = callWs("SealApplication", param);
		System.out.println("申请印章接口调用");
		System.out.println(String.valueOf(ur.length));
		System.out.println(ur[0].toString());

		if (ur.length > 0)
		{
			String[] str = ur[0].toString().split(",");
			String YZID=str[1];	//返回的印章id
			return YZID;
		}
		else
			return "";
	}

	/**
	 * PDF 签章接口
	 * @param YHID		用户 ID，由外部应用系统生成
	 * @param TYPE		类型，0 使用默认印章  1 企业，2 普通。
	 * @param YZID		印章 ID
	 * @param PDFurl		PDF 文件名称(ftp 中的文件名字，文件名字不能重复)
	 * @param ix			印章左下角 x坐标，由外部应用系统指定
	 * @param iy				印章左下角 y坐标，由外部应用系统指定
	 * @param ax			印章右上角 x坐标，由外部应用系统指定
	 * @param ay			印章右上角 y坐标，由外部应用系统指定
	 * @param p				盖章页码
	 * @param reason		盖章说明
	 * @param loc			地理位置
	 * @param con			盖章详情说明
	 * @param time		签名方式(0 第一次签名、1 从已签署中再次签署)，默认为0
	 * @return	成功:0,已签名的PDF文件下载路径   失败:1,失败信息
	 */
	public static String SealInterface(String YHID, String TYPE, String YZID, String PDFurl, float ix, float iy,
			float ax, float ay, int p, String reason, String loc, String con, String time) {
		if(YHID=="")
			return "";
		
		if(YZID=="")
			return "";
		
		String sign = "";
		Object[] param = new Object[] { YHID, TYPE, YZID, PDFurl, ix, iy, ax, ay, p, reason, loc, con, time, sign };
		Object[] ur = callWs("SealInterface", param);
		System.out.println("盖章接口调用");
		System.out.println(String.valueOf(ur.length));
		System.out.println(ur[0].toString());

		if (ur.length > 0)
		{
			String[] str = ur[0].toString().split(",");
			String downUrl=str[1];	//返回的印章id
			return downUrl;
		}
		else
			return "";
	}
}
