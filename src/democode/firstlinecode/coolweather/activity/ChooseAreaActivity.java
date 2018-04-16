package democode.firstlinecode.coolweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.democode.firstlinecode.coolweather.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import democode.firstlinecode.coolweather.db.CoolWeatherDB;
import democode.firstlinecode.coolweather.model.City;
import democode.firstlinecode.coolweather.model.County;
import democode.firstlinecode.coolweather.model.Province;
import democode.firstlinecode.coolweather.util.HttpCallbackListener;
import democode.firstlinecode.coolweather.util.HttpUtil;
import democode.firstlinecode.coolweather.util.Utility;

/**
 * 
 * @author Administrator
 *
 */
public class ChooseAreaActivity extends Activity {
	
	/**
	 * 省
	 */
	public static final int LEVEL_PROVINCE = 0;
	
	/**
	 * 市
	 */
	public static final int LEVEL_CITY = 1;
	
	/**
	 * 县
	 */
	public static final int LEVEL_COUNTY = 2;
	
	/**
	 * 对话框进度条
	 */
	private ProgressDialog progressDialog;
	
	/**
	 * 文本展示
	 */
	private TextView titleText;
	
	/**
	 * 列表展示
	 */
	private ListView listView;
	
	/**
	 * ListView包装器
	 */
	private ArrayAdapter<String> adapter;
	
	/**
	 * 数据库操作类
	 * 用于使用这个类来进行查询数据库中的数据,再放置到List容器当中,将数据展现出来.
	 */
	private CoolWeatherDB coolWeatherDB;
	
	/**
	 * 数据List 在使用适配器的时候,将这个属性放到适配器当中,填充数据.
	 */
	private List<String> dataList = new ArrayList<String>();
	
	/**
	 * 省列表 专门从数据库查询关于省的数据然后将数据放置到这个容器当中.
	 */
	private List<Province> provinceList;
	
	/**
	 * 市列表 专门从数据库查询关于市的数据然后将数据放置到这个容器当中.
	 */
	private List<City> cityList;
	
	/**
	 * 县列表 专门从数据库查询关于县的数据然后将数据放置到这个容器当中.
	 */
	private List<County> countyList;
	
	/**
	 * 选中的省份
	 */
	private Province selectedProvince;
	
	/**
	 * 选中的城市
	 */
	private City selectedCity;
	
	/**
	 * 当前选中的级别
	 */
	private int currentLevel;
	
	/**
	 * 是否从WeatherActivity中跳转过来
	 */
	private boolean isFromWeatherActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// 0.
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 1.如果从WeatherActivity跳转过来则会带一个参数from_weather_activity,如果没有这个参数则值为false;
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity",false);
		
