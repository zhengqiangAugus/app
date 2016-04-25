package com.zq.app.base;

import java.io.File;
import java.util.Properties;

import android.database.sqlite.SQLiteDatabase;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.wxlib.util.SysUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.zq.app.R;
import com.zq.app.bean.DaoMaster;
import com.zq.app.bean.DaoMaster.DevOpenHelper;
import com.zq.app.bean.DaoSession;

public final class Application extends android.app.Application{
	
	public final Properties preference = new Properties();
	
	private static DaoSession dosession ;
	
	public void onCreate() {
		super.onCreate();
		instance = this;
		
		//加载配置
		try {
			preference.load(getResources().openRawResource(R.raw.config));
		} catch (Exception e) {}
		
		//greemDao初始化
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "chat", null);
		SQLiteDatabase db = helper.getWritableDatabase();  
		DaoMaster daoMaster = new DaoMaster(db);  
		dosession = daoMaster.newSession() ;

		
		File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());  //缓存文件夹路径
    	ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
	        .threadPoolSize(5) // default  线程池内加载的数量
	        .threadPriority(Thread.NORM_PRIORITY + 2) // default 设置当前线程的优先级
	        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
	        .denyCacheImageMultipleSizesInMemory()
	        .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //可以通过自己的内存缓存实现
	        .memoryCacheSize(10 * 1024 * 1024)  // 内存缓存的最大值
	        .memoryCacheSizePercentage(15) // default
	        .discCache(new UnlimitedDiscCache(cacheDir)) // default 可以自定义缓存路径  
	        .discCacheSize(100 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
	        .discCacheFileCount(100)  // 可以缓存的文件数量 
	        // default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new Md5FileNameGenerator())加密
	        .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) 
	        //.imageDownloader(new BaseImageDownloader(getApplicationContext())) // default
	        //.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
	        .writeDebugLogs() // 打印debug log
	        .build(); //开始构建
        //Initialize ImageLoader with configuration.  
        ImageLoader.getInstance().init(config);
		
		//必须首先执行这部分代码, 如果在":TCMSSevice"进程中，无需进行云旺（OpenIM）和app业务的初始化，以节省内存;
		SysUtil.setApplication(this);
		if(!SysUtil.isTCMSServiceProcess(this)){
			//第一个参数是Application Context
			//这里的APP_KEY即应用创建时申请的APP_KEY，同时初始化必须是在主进程中
			SysUtil.isMainProcess(getApplicationContext());
			YWAPI.init(this,preference.getProperty(AppConstants.IM_APPKEY));
		}
	}
	
	public static String getValue(String key){
		return getInstance().preference.getProperty(key);
	}
	
	public static DaoSession getDosession() {
		return dosession;
	}

	private static Application instance;
	public static Application getInstance(){
		return instance;
	}
}
