package com.zq.app.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author 郑强
 * 吐司提示类
 */
public class ToastManager {
	
	/**
	 * 显示短时间的提示
	 * @param context
	 * @param text 内容
	 */
	public static void showShort(Context context,String text){
		Toast.makeText(context, text,Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 显示短时间的提示
	 * @param context
	 * @param text string 的引用resid
	 */
	public static void showShort(Context context,int resid){
		Toast.makeText(context, resid,Toast.LENGTH_SHORT).show();
	}
	/**
	 * 显示长时间的提示
	 * @param context
	 * @param text 内容
	 */
	public static void showLong(Context context,int resid){
		Toast.makeText(context, resid,Toast.LENGTH_LONG).show();
	}
	/**
	 * 显示长时间的提示
	 * @param context
	 * @param text string 的引用resid
	 */
	public static void showLong(Context context,String text){
		Toast.makeText(context, text,Toast.LENGTH_LONG).show();
	}
	
}
