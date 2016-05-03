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
import android.widget.ImageView;
import android.widget.TextView;
import cn.swu.swipemenulistview.SwipeMenu;
import cn.swu.swipemenulistview.SwipeMenuItem;
import cn.swu.swipemenulistview.SwipeMenuLayout;
import cn.swu.swipemenulistview.SwipeMenuView;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListUI;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationBody;
import com.alibaba.mobileim.conversation.YWTribeConversationBody;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeType;
import com.alibaba.mobileim.kit.contact.YWContactHeadLoadHelper;
import com.alibaba.mobileim.utility.IMUtil;
import com.zq.app.R;
import com.zq.app.activity.MainActivity;
import com.zq.app.base.Application;
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
		ViewHolder holder = null;
		if(convertView==null){
			View content = inflater.inflate(R.layout.conversation_list_item_2, parent,false);
			holder = new ViewHolder(content);
			SwipeMenu menu = new SwipeMenu(fragment.getActivity());
			menu.setViewType(viewType);
			createMenu(menu,fragment.getActivity());
			SwipeMenuView menuView = new SwipeMenuView(menu);
			convertView = new SwipeMenuLayout(content, menuView);
			convertView.setOnTouchListener(this);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.unread.setVisibility(View.GONE);
        int unreadCount = conversation.getUnreadCount();
        if (unreadCount > 0) {
            holder.unread.setVisibility(View.VISIBLE);
            if (unreadCount > 99){
                holder.unread.setText("99+");
            }else {
                holder.unread.setText(String.valueOf(unreadCount));
            }
        }

        holder.name.setText(conversation.getLastestMessage().getAuthorUserName());

        YWIMKit imKit = Application.getImkit();
        YWContactHeadLoadHelper helper = new YWContactHeadLoadHelper(fragment.getActivity(), null);
        /**
         * !!!注:这里是给群回话设置头像,这里直接设置的默认头像
         * 如果想自由设置可以使用{@link YWContactHeadLoadHelper#setCustomHeadView(ImageView, int, String)},
         * 该方法的第三个参数为加载头像的地址:可以是资源Id或者是sdcard上的file绝对路径以及网络url
         */
        /*if (tribe.getTribeType() == YWTribeType.CHATTING_TRIBE) {
            helper.setTribeDefaultHeadView(holder.head);
        } else {
            helper.setRoomDefaultHeadView(holder.head);
        }*/
        helper.setRoomDefaultHeadView(holder.head);
        //是否支持群@消息提醒
        boolean isAtEnalbe = YWAPI.getYWSDKGlobalConfig().enableTheTribeAtRelatedCharacteristic();
        if (isAtEnalbe){
            if (conversation.hasUnreadAtMsg()) {
                holder.atMsgNotify.setVisibility(View.VISIBLE);
            } else {
                holder.atMsgNotify.setVisibility(View.GONE);
            }
        } else {
            holder.atMsgNotify.setVisibility(View.GONE);
        }
        String content = conversation.getLatestContent();
        holder.content.setText(content);
        holder.time.setText(IMUtil.getFormatTime(conversation.getLatestTimeInMillisecond(), imKit.getIMCore().getServerTime()));
		return convertView;
	}
	
	private class ViewHolder{
        ImageView head;
        TextView unread;
        TextView name;
        TextView content;
        TextView atMsgNotify;
        TextView time;
        ViewHolder(View convertView){
        	this.head = (ImageView) convertView.findViewById(R.id.head);
        	this.unread = (TextView) convertView.findViewById(R.id.unread);
        	this.name = (TextView) convertView.findViewById(R.id.name);
        	this.content = (TextView) convertView.findViewById(R.id.content);
        	this.atMsgNotify = (TextView) convertView.findViewById(R.id.at_msg_notify);
        	this.time = (TextView) convertView.findViewById(R.id.time);
        }
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