		// 2.获取SharedPreferences文件.因为在获取天气信息的时候是将天气使用SharedPreferences方式保存的.
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		if (prefs.getBoolean("city_selected",false) && !isFromWeatherActivity ) {
			Intent intent = new Intent(this,WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		
		// 3.取消头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// 4.设置布局文件
		super.setContentView(R.layout.choose_area);
		
		// 5.引入ListView控件资源
		listView = (ListView) super.findViewById(R.id.list_view);
		
		// 6.标题控件
		titleText = (TextView) super.findViewById(R.id.title_text);
		
		// 7.适配器资源导入
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
		
		// 8.将适配器资源设置到listView容器当中
		listView.setAdapter(adapter);
		
		// 9.获取CoolWeatherDB实例
		coolWeatherDB = CoolWeatherDB.getCoolWeatherDB();
		
		/* 10.为listView控件中的每一个选项都进行点击事件监听,在进行点击的时候获取到相应的索引
		 * 否则怎么可能知道点击了哪一个要从哪加载.
		 * */
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				// 如果当前加载的ListView控件数据是关于省的数据. 
				// 在查询数据的时候都会将当前的currentLevel设置为对应的省,市,县的三种值.
				if (currentLevel == LEVEL_PROVINCE) {
					
					selectedProvince = provinceList.get(position);
					
					queryCity();
					
				} else if (currentLevel == LEVEL_CITY) { // 如果当前加载的ListView控件数据是关于市的数据.
					
					selectedCity = cityList.get(position);
					queryCounties();
					
				} else if (currentLevel == LEVEL_COUNTY) { // 如果当前加载的ListView控件数据是关于县的数据.
					
					String countyCode = countyList.get(position).getCountyCode();
					
					Intent intent = new Intent(ChooseAreaActivity.this,WeatherActivity.class);
					
					intent.putExtra("county_code",countyCode);
					
					// 这里调用的是OnItemClickListener这个监听器接口里面的startActivity()方法.
					startActivity(intent);
					
					// finish()释放资源.
					finish();
					
				}
				
			}
			
		});
		
		// 11.加载省级数据
		queryProvinces();
	}
	
	/**
	 * 查询全国所有的省,优先从数据库查询,如果没有查询到再去服务器上查询.
	 */
	private void queryProvinces() {
		
		// 调用专门的loadProvince方法获取到所有的省数据列表以一个List容器返回赋值个provinceList容器里面
		provinceList = coolWeatherDB.loadProvince();
		
		// 如果能够从本地数据库当中获取到数据那么就不会去访问网络.
		if (provinceList.size() > 0) {
			
			// 清除数据dataList里面本来装有的数据,避免因为上一次遗留下来的数据而导致这次重复添加.
			dataList.clear();
			
			for (Province province:provinceList) {
				dataList.add(province.getProvinceName());
			}
			
			// 更新ListView控件里面的数据
			adapter.notifyDataSetChanged();
			
			// 设置数量
			listView.setSelection(0);
			
			// 设置titleText的标题名称叫做"中国"
			titleText.setText("中国");
			
			// 设置当前选中的级别为省
			currentLevel = LEVEL_PROVINCE;
			
		} else { 
			//如果本地数据库确实不存在数据,则从服务器上去查询.
			queryFromServer(null,"province");
		}
		
	}
	
	/**
	 * 查询选中省内所有的市,优先从数据库查询,如果没有查询到再去服务器上查询.
	 */
	private void queryCity() {
		
		cityList = coolWeatherDB.loadCity(selectedProvince.getId());
		
		if (cityList.size() > 0) {
			
			dataList.clear();
			
			for (City city:cityList) {
				dataList.add(city.getCityName());
			}
			
			adapter.notifyDataSetChanged();
			
			listView.setSelection(0);
			
			titleText.setText(selectedProvince.getProvinceName());
			
			currentLevel = LEVEL_CITY;
			
		} else {
			
			queryFromServer(selectedProvince.getProvinceCode(),"city");
			
		}
		
	}
	
	/**
	 * 查询选中市内所有的县,优先从数据库查询,如果没有查询到再去服务器上去查询.
	 */
	private void queryCounties() {
		
		countyList = coolWeatherDB.loadCounty(selectedCity.getId());
				
		if (countyList.size() > 0) {
			
			dataList.clear();
			
			for (County county:countyList) {
				
				dataList.add(county.getCountyName());
				
			}
			
			adapter.notifyDataSetChanged();
			
			listView.setSelection(0);
			
			titleText.setText(selectedCity.getCityName());
			
			currentLevel = LEVEL_COUNTY;
			
		} else {
			queryFromServer(selectedCity.getCityCode(),"county");
		}
		
	}
	
	/**
	 * 根据传入的代码和类型从服务器上查询省市县数据.
	 * @param code
	 * @param type
	 */
	private void queryFromServer(final String code,final String type) {
		String address;
		
		// 如果code不为空的情况下则将code的值放在这个字符串里面.否则就不将这个code的值放进去(访问所有的市,县)数据,只去访问这个叫city.xml的文件(访问所有的省)数据.
		if (!TextUtils.isEmpty(code)) {
			address="http://www.weather.com.cn/data/list3/city" + code + ".xml";
		} else {
			address="http://www.weather.com.cn/data/list3/city.xml";
		}
		
		// 将因为这里没有数据,因此要从网络上获取数据就需要一定时间的等待,为了加深用户体验就在这里放了一个进度条框,让用户知道现在正在加载数据.
		showProgressDialog();
		
		// 发送HTTP请求.
		HttpUtil.sendHttpRequest(address,new HttpCallbackListener() {

			/**
			 * 发送成功
			 */
			@Override
			public void onFinish(String response) {
				
				boolean result = false;
				
				if ("province".equals(type)) {
					result = Utility.handleProvincesResponse(coolWeatherDB,response);
				} else if("city".equals(type)) {
					result = Utility.handleCityResponse(coolWeatherDB, response,selectedProvince.getId());
				} else if("county".equals(type)) {
					result = Utility.handleCountiesResponse(coolWeatherDB, response,selectedCity.getId());
				}
				
				// 如果获取到了数据,那么数据就已经存放到了数据库,则此时再调用上面所写的三个方法任意一个进行数据查询.
				if (result) {
					
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							
							// 将数据存储到数据库之后,则将进度条框关闭.
							closeProgressDialog();
							
							/*
							 * 执行执行数据的查询方法
							 * */
							if ("province".equals(type)) {
								queryProvinces();
							} else if("city".equals(type)) {
								queryCity();
							} else if("county".equals(type)) {
								queryCounties();
							}
							
						}
						
					});
					
				}
				
			}

			/**
			 * 发送失败
			 */
			@Override
			public void onError(final Exception e) {
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						
						// 关闭进度对话框.
						closeProgressDialog();
						
						// 使用Toast提示加载失败.
						Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
						
						e.printStackTrace();
					}
					
				});
			}
			
		});
		
	}
	
	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	
	/**
	 * 根据Back按键,根据当前的级别来判断,此时应该返回市列表,省列表,还是直接退出.
	 */
	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTY) {
			queryCity();
		} else if (currentLevel == LEVEL_CITY) {
			queryProvinces();
		} else {
			// 是否从天气信息列表中直接性的跳转过来,如果是,则直接跳转到信息列表而不在显示其它的列表
			if (isFromWeatherActivity) {
				Intent intent = new Intent(this,WeatherActivity.class);
				startActivity(intent);
			}
			finish();
		}
	}
	
}