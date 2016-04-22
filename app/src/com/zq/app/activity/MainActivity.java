package com.zq.app.activity;


import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.zq.app.R;
import com.zq.app.base.AppConstants;
import com.zq.app.base.Application;
import com.zq.app.bean.User;
import com.zq.app.bean.UserDao;
import com.zq.app.bean.UserDao.Properties;
import com.zq.app.util.AppManager;
import com.zq.app.util.CommUtil;
import com.zq.app.util.Loadable;
import com.zq.app.view.MainLayout;
import com.zq.service.TransparentService;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends FragmentActivity implements OnClickListener,Loadable{
	
	private UserDao userDao = Application.getDosession().getUserDao();
	
	MainLayout main;
	
	Fragment frags[] = new Fragment[3];
	
	User user;
	
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main_page);
		AppManager.addLoadable(this);
		
		user = userDao.queryBuilder().where(Properties.IsLogin.eq(true)).list().get(0);
		YWIMKit imkit = YWAPI.getIMKitInstance(user.getId(),Application.getValue(AppConstants.IM_APPKEY));
		Fragment chat = imkit.getConversationFragment();
		frags[0] = chat;
		getSupportFragmentManager().beginTransaction().add(R.id.main, chat).commit();
		load();
	}
	
	Handler handler = new Handler();
	public void load(){
		main = (MainLayout) findViewById(R.id.main_layout);
		
	}
	
	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id) {
		case 1:
			Intent intent = new Intent(this,TransparentService.class);
			if(!CommUtil.isServiceWork(this,TransparentService.class.getName())){
				startService(intent);
			}else{
				stopService(intent);
			}
			break;
		}
	}
	
	public void finish() {
		moveTaskToBack(false);
	}
	
}
