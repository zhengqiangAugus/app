package com.zq.app.view.swipemenu;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.mobileim.conversation.YWConversation;

import android.content.Context;

public class SwipeMenu {
	
	public static enum MenuType{
		TOP,UNREAD,DELETE;
	}
	
	private Context mContext;
	private List<SwipeMenuItem> mItems;
	private int mViewType;
	private YWConversation conversation;

	public YWConversation getConversation() {
		return conversation;
	}

	public void setConversation(YWConversation conversation) {
		this.conversation = conversation;
	}

	public SwipeMenu(Context context) {
		mContext = context;
		mItems = new ArrayList<SwipeMenuItem>();
	}

	public Context getContext() {
		return mContext;
	}

	public void addMenuItem(SwipeMenuItem item) {
		mItems.add(item);
	}

	public void removeMenuItem(SwipeMenuItem item) {
		mItems.remove(item);
	}

	public List<SwipeMenuItem> getMenuItems() {
		return mItems;
	}

	public SwipeMenuItem getMenuItem(int index) {
		return mItems.get(index);
	}

	public int getViewType() {
		return mViewType;
	}

	public void setViewType(int viewType) {
		this.mViewType = viewType;
	}

}
