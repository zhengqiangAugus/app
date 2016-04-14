package com.zq.app.activity;




import com.zq.app.R;
import com.zq.app.util.AppManager;
import com.zq.app.util.Loadable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
public abstract class BaseActivity extends Activity implements View.OnClickListener,Loadable{
	public void setActitle(CharSequence actitle) {
		TextView title = findById(R.id.title);
		if(title!=null)title.setText(actitle);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addLoadable(this);
	}
	
	public <T extends Object> T findById(int id) {
		@SuppressWarnings("unchecked")
		T findViewById = (T) super.findViewById(id);
		return findViewById;
	}
	
	public void showToast(final String str){
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@Override
	public void setContentView(View view) {
		LayoutInflater inf = getLayoutInflater();
		ViewGroup mview = (ViewGroup) inf.inflate(R.layout.title,null);
		int id = view.getId();
		switch (id) {
		}
		ViewGroup content = (ViewGroup) mview.findViewById(R.id.content);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		view.setLayoutParams(lp);
		content.addView(view);
		super.setContentView(mview);
		initBack();
	}
	
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.move_in,R.anim.move_out);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		LayoutInflater inf = getLayoutInflater();
		ViewGroup view = (ViewGroup) inf.inflate(layoutResID,null);
		setContentView(view);
	}
	Handler handler = new Handler();
	private void initBack(){
		View view = findViewById(R.id.title_back);
		if(view!=null)view.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Loadable prev = AppManager.getAppManager().prevLoadable();
				if(prev instanceof MainActivity){
					MainActivity main = (MainActivity) prev;
					main.closeLeft = false;
				}
				if(prev!=null){
					prev.load();
					if(prev instanceof MainActivity){
						MainActivity main = (MainActivity) prev;
						main.closeLeft = true;
					}
				}
				finish();
			}
		});
	}
	
	public void finish() {
		super.finish();
		AppManager.getAppManager().removeLoadable(this);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			Loadable prev = AppManager.getAppManager().prevLoadable();
			if(prev instanceof MainActivity){
				MainActivity main = (MainActivity) prev;
				main.closeLeft = false;
			}
			if(prev!=null)
			prev.load();
			if(prev instanceof MainActivity){
				MainActivity main = (MainActivity) prev;
				main.closeLeft = true;
			}
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onClick(View arg0) {
	}
	public int dip2px(float dpValue) {
		final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}
