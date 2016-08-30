/**
 * 
 */
package com.zq.app.util;


import org.apache.http.Header;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.zq.app.bean.Response;

/**
 * <p>Title:ResponseHandler</p>
 * <p>Description:</p>
 * @author 郑强
 * @date 2016年6月20日
 */
public class ResponseHandler extends JsonHttpResponseHandler{
	
	private static final String TAG = ResponseHandler.class.getSimpleName();
	
	public ResponseHandler() {
		super();
	}

	public ResponseHandler(String encoding) {
		super(encoding);
	}

	public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
		LogManager.e(TAG,throwable.getMessage());
		Response resp = new Response();
		resp.setMsg("网络异常，请重新尝试");
		resp.setState(false);
		resp.setTotal(0);
		onFailure(resp);
	}
	
	public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		LogManager.i(TAG,response.toString());
		Gson gson = new Gson();
		try {
			Response resp = gson.fromJson(response.toString(),Response.class);
			onSuccess(resp);
		} catch (Exception e) {
			e.printStackTrace();
			Response resp = new Response();
			resp.setMsg("网络异常，请重新尝试");
			resp.setState(false);
			resp.setTotal(0);
			onFailure(resp);
		}
	}

	public void onSuccess(Response response){
	}
	
	public void onFailure(Response response) {
	}
}
