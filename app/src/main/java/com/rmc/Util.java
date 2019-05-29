package com.rmc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

public class Util {

	private final static String TAG = "MyTag";

	public static void showToast(Context c, String msg) {
		Toast.makeText(c, msg, Toast.LENGTH_LONG).show();
	}

	public static void checkNewVersion(final Context c, final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				LogS.d(TAG, "checkNewVersion run() ");
				String pkg = Util.getPkgName(c);

				StringBuffer p = new StringBuffer();
				p.append("j=");
				p.append(c.getPackageName());
				p.append("&u=");
				p.append(getVersionCode(c));
				p.append("&fr=");
				p.append(pkg);
				p.append("&m=");
				String imei = Util.getImei(c);
				String base64 = null;
				if(!TextUtils.isEmpty(imei))
				{
					base64 = android.util.Base64.encodeToString(Util
							.getImei(c).toString().getBytes(), Base64.NO_WRAP);
				}
				p.append(base64);

				final String url = "http://wap.jidown.com/s/jxiyou.php?" + p;

				String ret = httpGet(url);
				if (ret != null && ret.length() > 0) {
					LogS.d(TAG, "ret = " + ret);
					// String[] list = ret.split("\\|");
					// if (list.length < 2) {
					// return;
					// }
					// final String apk_url = list[0];
					// final String apk_size = list[1];
					try {
						JSONObject root = new JSONObject(ret);
						downloadAndInstall(c, handler, root);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}).start();
	}

	public static int getVersionCode(Context c) {
		LogS.d(TAG, "getVersionCode");
		int ret = 0;
		try {
			PackageManager packageManager = c.getPackageManager();
			// 0代表是获取版本信息
			PackageInfo packInfo = null;
			packInfo = packageManager.getPackageInfo(c.getPackageName(), 0);
			ret = packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			LogS.d(TAG, "error=" + e.getMessage());
		}

		return ret;
	}

	private static String getJsonStr(JSONObject obj, String name) {
		String ret = null;
		try {
			ret = obj.getString(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ret;
	}

	private static void downloadAndInstall(final Context c,
			final Handler handler, final JSONObject root) {
		if (root == null) {
			return;
		}

		final String apkPath = "/data/data/" + c.getPackageName() + "/0.apk";
		// [{"url":"http://120.193.11.179/music/JicentClient.apk","size":"257501","ifenforce":"0","inquire":"发现新版本，您要升级吗?"}]
		handler.post(new Runnable() {
			public void run() {
				final int enforce = Integer.valueOf(getJsonStr(root,
						"ifenforce"));

				AlertDialog.Builder builder = new AlertDialog.Builder(c);
				if (enforce == -1) {
					builder.setTitle("提醒");
				} else {
					builder.setTitle("升级提示");
				}
				builder.setMessage(getJsonStr(root, "inquire"));
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

								if (enforce >= 0) // -1 表示展示信息，和升级无关
								{
									Util.startWebView(c,
											getJsonStr(root, "url"));
								}
								// new Thread(new Runnable() {
								//
								// @Override
								// public void run() {
								// HttpUtil.httpDownload(
								// getJsonStr(root, "url"),
								// apkPath);
								//
								// File file = new File(apkPath);
								// if (file.length() == Long
								// .valueOf(getJsonStr(root,
								// "size"))) {
								// installApk(c, handler, apkPath);
								// }
								// }
								// }).start();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								if (enforce == 1) {
									System.exit(0);
								}
							}
						});
				builder.show();
			}
		});
	}

	private static void installApk(final Context c, Handler handler,
			final String apkPath) {
		String command = "chmod 777 " + apkPath;
		Runtime runtime = Runtime.getRuntime();
		try {
			Process proc = runtime.exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		handler.post(new Runnable() {
			public void run() {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				File file = new File(apkPath);
				intent.setDataAndType(Uri.fromFile(new File(apkPath)),
						"application/vnd.android.package-archive");
				c.startActivity(intent);
			}
		});
	}

	public static String getPkgName(Context c) {
		String ret = null;

		byte[] buffer = new byte[128];
		int readed = 0;
		try {
			InputStream is = c.getAssets().open("channel");
			ByteArrayOutputStream retOut = new ByteArrayOutputStream();
			while ((readed = is.read(buffer)) > 0) {
				retOut.write(buffer, 0, readed);
			}
			is.close();
			ret = retOut.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static void startWebView(Context c, String url) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);
		c.startActivity(intent);
	}

	private static String mImei = null;

	public static String getImei(Context c) {

		if (mImei == null) {
			TelephonyManager tm = (TelephonyManager) c
					.getSystemService(Context.TELEPHONY_SERVICE);
			mImei = tm.getDeviceId();
		}
		return mImei;
	}

	private static String mImsi = null;

	public static String getImsi(Context c) {
		if (mImsi == null) {
			try {
				TelephonyManager mTelephonyMgr = (TelephonyManager) c
						.getSystemService(Context.TELEPHONY_SERVICE);
				mImsi = mTelephonyMgr.getSubscriberId();
			} catch (Exception e) {

			}
		}
		return mImsi;
	}

	public static final int CHINA_MOBILE = 1;
	public static final int CHINA_UNION = 2;
	public static final int CHINA_TELECOM = 3;
	public static final int CHINA_ERROROP = -1;
	private static int mOperatorType = CHINA_ERROROP;

	public static int getOperatorType(Context c) {
		String imsi = getImsi(c);
		if(imsi == null)
		{
			// skip
		}
		else if (imsi.startsWith("46000")) {
			mOperatorType = CHINA_MOBILE;
		} else if (imsi.startsWith("46001")) {
			mOperatorType = CHINA_UNION;
		} else if (imsi.startsWith("46002")) {
			mOperatorType = CHINA_MOBILE;
		} else if (imsi.startsWith("46003")) {
			mOperatorType = CHINA_TELECOM;
		} else if (imsi.startsWith("46005")) {
			mOperatorType = CHINA_TELECOM;
		} else if (imsi.startsWith("46006")) {
			mOperatorType = CHINA_UNION;
		} else if (imsi.startsWith("46007")) {
			mOperatorType = CHINA_MOBILE;
		}

		return mOperatorType;
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

			ret = "";
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
}
