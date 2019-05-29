package com.jicent.jetrun.utils;

import android.util.Log;

public class LogUtil {
	private static boolean debug=true;
	
	public static int e(String tag, String msg, boolean isPrint){
		if (debug&&isPrint) {
			return Log.e(tag, msg);
		}
		return -1;
	}

}
