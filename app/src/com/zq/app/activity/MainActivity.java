package com.zq.app.activity;


import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.zq.app.R;
import com.zq.app.base.Application;
import com.zq.app.bean.User;
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

public class MainActivity extends FragmentActivity implements OnClickListener,Loadable,IYWConversationUnreadChangeListener{
	
	MainLayout main;
	
	View tabs[] = new View[3];
	
	Fragment frags[] = new Fragment[3];
	
	TextView unreadcount;
	
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
		
		frags[0] = Application.getImkit().getConversationFragment();
		frags[1] = Application.getImkit().getContactsFragment();
		frags[2] = new Fragment();
		getSupportFragmentManager().beginTransaction()
		.add(R.id.main, frags[0])
		.add(R.id.main, frags[1])
		.add(R.id.main, frags[2])
		.hide(frags[1])
		.hide(frags[2])
		.show(frags[0])
		.commit();
		
		Application.getImkit().getConversationService().addTotalUnreadChangeListener(this);
		
		load();
	}
	
	Handler handler = new Handler();
	public void load(){
		main = (MainLayout) findViewById(R.id.main_layout);
		unreadcount = (TextView) findViewById(R.id.unreadcount);
		onUnreadChange();
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
		
		getSupportFragmentManager().beginTransaction()
		.hide(frags[curIndex])
		.show(frags[index])
		.commit();
		load();
		
		curIndex = index;
		
	}
	
	public void toggleLeft(View view){
		main.showLeft();
	}
	public void onUnreadChange() {
		handler.post(new Runnable() {
			public void run() {
				int count = Application.getImkit().getConversationService().getAllUnreadCount() ;
				if(count>0&&count <= 99){
					unreadcount.setVisibility(View.VISIBLE);
					unreadcount.setText(count+"");
				}else if(count > 99){
					unreadcount.setVisibility(View.VISIBLE);
					unreadcount.setText("...");
				}else{
					unreadcount.setVisibility(View.INVISIBLE);
				}
			}
		});
	}
	
}
