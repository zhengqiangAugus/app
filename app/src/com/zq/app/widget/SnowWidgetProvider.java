package com.zq.app.widget;

import com.zq.app.R;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

@SuppressLint("NewApi")
public class SnowWidgetProvider extends AppWidgetProvider{
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}
	
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
		for(int i=0;i<appWidgetIds.length;i++){
			RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.snow_manage);
			appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
}
