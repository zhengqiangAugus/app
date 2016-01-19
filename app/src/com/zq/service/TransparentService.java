package com.zq.service;

import com.zq.app.R;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

@SuppressLint("RtlHardcoded")
public class TransparentService extends Service{
	
	WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	View view;
	public TransparentService() {
		super();
		lp.x = 0;
		lp.y = 0;
		lp.type = LayoutParams.TYPE_SYSTEM_OVERLAY;
		lp.format = PixelFormat.RGBA_8888;
		lp.gravity = Gravity.LEFT|Gravity.TOP;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
	}
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@SuppressLint("InflateParams")
	@Override
	public void onCreate() {
		super.onCreate();
		if(view==null){
			WindowManager windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
			LayoutInflater lif = LayoutInflater.from(getApplicationContext());
			view= lif.inflate(R.layout.snow, null);
			windowManager.addView(view,lp);
		}
	}

	@Override
	public void onDestroy() {
		WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		windowManager.removeView(view);
		super.onDestroy();
	}
	
}
