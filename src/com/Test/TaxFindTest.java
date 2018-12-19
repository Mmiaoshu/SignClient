package com.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TaxFindTest {
	
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	private static String sendGet(String urlpath, String param) {
		String result = "";
		StringBuffer buffer = new StringBuffer();
		try {

			String urlNameString = urlpath + "?" + param;
			URL url = new URL(urlNameString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line = "";
			while ((line = br.readLine()) != null) {
				buffer.append(line);
			}
			br.close();
			conn.disconnect();

			result = buffer.toString();
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}

		return result;
	}

	private static void post(String urlPath, String json) {
		try {
			System.out.println(json);
			// 创建url资源
			URL url = new URL(urlPath);
			// 建立http连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置允许输出
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 设置不用缓存
			conn.setUseCaches(false);
			// 设置传递方式
			conn.setRequestMethod("POST");
			// 设置维持长连接
			conn.setRequestProperty("Connection", "Keep-Alive");
			// 设置文件字符集:
			conn.setRequestProperty("Charset", "UTF-8");
			// // 转换为字节数组
			byte[] data = json.getBytes();
			// 设置文件长度
			conn.setRequestProperty("Content-Length", String.valueOf(data.length));
			// 设置文件类型:
			conn.setRequestProperty("contentType", "application/json");

			// 开始连接请求
			conn.connect();
			OutputStream out = conn.getOutputStream();
			// 写入请求的字符串
			out.write((json).getBytes());
			out.flush();
			out.close();

			System.out.println("ResponseCode:" + conn.getResponseCode());

			// 请求返回的状态
			if (conn.getResponseCode() == 200) {
				System.out.println("连接成功");
				// 请求返回的数据
				InputStream in = conn.getInputStream();
				String a = null;
				try {
					byte[] data1 = new byte[in.available()];
					in.read(data1);
					// 转成字符串
					a = new String(data1);
					System.out.println(a);
//					return a;
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
//					return "";
				}
			} else {
				System.out.println("连接出现错误");
//				return "";
			}

		} catch (Exception e) {

		}
	}
	

	public static void main(String[] args) {
			String url = "http://124.47.3.237:8080/CAService/cert/check";
			String param = "ApplyId=ejx92ncq5ooitu8xquw6&tax=91610133092786775P";
			String res = sendGet(url,param);
			System.out.println("返回的json数据："+res);
	}

}
