package com.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class T {

	public static void main(String[] args) {
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
//		Calendar calendar = Calendar.getInstance();
//        Date date = new Date(System.currentTimeMillis());
//        System.out.println(simpleDateFormat.format(date));
//        System.out.println(date);
//        calendar.setTime(date);
////        calendar.add(Calendar.WEEK_OF_YEAR, -1);
//        calendar.add(Calendar.YEAR, 1);
//        date = calendar.getTime();
//       
//        
//        
//        String d = simpleDateFormat.format(date);
//        System.out.println(d);
		Map tm = new HashMap();
		
		tm.put("TYPE", "1");
		tm.put("BUSINESSNUM", "2017394390234");
		tm.put("YHID", "0001test20170714003");
		tm.put("SJH", "13592601000");
		tm.put("XM", "操作员2");
		tm.put("SFZH","410927198001011010");
		tm.put("QYMC", "测试公司");
	
		String param = JSONObject.fromObject(tm).toString();
		System.out.println(param);
	
//		String s = "7%2J%23%211%22%2HD2%A200ts111%22%2M2%A21112%C2SZ%23%21%22%2YC2%A211%22%2YE2%A212%C2BSNSNM2%A221349242%D";
		String s = "";
		String p = "";
		try {
			s= URLEncoder.encode(param, "utf-8");
			System.out.println(s);
			p = URLDecoder.decode(s,"utf-8");
//			logger.info("解码后的原始数据："+sb.toString());
			System.out.println(p);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			logger.error("解码后原始数据异常："+e.getMessage());
		}
	}

}
