package democode.firstlinecode.coolweather.activity;

import com.example.democode.firstlinecode.coolweather.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import democode.firstlinecode.coolweather.service.AutoUpdateService;
import democode.firstlinecode.coolweather.util.HttpCallbackListener;
import democode.firstlinecode.coolweather.util.HttpUtil;
import democode.firstlinecode.coolweather.util.Utility;

/**
 * WeatherActivity 显示天气信息Activity
 * 
 * @author Administrator
 *
 */
public class WeatherActivity extends Activity implements OnClickListener {

	private LinearLayout weatherInfoLayout;

	/**
	 * 用于显示城市名称
	 */
	private TextView cityNameText;

	/**
	 * 用于显示发布时间
	 */
	private TextView publishText;

	/**
	 * 用于显示天气描述信息
	 */
	private TextView weatherDespText;

	/**
	 * 用于显示气温1
	 */
	private TextView temp1Text;

	/**
	 * 用于显示气温2
	 */
	private TextView temp2Text;

	/**
	 * 用于显示当前日期
	 */
	private TextView currentDateText;

	/**
	 * 切换城市按钮
	 */
	private Button switchCity;

	/**
	 * 更新天气按钮
	 */
	private Button refershWeather;

	/**
	 * 
	 */
	protected void onCreate(Bundle bundle) {

		super.onCreate(bundle);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.setContentView(R.layout.weather_layout);
		
		
		// 初始化属性中的各个控件
		weatherInfoLayout = (LinearLayout) super.findViewById(R.id.weather_info_layout);

		cityNameText = (TextView) super.findViewById(R.id.city_name);

		publishText = (TextView) super.findViewById(R.id.publish_text);

		weatherDespText = (TextView) super.findViewById(R.id.weather_desp);

		temp1Text = (TextView) super.findViewById(R.id.temp1);

		temp2Text = (TextView) super.findViewById(R.id.temp2);

		currentDateText = (TextView) super.findViewById(R.id.current_date);

		switchCity = (Button) super.findViewById(R.id.switch_city);

		refershWeather = (Button) super.findViewById(R.id.refresh_weather);

		// 判断用户是否已经选中了某个县的天气并要求显示.
		String countyCode = super.getIntent().getStringExtra("county_code");
		
		/* 判断countyCode属性是否存在 这行代码是确定了用户已经在ChooseAreaActivity页面中点击了省,市,县 三个按钮里面的最后一个县.
		 * 那么在跳转到县的时候就会由Intent顺带一个参数叫做countyCode的到这个县的代号,这里面会根据这个代码来进行查询.
		 */
		if (!TextUtils.isEmpty(countyCode)) {
			// 设置同步中字符串增加用户体验,让用户知道现在正在填充数据.
			publishText.setText("同步中");
			
			// 将显示天气详情信息的这一个LinearLayout展示打开.
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			
			// 显示城市名称的控件进行显示出来.
			cityNameText.setVisibility(View.INVISIBLE);
			
			// 先去查询县对应的天气代号,然后再通过这个天气代号去查询天气情况.
			queryWeatherCode(countyCode);
		} else { 
			// 没有县级代码时就直接显示本地天气,也就是当用户没有去做点击进入选择列表时就显示之前默认存储好的天气就行.
			showWeather();
		} 
		
		// 添加响应事件.
		switchCity.setOnClickListener(this);
		refershWeather.setOnClickListener(this);
		
	}
	
	/**
	 * 给按钮添加响应事件.
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.switch_city:
				 Intent intent = new Intent(this,ChooseAreaActivity.class);
				 intent.putExtra("from_weather_activity",true);
				 super.startActivity(intent);
				 finish();
				 break;
			case R.id.refresh_weather: // 刷新天气信息功能,本质上就是通过之前查询到的县级天气再去查询一遍.
				 publishText.setText("同步中");
				 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
				 String weatherCode = prefs.getString("weather_code","");
				 if (!TextUtils.isEmpty(weatherCode)) {
					 // 通过之前存储的天气代码直接通过这个代码去查询天气即可.
					 queryWeatherInfo(weatherCode);
				 }
				 
				 break;
			default :
				 break;
		}
	}
	
	/**
	 * 查询县级代号所对应的天气代号.
	 * @param countyCode
	 */
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
		queryFromServer(address,"countyCode");
	}
	
	/**
	 * 查询天气代码所对应的天气信息.
	 */
	private void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
		queryFromServer(address,"weatherCode");
	}

	/**
	 * 根据传入的地址和类型去服务器查询天气代号或者天气信息.
	 */
	private void queryFromServer(final String address,final String type) {
		HttpUtil.sendHttpRequest(address,new HttpCallbackListener(){

			/**
			 * 同步成功
			 */
			@Override
			public void onFinish(String response) {
				
				// 如果调用的是countyCode则代表现在需要去查询县对应的天气代号.
				if ("countyCode".equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						String[] array = response.split("\\|");
						if (array != null && array.length == 2) {
							String weatherCode = array[1];
							
							// 将天气代码得到之后,将其放在queryWeatherInfo进行组合,组合成为一个完整的地址.
							queryWeatherInfo(weatherCode);
						}
					}
				} else if ("weatherCode".equals(type)) { // 如果调用的是weatherCode则代表现在需要去查询天气代码说对应的天气信息
					// 1.处理服务器返回的天气信息
					Utility.handleWeatherResponse(WeatherActivity.this,response);
					
					// 2.
					runOnUiThread(new Runnable(){
						@Override
						public void run() {
							// 3.因为在1中已经将所有关于这个县的天气详细数据存放在了本地使用的SharedPreferences的方式,所以说这里面读取的时候也只需要通过读取SharedPreferences的方式即可.
							showWeather();
						}
					});

				}
				
			}

			/**
			 * 同步失败
			 */
			@Override
			public void onError(Exception e) {
				publishText.setText("同步失败");
			}
			
		});
	}
	
	/**
	 * 从SharedPreferences文件中读取出的天气信息,并显示到界面上.
	 */
	private void showWeather() {
		// 1
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		// 2
		cityNameText.setText(prefs.getString("city_name",""));
		
		// 3
		temp1Text.setText(prefs.getString("temp1",""));
		temp2Text.setText(prefs.getString("temp2",""));
		weatherDespText.setText(prefs.getString("weather_desp",""));
		publishText.setText("今天" + prefs.getString("publish_time","") + "发布");
		currentDateText.setText(prefs.getString("current_date",""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		Intent intent = new Intent(this,AutoUpdateService.class);
		startService(intent);
	}
	
}