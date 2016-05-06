package com.zq.app.im.ui;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.mobileim.WXAPI;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListUI;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactProfileCallback;
import com.alibaba.mobileim.contact.IYWCrossContactProfileCallback;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWP2PConversationBody;
import com.alibaba.mobileim.conversation.YWTribeConversationBody;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.kit.contact.YWContactHeadLoadHelper;
import com.alibaba.mobileim.utility.IMSmilyCache;
import com.alibaba.mobileim.utility.IMUtil;
import com.zq.app.R;
import com.zq.app.activity.MainActivity;
import com.zq.app.base.Application;
import com.zq.app.view.MainLayout;
import com.zq.app.view.swipemenu.SwipeMenu;
import com.zq.app.view.swipemenu.SwipeMenuItem;
import com.zq.app.view.swipemenu.SwipeMenuLayout;
import com.zq.app.view.swipemenu.SwipeMenuView;
import com.zq.app.view.swipemenu.SwipeMenu.MenuType;
import com.zq.app.view.swipemenu.SwipeMenuView.OnSwipeItemClickListener;

/**
 * @author 郑强
 * 自定义会话列表
 */
@SuppressWarnings("deprecation")
public class ConversationList extends IMConversationListUI implements View.OnTouchListener,OnSwipeItemClickListener{

	public ConversationList(Pointcut pointcut) {
		super(pointcut);
	}
	
