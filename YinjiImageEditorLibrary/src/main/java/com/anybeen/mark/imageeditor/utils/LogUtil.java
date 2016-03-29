package com.anybeen.mark.imageeditor.utils;

/**
 * 日志工具类
 * by dyzs
 */
public class LogUtil {
	private static final boolean isDebug = true;
	public static void d(String tag, String msg) {
		if (isDebug) {
			android.util.Log.d(tag, msg);
		}
	}
	// 方法的重载
	public static void d(Object obj, String msg) {
		if (isDebug) {
			android.util.Log.d(obj.getClass().getSimpleName(), msg);
		}
	}
	
	
	public static void w(String tag, String msg) {
		if (isDebug) {
			android.util.Log.w(tag, msg);
		}
	}
	// 方法的重载
	public static void w(Object obj, String msg) {
		if (isDebug) {
			android.util.Log.w(obj.getClass().getSimpleName(), msg);
		}
	}
	
	
	public static void e(String tag, String msg) {
		if (isDebug) {
			android.util.Log.e(tag, msg);
		}
	}
	// 方法的重载
	public static void e(Object obj, String msg) {
		if (isDebug) {
			android.util.Log.e(obj.getClass().getSimpleName(), msg);
		}
	}
}
