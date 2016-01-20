package com.zq.app.activity;


import com.zq.app.R;
import com.zq.app.util.AppManager;
import com.zq.app.util.Loadable;
import com.zq.app.view.MainLayout;
import com.zq.service.TransparentService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener,Loadable{
	
	MainLayout main;
	public boolean closeLeft = false;
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main_page);
		AppManager.getAppManager().addLoadable(this);
		load();
		closeLeft = true;
	}
	
	Handler handler = new Handler();
	@SuppressLint("NewApi")
	public void load(){
		main = (MainLayout) findViewById(R.id.main_layout);
		if(closeLeft){
			main.closeLeft();
		}
		/*View open_snow = findViewById(R.id.open_snow);
		open_snow.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(),TransparentService.class);
				startService(intent);
			}
		});*/
	}
	
	
	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id) {
		}
	}
}
