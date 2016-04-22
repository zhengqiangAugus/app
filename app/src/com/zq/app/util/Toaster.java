package com.zq.app.util;

import android.content.Context;
import android.widget.Toast;

public class Toaster {
	
	public static void showShort(Context context,CharSequence text){
		Toast.makeText(context, text,Toast.LENGTH_SHORT).show();
	}
	
	public static void showShort(Context context,int text){
		Toast.makeText(context, text,Toast.LENGTH_SHORT).show();
	}
	
	public static void showLong(Context context,CharSequence text){
		Toast.makeText(context, text,Toast.LENGTH_LONG).show();
	}
	
	public static void showLong(Context context,int text){
		Toast.makeText(context, text,Toast.LENGTH_LONG).show();
	}
}
