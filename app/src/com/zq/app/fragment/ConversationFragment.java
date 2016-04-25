package com.zq.app.fragment;

import java.util.List;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.conversation.YWConversation;
import com.zq.app.R;
import com.zq.app.base.Application;
import com.zq.app.bean.User;
import com.zq.app.bean.UserDao;
import com.zq.app.bean.UserDao.Properties;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ConversationFragment extends Fragment{
	
	private UserDao userDao = Application.getDosession().getUserDao();
	User user;
	@SuppressLint("InflateParams")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_conversation, null);
		
		user = userDao.queryBuilder().where(Properties.IsLogin.eq(true)).list().get(0);
		
		YWIMCore imcore = YWAPI.createIMCore(user.getId(), YWAPI.getAppKey());
		List<YWConversation> list = imcore.getConversationService().getConversationList();
		for (YWConversation ywConversation : list) {
			System.err.println(ywConversation.getLatestContent());
		}
		
		return view;
	}
	
}
