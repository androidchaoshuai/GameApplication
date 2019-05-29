package com.jicent.jetrun.utils;

import android.content.SharedPreferences;

/**
 * 使用sharedpreferences来存储键值对
 * @author yujia
 *
 */
public class SPUtil {
	
	public static void commit(SharedPreferences sp, String key, boolean value){
		sp.edit().putBoolean(key, value).commit();
	}
	public static void commit(SharedPreferences sp, String key, int value){
		sp.edit().putInt(key, value).commit();
	}
	public static void commit(SharedPreferences sp, String key, float value){
		sp.edit().putFloat(key, value).commit();
	}
	public static void commit(SharedPreferences sp, String key, String value){
		sp.edit().putString(key, value).commit();
	}
	public static void commit(SharedPreferences sp, String key, long value){
		sp.edit().putLong(key, value).commit();
	}
	
	public static int getDataFormSp(SharedPreferences sp, String key,int defValue){
		return sp.getInt(key, defValue);
	}
	public static long getDataFormSp(SharedPreferences sp, String key,long defValue){
		return sp.getLong(key, defValue);
	}
	public static String getDataFormSp(SharedPreferences sp, String key,String defValue){
		return sp.getString(key, defValue);
	}
	public static float getDataFormSp(SharedPreferences sp, String key,float defValue){
		return sp.getFloat(key, defValue);
	}
	public static boolean getDataFormSp(SharedPreferences sp, String key,boolean defValue){
		return sp.getBoolean(key, defValue);
	}
}
