package com.zq.app.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBHelper extends SQLiteOpenHelper{
	private static final int VERSION = 1;
	private static DBHelper instance = null;
	private DBHelper(Context context) {
		super(context, "datebase.db", null, 1);
	}
	public static DBHelper getInstens(Context context) {
	    if (instance == null) {
	    	instance = new DBHelper(context);
	    }
	    return instance;
	}
	//三个不同参数的构造函数
	//带全部参数的构造函数，此构造函数必不可少
	public DBHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
	}
	//带两个参数的构造函数，调用的其实是带三个参数的构造函数
	public DBHelper(Context context,String name){
		this(context,name,VERSION);
	}
	//带三个参数的构造函数，调用的是带所有参数的构造函数
	public DBHelper(Context context,String name,int version){
		this(context, name,null,version);
	}
	//创建数据库
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table user(id  integer primary key autoincrement,user text)";
		db.execSQL(sql);
		sql = "create table face(id  integer primary key autoincrement,img blob,userid int)";
		db.execSQL(sql);
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}
}