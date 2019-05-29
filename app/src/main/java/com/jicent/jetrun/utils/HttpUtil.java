package com.jicent.jetrun.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.rmc.LogS;
import com.rmc.Util;

public class HttpUtil {
	
	private final static String TAG = "MyTag";
	private static String checkUrl="http://wap.jidown.com/stats/prod/ttapk/top10/checkname.jsp" ;
	public static String infoUrl="http://wap.jidown.com/stats/prod/ttapk/top10/notify.jsp" ;
	
	public static String nameCheck(Context context, String name) {
		JSONArray array = getDataFromServer(checkUrl, context, name,
				null,null );
		if (array != null) {
			try {
				if (array.getJSONObject(0).getString("ifmul").equals("0")) {
					return "use";
				}else if (array.getJSONObject(0).getString("ifmul").equals("-1")) {
					return "sensitive";
				}else {
					return "repeat";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return "网络连接出现错误，请重新获取数据!";
		}
		return null;
	}

	private static String httpPost(String httpUrl, JSONObject send) {
		String ret = null;
		String sendStr = null;
		try {
			URL url = new URL(httpUrl);

			if (send != null) {
				sendStr = send.toString();
			} else {
				sendStr = "{}";
			}

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10 * 1000);
			conn.setReadTimeout(10 * 1000);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestProperty("Content-type", "application/json");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			// conn.connect();
			OutputStream out = conn.getOutputStream();
			out.write(sendStr.getBytes());
			out.flush();
			out.close();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return null;
			}

			InputStream in = conn.getInputStream();
			byte[] array = new byte[4096];
			int readed = 0;
			ByteArrayOutputStream retOut = new ByteArrayOutputStream();
			while ((readed = in.read(array)) != -1) {
				retOut.write(array, 0, readed);
			}
			// BufferedReader reader=new BufferedReader(new
			// InputStreamReader(in));
			// String str="";
			// String line="";
			// while ((line=reader.readLine())!=null) {
			// str+=line;
			// }
			in.close();
			ret = retOut.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}

	public static JSONArray getDataFromServer(final String url,
			final Context c, final String name, final String score,final String type) {
		JSONArray ret = null;
		try {
			JSONObject root = new JSONObject();
			root.put("imei", Util.getImei(c));
			root.put("name", name);
			root.put("score", score);
			root.put("ptype", type);
			root.put("pid", 1000);
			String str = httpPost(url, root);
			Log.e("jaze"+HttpUtil.class.getSimpleName(), str);
			ret = new JSONArray(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}

	public static String httpGet(String httpUrl) {

		// Socket socket;
		String ret = null;

		try {
			URL url = new URL(httpUrl);
			LogS.d(TAG, "httpGet URL :" + url);
			int timeout = 30 * 1000;

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);
			conn.setInstanceFollowRedirects(true);
			// conn.setRequestProperty("Platform", "Android");
			// conn.setRequestProperty("Content-type",
			// "application/json");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.connect();

			int code = conn.getResponseCode();
			LogS.d(TAG, "code = " + code);
			if (code != 200) {
				return ret;
			}

			InputStream in = conn.getInputStream();
			byte[] array = new byte[4096];
			int readed = 0;
			ByteArrayOutputStream retOut = new ByteArrayOutputStream();
			while ((readed = in.read(array)) > 0) {
				retOut.write(array, 0, readed);
			}
			in.close();
			LogS.d(TAG, "<<<<<<" + retOut.toString());
			ret = retOut.toString();
		} catch (Exception e) {
			e.printStackTrace();
			LogS.d(TAG, "htppGet error = " + e.getMessage());
		}
		;

		return ret;
	}

	public static String httpDownload(String httpUrl, String path) {

		// Socket socket;
		String ret = null;

		try {
			URL url = new URL(httpUrl);
			LogS.d(TAG, "httpDownload url:" + url);
			LogS.d(TAG, "httpDownload path :" + path);
			int timeout = 30 * 1000;

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);
			conn.setInstanceFollowRedirects(true);
			// conn.setRequestProperty("Platform", "Android");
			// conn.setRequestProperty("Content-type",
			// "application/json");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.connect();

			int code = conn.getResponseCode();
			LogS.d(TAG, "code = " + code);
			if (code != 200) {
				return ret;
			}

			FileOutputStream fos = new FileOutputStream(path, false);

			InputStream in = conn.getInputStream();
			byte[] array = new byte[4096];
			int readed = 0;
			while ((readed = in.read(array)) > 0) {
				fos.write(array, 0, readed);
			}
			in.close();

			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			LogS.d(TAG, "htppGet error = " + e.getMessage());
		}
		;

		return ret;
	}
}
