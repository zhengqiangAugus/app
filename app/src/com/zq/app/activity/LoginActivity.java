package com.zq.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.login.YWLoginCode;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zq.app.R;
import com.zq.app.base.AppConstants;
import com.zq.app.base.Application;
import com.zq.app.bean.User;
import com.zq.app.bean.UserDao;
import com.zq.app.util.AppManager;
import com.zq.app.util.CommUtil;
import com.zq.app.util.Loadable;
import com.zq.app.util.Toaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class LoginActivity extends Activity implements View.OnClickListener,Loadable{
	
	private UserDao userDao = Application.getDosession().getUserDao();
	
	ImageView face;
	View acc_label,pwd_label,acc_cha,pwd_cha,more_acc,acc_list_bar,board;
	LinearLayout acc_list;
	EditText acc,pwd;
	TextView sub_btn;
	private List<User> list = new ArrayList<User>();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		AppManager.addLoadable(this);
		face = (ImageView) findViewById(R.id.face);
		acc_label = findViewById(R.id.acc_label);
		pwd_label = findViewById(R.id.pwd_label);
		acc_cha = findViewById(R.id.clear_input_acc);
		acc_cha.setOnClickListener(this);
		pwd_cha = findViewById(R.id.clear_input_pwd);
		pwd_cha.setOnClickListener(this);
		sub_btn = (TextView) findViewById(R.id.sub_btn);
		sub_btn.setOnClickListener(this);
		more_acc = findViewById(R.id.more_ac);
		more_acc.setOnClickListener(this);
		acc_list = (LinearLayout) findViewById(R.id.acc_list);
		acc_list_bar = findViewById(R.id.acc_list_bar);
		acc = (EditText) findViewById(R.id.acc);
		pwd = (EditText) findViewById(R.id.pwd);
		board = findViewById(R.id.board);
		acc.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
			public void afterTextChanged(Editable arg0) {
				if(arg0.toString()!=null&&arg0.toString().length()>0){
					acc_cha.setVisibility(View.VISIBLE);
				}else{
					acc_cha.setVisibility(View.GONE);
				}
			}
		});
		pwd.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
			public void afterTextChanged(Editable arg0) {
				if(arg0.toString()!=null&&arg0.toString().length()>0){
					pwd_cha.setVisibility(View.VISIBLE);
				}else{
					pwd_cha.setVisibility(View.GONE);
				}
			}
		});
		list = userDao.loadAll();
		load();
	}

	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.sub_btn:
			doLogin();
			break;
		case R.id.more_ac:
			if(acc_list_bar.getVisibility()!=View.VISIBLE){
				sub_btn.setVisibility(View.GONE);
				pwd_label.setVisibility(View.GONE);
				acc_list_bar.setVisibility(View.VISIBLE);
			}else{
				sub_btn.setVisibility(View.VISIBLE);
				pwd_label.setVisibility(View.VISIBLE);
				acc_list_bar.setVisibility(View.GONE);
			}
			break;
		case R.id.clear_input_acc:
			acc.setText(null);
			break;
		case R.id.clear_input_pwd:
			pwd.setText(null);
			break;
		}
	}

	private void doLogin(){
		if(acc.length()==0){
			Toaster.showShort(this,"请输入你的账号或手机号");
			return;
		}
		if(pwd.length()==0){
			Toaster.showShort(this,"请输入你的密码");
			return;
		}
		board.setVisibility(View.VISIBLE);
		sub_btn.setText("正在登录……");
		final YWIMKit imkit = YWAPI.getIMKitInstance(acc.getText().toString(),Application.getValue(AppConstants.IM_APPKEY));
		IYWLoginService loginService = imkit.getLoginService();
		YWLoginParam loginParam = YWLoginParam.createLoginParam(acc.getText().toString(),pwd.getText().toString());
		loginService.login(loginParam,new IWxCallback() {
			public void onSuccess(Object... arg0) {
				User user = new User();
				user.setId(acc.getText().toString());
				user.setPassword(pwd.getText().toString());
				user.setIsLogin(true);
				userDao.insert(user);
				board.setVisibility(View.GONE);
				sub_btn.setText("登录");
				Intent intent = new Intent(LoginActivity.this,MainActivity.class);//imkit.getConversationActivityIntent();
				startActivity(intent);
				AppManager.closeLastLoadable();
			}
			public void onProgress(int arg0) {}
			public void onError(int code, String arg1) {
				board.setVisibility(View.GONE);
				sub_btn.setText("登录");
				switch (code) {
				case YWLoginCode.LOGON_FAIL_TIME_OUT:
					Toaster.showShort(getApplicationContext(), "登录超时");
					break;
				case YWLoginCode.LOGON_FAIL_INVALIDUSER:
					Toaster.showShort(getApplicationContext(), "用户不存在");
					break;
				case YWLoginCode.LOGON_FAIL_INVALIDPWD:
					Toaster.showShort(getApplicationContext(), "密码错误");
					break;
				default:
					Toaster.showShort(getApplicationContext(), "登录失败");
					break;
				}
			}
		});
	}
	
	@SuppressLint("InflateParams")
	public void load() {
		if(list.size()>0){
			LayoutInflater inflater = getLayoutInflater();
			for (final User user:list) {
				final View root = inflater.inflate(R.layout.acc_list_item,null);
				if(!CommUtil.isEmpty(user.getIcon_url())){
					ImageLoader.getInstance().displayImage(user.getIcon_url(),(ImageView)root.findViewById(R.id.face));
				}
				TextView userid = (TextView) root.findViewById(R.id.acc);
				userid.setText(user.getId()+"");
				root.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {
						userDao.delete(user);
						acc_list.removeView(root);
					}
				});
				root.setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {
						chose(user);
					}
				});
				acc_list.addView(root);
			}
		}else{
			more_acc.setVisibility(View.GONE);
			LayoutParams lp = (LayoutParams) acc_cha.getLayoutParams();
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
			acc_cha.setLayoutParams(lp);
		}
	}
	
	
	private void chose(User user){
		acc.setText(user.getId());
		pwd.setText(user.getPassword());
	}
}
