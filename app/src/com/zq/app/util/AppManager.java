package com.zq.app.util;
import java.util.Stack;
import android.app.Activity;

public class AppManager{
	private static AppManager instance;
	
	private Stack<Loadable> acs = new Stack<Loadable>();
	
	public synchronized static AppManager getAppManager(){
		if(instance == null)
			instance = new AppManager();
		return instance;
	}
	
	public void addLoadable(Loadable ac){
		this.acs.add(ac);
	}
	
	public void removeLoadable(Loadable ac){
		this.acs.remove(ac);
	}
	
	public Loadable prevLoadable(){
		if(acs.size()>1)
			return acs.get(acs.size()-2);
		return null;
	}
	
	public void finishBefore(int leftCount){
		for (Loadable activity : acs) {
			System.out.println(activity);
		}
		while(acs.size()>leftCount){
			closeLastLoadable();
		}
	}
	
	public void reloadPrev(){
		Loadable ac = prevLoadable();
		if(ac!=null){
			ac.load();
		}
	}
	
	public void reloadCurrent(){
		Loadable ac = currentLoadable();
		if(ac!=null){
			ac.load();
		}
	}
	
	public void closeLastLoadable(){
		Loadable ac = currentLoadable();
		if(ac!=null){
			acs.remove(ac);
			((Activity)ac).finish();
		}
	}
	
	public Loadable currentLoadable(){
		if(acs.size()>0)
			return this.acs.lastElement();
		else
			return null;
	}
}

