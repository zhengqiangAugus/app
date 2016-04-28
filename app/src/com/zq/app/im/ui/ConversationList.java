package com.zq.app.im.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import cn.swu.swipemenulistview.SwipeMenu;
import cn.swu.swipemenulistview.SwipeMenuItem;
import cn.swu.swipemenulistview.SwipeMenuLayout;
import cn.swu.swipemenulistview.SwipeMenuView;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListUI;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.kit.contact.YWContactHeadLoadHelper;
import com.zq.app.R;
import com.zq.app.activity.MainActivity;
import com.zq.app.view.MainLayout;

/**
 * @author 郑强
 * 自定义会话列表
 */
public class ConversationList extends IMConversationListUI implements View.OnTouchListener{

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
	
	@Override
	public int getCustomItemViewTypeCount() {
		return 1;
	}
	
	public int getCustomItemViewType(YWConversation conversation) {
		return 0;
	}
	
	@Override
	public View getCustomItemView(Fragment fragment,YWConversation conversation, View convertView, int viewType,YWContactHeadLoadHelper headLoadHelper, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(fragment.getActivity());
		SwipeMenuLayout layout;
		if(convertView==null){
			View content = inflater.inflate(R.layout.conversation_list_item, parent,false);
			SwipeMenu menu = new SwipeMenu(fragment.getActivity());
			menu.setViewType(viewType);
			createMenu(menu,fragment.getActivity());
			SwipeMenuView menuView = new SwipeMenuView(menu);
			layout = new SwipeMenuLayout(content, menuView);
			layout.setOnTouchListener(this);
			convertView = content;
		}else{
			layout = (SwipeMenuLayout) convertView;
		}
		return layout;
	}
	
	MainLayout mainLayout = null;
	public void onActivityCreated(Bundle savedInstanceState, Fragment fragment) {
		super.onActivityCreated(savedInstanceState, fragment);
		if(fragment.getActivity() instanceof MainActivity){
			MainActivity ac = (MainActivity) fragment.getActivity();
			mainLayout = ac.getMainLayout();
		}
	}
	
	private SwipeMenuLayout touchMenu;
	public boolean onTouch(View view, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			touchMenu = (SwipeMenuLayout) view;
			if(mainLayout!=null){
				mainLayout.selectSwipeMenu(touchMenu);
			}
		}
		return view.onTouchEvent(event);
	}
	public void createMenu(SwipeMenu menu,Context context) {
		// Test Code
		SwipeMenuItem item = new SwipeMenuItem(context);
		item.setTitle("置顶");
		item.setTitleColor(Color.WHITE);
		item.setTitleSize(18);
		item.setBackground(new ColorDrawable(Color.GRAY));
		item.setWidth(150);
		menu.addMenuItem(item);

		item = new SwipeMenuItem(context);
		item.setTitle("删除");
		item.setTitleColor(Color.WHITE);
		item.setTitleSize(18);
		item.setBackground(new ColorDrawable(Color.RED));
		item.setWidth(150);
		menu.addMenuItem(item);
	}
}
