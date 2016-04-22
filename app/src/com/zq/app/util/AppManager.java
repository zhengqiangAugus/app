package com.zq.app.util;
import java.util.Stack;
import android.app.Activity;

public class AppManager{
	
	private static Stack<Loadable> acs = new Stack<Loadable>();
	
	
	public static void addLoadable(Loadable ac){
		acs.add(ac);
	}
	
	public static void removeLoadable(Loadable ac){
		acs.remove(ac);
	}
	
	public static Loadable prevLoadable(){
		if(acs.size()>1)
			return acs.get(acs.size()-2);
		return null;
	}
	
	public static void finishBefore(int leftCount){
		for (Loadable activity : acs) {
			System.out.println(activity);
		}
		while(acs.size()>leftCount){
			closeLastLoadable();
		}
	}
	
	public static void reloadPrev(){
		Loadable ac = prevLoadable();
		if(ac!=null){
			ac.load();
		}
	}
	
	public static void reloadCurrent(){
		Loadable ac = currentLoadable();
		if(ac!=null){
			ac.load();
		}
	}
	
	public static void closeLastLoadable(){
		Loadable ac = currentLoadable();
		if(ac!=null){
			acs.remove(ac);
			((Activity)ac).finish();
		}
	}
	
	public static Loadable currentLoadable(){
		if(acs.size()>0)
			return acs.lastElement();
		else
			return null;
	}
}

