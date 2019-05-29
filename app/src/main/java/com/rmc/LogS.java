package com.rmc;

import android.util.Log;

public class LogS 
{
	public static final boolean DEBUG = true;
	public static int v(String tag, String msg) 
	{
		if (DEBUG) 
			return Log.v(tag, msg);
		else 
			return 0;
	}
	
	public static int d(String tag, String msg) 
	{
		if (DEBUG) 
			return Log.e(tag, msg);
		else 
			return 0;
	}
	
	public static int i(String tag, String msg) 
	{
		if (DEBUG) 
			return Log.i(tag, msg);
		else 
			return 0;
	}
	
	public static int w(String tag, String msg) 
	{
		if (DEBUG) 
			return Log.w(tag, msg);
		else 
			return 0;
	}
	
	public static int e(String tag, String msg) 
	{
		if (DEBUG) 
			return Log.e(tag, msg);
		else 
			return 0;
	}
	
	public static void toFile(String msg) 
	{
//		try
//		{
//			String path =  Environment.getExternalStorageDirectory() + "/MDMLog.txt";
//			File file = new File(path);
//			FileOutputStream outStream = new FileOutputStream(file, true);
//			String formatTime = DateFormat.format("yyyy-MM-dd kk:mm:ss", System.currentTimeMillis()).toString();
//			outStream.write(formatTime.getBytes(), 0, formatTime.getBytes().length);
//			byte[] arrow = { '>', ' ' };
//			outStream.write(arrow, 0, 2);
//			outStream.write(msg.getBytes(), 0, msg.getBytes().length);
//			byte[] ln = { '\n' };
//			outStream.write(ln, 0, 1);
//			outStream.close();
//		} catch (Exception e)
//		{
//			e.printStackTrace();
//		}
	}
	
	public static void log(String msg) 
	{
		if (DEBUG == false) 
			return;
		final String tag = "MyTag";
		if(msg == null)
			Log.d(tag, "it is null");
		else 
			Log.d(tag, msg);
	}

	public static void log(int msg) 
	{
		if (DEBUG == false) 
			return;
		final String tag = "MyTag";
		Log.d(tag, String.valueOf(msg));
	}
}
