package com.zq.app.util;

import android.util.Log;

/**
 * Log统一管理类
 * 
 * @author way
 * 
 */
public class LogManager {

	public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
	private static final String TAG = LogManager.class.getSimpleName();

	// 下面四个是默认tag的函数
	public static void i(String msg) {
		if (isDebug)
			Log.i(TAG, String.valueOf(msg));
	}

	public static void d(String msg) {
		if (isDebug)
			Log.d(TAG, String.valueOf(msg));
	}

	public static void e(String msg) {
		if (isDebug)
			Log.e(TAG, String.valueOf(msg));
	}

	public static void v(String msg) {
		if (isDebug)
			Log.v(TAG, String.valueOf(msg));
	}

	// 下面是传入自定义tag的函数
	public static void i(String tag, String msg) {
		if (isDebug)
			Log.i(tag, String.valueOf(msg));
	}

	public static void d(String tag, String msg) {
		if (isDebug)
			Log.i(tag, String.valueOf(msg));
	}

	public static void e(String tag, String msg) {
		if (isDebug)
			Log.i(tag, String.valueOf(msg));
	}

	public static void v(String tag, String msg) {
		if (isDebug)
			Log.v(tag, String.valueOf(msg));
	}
}