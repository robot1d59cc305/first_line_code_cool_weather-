package democode.firstlinecode.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import democode.firstlinecode.coolweather.receiver.AutoUpdateReceiver;
import democode.firstlinecode.coolweather.util.HttpCallbackListener;
import democode.firstlinecode.coolweather.util.HttpUtil;
import democode.firstlinecode.coolweather.util.Utility;

/**
 * 
 * @author Administrator
 *
 */
public class AutoUpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	
	/**
	 * 
	 * @param intent
	 * @param flags
	 * @param startId
	 * @return
	 */
	public int onStart(Intent intent,int flags,int startId) {
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				updateWeather();
			}
			
		}).start();
		
		// 获取到后台自动执行任务的类.
		AlarmManager manager = (AlarmManager) super.getSystemService(ALARM_SERVICE);
		
		// 计算好时间后台执行任务的执行时间.
//		int anHour = 8 * 60 * 60 * 100; 公式是这样推导出来的.下面的计算方式就直接写出来.
		int anHour = 3 * 1000;
		
		// 
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
		
		Intent i = new Intent(this,AutoUpdateReceiver.class);
		
		PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
		
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
		
		return super.onStartCommand(intent,flags,startId);
	}
	
	/**
	 * 
	 */
	private void updateWeather() {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode = prefs.getString("weather_code","");
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
		
		HttpUtil.sendHttpRequest(address,new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				Log.d("TAG",response);
				Utility.handleWeatherResponse(AutoUpdateService.this,response);
			}

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}
			
		});
		
	}

}