package com.example.democode.firstlinecode.coolweather.test;

import com.example.democode.firstlinecode.coolweather.test.util.MyApplication;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import democode.firstlinecode.coolweather.db.CoolWeatherOpenHelper;

public class CoolWeatherOpenHelperTest extends AndroidTestCase {
	
	/**
	 * 测试数据库是否能够创建成功
	 */
	public void testCoolWeatherOpenHelperWritter() {
		CoolWeatherOpenHelper coolWeatherOpenHelper = new CoolWeatherOpenHelper(MyApplication.getContext(),"CoolWeather.db",null,1);
		SQLiteDatabase sqLiteDatabase = coolWeatherOpenHelper.getWritableDatabase();
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
}
