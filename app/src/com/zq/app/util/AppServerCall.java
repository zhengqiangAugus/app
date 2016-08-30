package com.zq.app.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;


/**
 * @author 郑强
 * http加载类
 */
public class AppServerCall {
	private static final String TAG = AppServerCall.class.getSimpleName();
	private AsyncHttpClient client;
	//单例模式
	private static AppServerCall instance;
	
	private AppServerCall(){
		client = new AsyncHttpClient();
		client.setTimeout(30000);
	}
	
	/**
	 * 获取单一实例
	 */
	public static AppServerCall getInstance(){
		if(instance==null){
			instance =  new AppServerCall();
		}
		return instance;
	}
	
	public RequestHandle post(String url,RequestParams params,ResponseHandlerInterface handler){
		LogManager.i(TAG, url);
		return client.post(url, params, handler);
	}
	
}
