package com.zq.app.activity;

import java.util.List;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.zq.app.R;
import com.zq.app.base.AppConstants;
import com.zq.app.base.Application;
import com.zq.app.bean.User;
import com.zq.app.bean.UserDao;
import com.zq.app.bean.UserDao.Properties;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;

public class GuidePageActivity extends Activity{
	 
	/**
	 * 用户DAO
	 */
	private UserDao userDao = Application.getDosession().getUserDao();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);
		init();
	}
	Intent intent;
	private void init(){
		long start = System.currentTimeMillis();
		
		List<User> users = userDao.queryBuilder().where(Properties.IsLogin.eq(true)).list();
		if(users!=null&&users.size()>0){
			User user = users.get(0);
			YWIMKit imkit = YWAPI.getIMKitInstance(user.getId(),Application.getValue(AppConstants.IM_APPKEY));
			IYWLoginService loginService = imkit.getLoginService();
			YWLoginParam loginParam = YWLoginParam.createLoginParam(user.getId(),user.getPassword());
			loginService.login(loginParam,null);
			Application.setImkit(imkit);
			intent = new Intent(this,MainActivity.class);
		}else{
			intent = new Intent(this,LoginActivity.class);
		}
		long end = System.currentTimeMillis();
		long time = (end - start) / 1000;
		if(time>=2){
			startActivity(intent);
			finish();
		}else{
			new Thread(new Runnable() {
				public void run() {
					SystemClock.sleep(2000);
					startActivity(intent);
					finish();
				}
			}).start();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent home = new Intent(Intent.ACTION_MAIN);  

			home.addCategory(Intent.CATEGORY_HOME);   

			startActivity(home); 
		}
		return true ;
	}
	
}
