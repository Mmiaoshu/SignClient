package com.Test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
	
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
	
	
}
