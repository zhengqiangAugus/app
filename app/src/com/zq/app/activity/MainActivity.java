package com.zq.app.activity;


import com.zq.app.R;
import com.zq.app.bean.User;
import com.zq.app.fragment.ConversationFragment;
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
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnClickListener,Loadable{
	
	MainLayout main;
	
	View tabs[] = new View[3];
	
	Fragment frags[] = new Fragment[3];
	
	int curIndex = 0;
	
	User user;
	
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main_page);
		AppManager.addLoadable(this);
		
		tabs[0] = findViewById(R.id.chat);
		tabs[0].findViewById(R.id.tab_img).setSelected(true);
		((TextView)tabs[0].findViewById(R.id.tab_text)).setTextColor(getResources().getColorStateList(R.color.tab_blue));
		tabs[1] = findViewById(R.id.contacts);
		tabs[2] = findViewById(R.id.news);
		
		Fragment chat = new ConversationFragment();
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
	
	public void onTabClick(View view){
		int index = 0;
		switch (view.getId()) {
		case R.id.chat:
			index = 0;
			break;
		case R.id.contacts:
			index = 1;
			break;
		case R.id.news:
			index = 2;
			break;
		}
		if(index == curIndex)return;
		TextView tab_text = (TextView) view.findViewById(R.id.tab_text);
		tab_text.setTextColor(getResources().getColorStateList(R.color.tab_blue));
		view.findViewById(R.id.tab_img).setSelected(true);
		tabs[curIndex].findViewById(R.id.tab_img).setSelected(false);
		((TextView)tabs[curIndex].findViewById(R.id.tab_text)).setTextColor(getResources().getColorStateList(R.color.tab_gray));
		
		curIndex = index;
		
	}
	
	public void toggleLeft(View view){
		main.showLeft();
	}
	
}
