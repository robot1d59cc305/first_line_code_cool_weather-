package com.example.democode.firstlinecode.coolweather.test.util;

import android.app.Application;
import android.content.Context;

/**
 * 
 * MyApplication类.
 * @author Administrator
 *
 */
public class MyApplication extends Application {
	
	/**
	 * 全局Context
	 */
	public static Context context;
	
	/**
	 * 赋值实例
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
	}
	
	/**
	 * 返回Context的实例
	 * @return
	 */
	public static Context getContext() {
		return context;
	}
	
}