	public View getCustomConversationListTitle(Fragment fragment,Context context, LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.customer_ui_conversation_title, null);
		return view;
	}
	
	public View getCustomEmptyViewInConversationUI(Context context) {
		/** 以下为示例代码，开发者可以按需返回任何view*/
		TextView textView = new TextView(context);
		textView.setText("还没有会话哦，快去找人聊聊吧!");
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(18);
		return textView;
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
			menu.setConversation(conversation);
			createMenu(menu,fragment.getActivity(),conversation);
			SwipeMenuView menuView = new SwipeMenuView(menu);
			menuView.setOnSwipeItemClickListener(this);
			convertView = new SwipeMenuLayout(content, menuView);
			convertView.setOnTouchListener(this);
			holder.menu = menuView;
			convertView.setTag(holder);
			content.setBackgroundColor(Color.parseColor("#55ffffff"));
		}else{
			holder = (ViewHolder) convertView.getTag();
			holder.menu.getMenu().getMenuItems().clear();
			createMenu(holder.menu.getMenu(),fragment.getActivity(),conversation);
			holder.menu.refresh();
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
        YWConversationType type = conversation.getConversationType();
        if(type == YWConversationType.Tribe){
        	handleTribeConversation(conversation, holder);
        }
        if(type == YWConversationType.P2P){
        	handleP2PConversation(conversation, holder);
        }
        
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
        helper.setHeadView(holder.head, conversation);
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
        setSmilyContent(fragment.getContext(), content, holder);
        return convertView;
	}
	
	private void handleTribeConversation(YWConversation conversation,ViewHolder holder){
		YWTribeConversationBody body = (YWTribeConversationBody) conversation.getConversationBody();
		YWTribe tribe = body.getTribe();
        String name = tribe.getTribeName();
        holder.name.setText(name);
	}
	
	private void handleP2PConversation(YWConversation conversation,ViewHolder holder){
		IYWContact contact = ((YWP2PConversationBody) conversation.getConversationBody()).getContact();
        String userId = contact.getUserId();
        String appkey = contact.getAppKey();
        String conversationName = userId;
        IYWCrossContactProfileCallback callback = WXAPI.getInstance().getCrossContactProfileCallback();
        if (callback != null) {
            IYWContact iContact = callback.onFetchContactInfo(userId, appkey);
            if (iContact != null && !TextUtils.isEmpty(iContact.getShowName())) {
                conversationName = iContact.getShowName();
            }
        } else {
            IYWContactProfileCallback profileCallback = WXAPI.getInstance().getContactProfileCallback();
            if (profileCallback != null) {
                IYWContact iContact = profileCallback.onFetchContactInfo(userId);
                if (iContact != null && !TextUtils.isEmpty(iContact.getShowName())) {
                    conversationName = iContact.getShowName();
                }
            }
        }
        IYWContact iContact = WXAPI.getInstance().getWXIMContact(userId);
        if (iContact != null && !TextUtils.isEmpty(iContact.getShowName())) {
            conversationName = iContact.getShowName();
        }
		holder.name.setText(conversationName);
	}
	
	private class ViewHolder{
        ImageView head;
        TextView unread;
        TextView name;
        TextView content;
        TextView atMsgNotify;
        TextView time;
        SwipeMenuView menu;
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
	public void createMenu(SwipeMenu menu,Context context,YWConversation conversation) {
		SwipeMenuItem item;
			item = new SwipeMenuItem(context);
			if(!conversation.isTop()){
				item.setTitle("置顶");
				item.setWidth(150);
			}else{
				item.setTitle("取消置顶");
				item.setWidth(180);
			}
			item.setTitleColor(Color.WHITE);
			item.setTitleSize(18);
			item.setBackground(new ColorDrawable(Color.parseColor("#C7C7CD")));
			item.setMenuType(MenuType.TOP);
			menu.addMenuItem(item);
		if(conversation.getConversationType() !=YWConversationType.Custom && conversation.getUnreadCount() > 0){
			item = new SwipeMenuItem(context);
			item.setTitle("标为已读");
			item.setTitleColor(Color.WHITE);
			item.setTitleSize(18);
			item.setBackground(new ColorDrawable(Color.parseColor("#FF9D00")));
			item.setWidth(180);
			item.setMenuType(MenuType.UNREAD);
			menu.addMenuItem(item);
		}
		
		item = new SwipeMenuItem(context);
		item.setTitle("删除");
		item.setTitleColor(Color.WHITE);
		item.setTitleSize(18);
		item.setBackground(new ColorDrawable(Color.parseColor("#FF3A30")));
		item.setWidth(150);
		item.setMenuType(MenuType.DELETE);
		menu.addMenuItem(item);
	}
	IWxCallback emptyCallBack = new IWxCallback() {
		public void onSuccess(Object... arg0) {
		}
		public void onProgress(int arg0) {
		}
		public void onError(int arg0, String arg1) {
		}
	};
	public void onItemClick(SwipeMenuView view,SwipeMenu menu, int index) {
		SwipeMenuItem item = menu.getMenuItem(index);
		MenuType type = item.getMenuType();
		IYWConversationService service = Application.getImkit().getConversationService();
		YWConversation conversation = menu.getConversation();
		switch (type) {
		case TOP:
			if(!conversation.isTop()){
				service.setTopConversation(menu.getConversation());
			}else{
				service.removeTopConversation(menu.getConversation());
			}
			break;
		case UNREAD:
			service.markReaded(conversation);
			break;
		case DELETE:
			service.deleteConversation(conversation);
			break;
			default:break;
		}
	}
	private Map<String, CharSequence> mSmilyContentCache = new HashMap<String, CharSequence>();  //表情的本地缓存，加速读取速度用
    IMSmilyCache smilyManager;
    int defaultSmilySize = 0;
    private int contentWidth;

	private void setSmilyContent(Context context, String content,ViewHolder holder) {
		initSmilyManager(context);
		if (content == null || holder.content.getPaint() == null) {
			CharSequence charSequence = mSmilyContentCache.get(content);
			if (charSequence != null) {
				holder.content.setText(charSequence);
			} else {
				CharSequence smilySpanStr = smilyManager.getSmilySpan(context,
						content, defaultSmilySize, false);
				mSmilyContentCache.put(content, smilySpanStr);
				holder.content.setText(smilySpanStr);
			}
		} else {
			CharSequence charSequence = mSmilyContentCache.get(content);
			if (charSequence != null) {
				holder.content.setText(charSequence);
			} else {
				CharSequence text = TextUtils.ellipsize(content,
						holder.content.getPaint(), contentWidth,
						holder.content.getEllipsize());
				CharSequence smilySpanStr = smilyManager.getSmilySpan(context,
						String.valueOf(text), defaultSmilySize, false);
				mSmilyContentCache.put(content, smilySpanStr);
				holder.content.setText(smilySpanStr);
			}
		}
	}

	private void initSmilyManager(Context context) {
		if (smilyManager == null) {
			smilyManager = IMSmilyCache.getInstance();
			defaultSmilySize = (int) context.getResources().getDimension(
					R.dimen.aliwx_smily_column_width);
			int width = context.getResources().getDisplayMetrics().widthPixels;
			contentWidth = width
					- context.getResources().getDimensionPixelSize(
							R.dimen.aliwx_column_up_unit_margin)
					* 2
					- context.getResources().getDimensionPixelSize(
							R.dimen.aliwx_common_head_size)
					- context.getResources().getDimensionPixelSize(
							R.dimen.aliwx_message_content_margin_right);
		}
	}
}
