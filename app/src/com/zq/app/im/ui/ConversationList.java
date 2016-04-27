package com.zq.app.im.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListUI;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.kit.contact.YWContactHeadLoadHelper;
import com.zq.app.R;

/**
 * @author 郑强
 * 自定义会话列表
 */
public class ConversationList extends IMConversationListUI{

	public ConversationList(Pointcut pointcut) {
		super(pointcut);
	}
	
	public View getCustomConversationListTitle(Fragment fragment,Context context, LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.customer_ui_conversation_title, null);
		return view;
	}
	
	public boolean enableSearchConversations(Fragment fragment) {
		return true;
	}
	
	public int getCustomBackgroundResId() {
		return R.drawable.home_bg_1;
	}
	
	public boolean needHideNullNetWarn(Fragment fragment) {
        return false;
    }
	
	public  boolean getPullToRefreshEnabled(){
        return true;
    }
	
/*	public View getCustomView(Context context, YWConversation conversation,View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		if(convertView==null){
			View content = inflater.inflate(R.layout.conversation_list_item, parent);
			return content;
		}
		return convertView;
	}
*/	@Override
	public int getCustomItemViewTypeCount() {
		return 1;
	}
	
	public int getCustomItemViewType(YWConversation conversation) {
		return 0;
	}
	
	@Override
	public View getCustomItemView(Fragment fragment,YWConversation conversation, View convertView, int viewType,YWContactHeadLoadHelper headLoadHelper, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(fragment.getActivity());
		if(convertView==null){
			View content = inflater.inflate(R.layout.conversation_list_item, parent,false);
			convertView = content;
		}
		return convertView;
	}
}
