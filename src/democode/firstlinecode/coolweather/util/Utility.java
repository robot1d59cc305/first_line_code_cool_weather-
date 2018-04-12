package democode.firstlinecode.coolweather.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import democode.firstlinecode.coolweather.db.CoolWeatherDB;
import democode.firstlinecode.coolweather.model.City;
import democode.firstlinecode.coolweather.model.County;
import democode.firstlinecode.coolweather.model.Province;

/**
 * 解析服务器返回的数据和代码,并且使用CoolWeatherDB将这些数据存储到数据库中.
 * 主要是服务器返回的数据和代码格式比较特定,因此需要使用到字符串工具类去解析这些数据.然后再将这些数据保存到数据库或者说使用其它的方式进行数据存储,
 * 
 * @author Administrator
 */
public class Utility {

	/**
	 * 对需要较大请求网络的耗时操作使用同步技术进行加持.以保证在大量的访问情况下不会造成意料之外的异常.
	 * 因为用户一打开应用程序可能就会频繁的在省列表与天气信息页面之间进行详细的切换,因此对获取所有省列表的页面的方法进行锁的加持.
	 * @param coolWeatherDB
	 * @param response
	 * @return
	 */
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response) {
		
		// 1.判断response字符串是否为空.判断不为空的情况.
		if (!TextUtils.isEmpty(response)) {
			
			// 2.通过response调用split方法通过截取','字符串来将这些词语进行分组.
			String[] allProvinces = response.split(",");
			
			/* 3.因为从省份当中取出的数据就是类似于这种格式的,所以说通过字符串的一些工具类将这些字符串根据特定的符号进行分割切除,然后在装到数组里面.  
			 * 01|北京,02|上海,03|天津,04|重庆,05|黑龙江,06|吉林,07|辽宁,08|内蒙古,09|河北,10|山西,11|陕西.
			 * */
			if (allProvinces != null && allProvinces.length > 0) {
				
				// 4.使用forEach循环将2中的数组进行遍历,因为即使在进行.
				for (String p:allProvinces) {
					
					// 5.在保证有数据的情况下开始以|符号进行切割.
					String[] array = p.split("\\|");
					
					// 6.将数据存储到Province对象当中,Province就是作为一个JavaBean一样的存在.
					Province province = new Province();
					
					// 7.设置6中的setget方法,让5中的数据进入到了6里面.
					province.setProvinceName(array[1]);
					province.setProvinceCode(array[0]);
					
					// 8.将解析出来的数据存储到Province表.
					coolWeatherDB.saveProvince(province);
					
				}
				
				// 将Boolean值进行返回.因为在这里已经可以通过3的防范机制知道数据是存在的,而且已经解析成功所以就将消息传递出去.
				return true;
				
			}
			
		}
		
		// 默认状态下是假定执行失败的.
		return false;
	}
	
	/**
	 * 解析和处理服务器返回的市级数据.
	 * @param coolWeatherDB
	 * @param response
	 * @param cityId
	 * @return
	 */
	public static boolean handleCityResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId) {
		
		// 1.判断response字符串是不为空的情况.
		if (!TextUtils.isEmpty(response)) {
			
			// 2.截取字符串以逗号的为准.
			String[] allCity = response.split(",");
			
			// 3.将截取之后的数据存放到2中allCounties数组里面.
			if (allCity != null && allCity.length > 0) {
				
				// 4.将3中数组里面的数据使用for循环遍历,
				for (String c : allCity) {
					
					// 5.将4中字符串按照/字符进行截取.
					String[] array = c.split("\\|");
					
					// 6.实例化County对象.
					City city = new City();
					
					// 7.往6中填充数据.
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					
					// 8. 调用coolWeatherDB进行数据存储.
					coolWeatherDB.saveCity(city);
					
				}
				
				// 在3步骤中验证了数据成功之后返回true以表明数据的正确.
				return true;
			}
			
		}
		
		// 默认状态下是假定执行失败的.
		return false;
	}
	
	/**
	 * 解析和处理服务器返回的县级数据
	 * @param coolWeatherDB
	 * @param response
	 * @param cityId
	 * @return
	 */
	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId) {
		
		if (!TextUtils.isEmpty(response)) {
			
			String[] allCounties = response.split(",");
			
			if (allCounties != null && allCounties.length > 0) {
				
				for (String c:allCounties) {
					
					String[] counties = c.split("\\|");
					
					County county = new County();
					
					county.setCountyCode(counties[0]);
					
					county.setCountyName(counties[1]);
					
					county.setCityId(cityId);
					
					coolWeatherDB.saveCounty(county);
					
				}
				
				return true;
			}
			
		}
		
		return false;
	}
	
	/**
	 * 解析服务器返回的JSON数据,并将解析出的数据存储到本地. 主要是用于去解析详细这个县的天气情况是怎么样. 
	 * @param context
	 * @param response
	 */
	public static void handleWeatherResponse(Context context,String response) {
		
		try {
			
			if (!TextUtils.isEmpty(response)) {
				
				JSONObject jsonObject = new JSONObject(response);
				
				JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
				
				String cityName = weatherInfo.getString("city");
				
				String weatherCode = weatherInfo.getString("cityId");
				
				String temp1 = weatherInfo.getString("temp1");
				
				String temp2 = weatherInfo.getString("temp2");
				
				String weatherDesp = weatherInfo.getString("weather");
				
				String publishTime = weatherInfo.getString("ptime");
				
				// 通过这个方式存储到本地SharedPreferences里面,不需要再存储到数据库中了,所以取出来的时候也是通过SharedPreferences读取出来的方式.
				saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
				
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 将服务器返回的所有天气信息存储到SharedPreferences文件中.
	 * @param context
	 * @param cityName
	 * @param weatherCode
	 * @param temp1
	 * @param temp2
	 * @param weatherDesp
	 * @param publishTime
	 */
	public static void saveWeatherInfo(Context context,String cityName,String weatherCode,String temp1,String temp2,String weatherDesp,String publishTime) {
		
		// 格式转换将日期转换为指定的格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		
		// 获取SharedPreferences.Editor对象的引用,将数据保存到SharedPreferences.Editor对象当中.
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		
		/* 将传递进来的参数加入到editor当中,这里不做验证逻辑,为什么? 
		 * 因为之前的时候已经在handleWeatherResponse方法之中进行了逻辑验证,要么就存在数据,要么不存在数据.
		 * */
		editor.putBoolean("city_selected",true);
		editor.putString("city_name",cityName);
		editor.putString("weather_code",weatherCode);
		editor.putString("temp1",temp1);
		editor.putString("temp2",temp2);
		editor.putString("weather_desp",weatherDesp);
		editor.putString("publish_time",publishTime);
		editor.putString("current_date",sdf.format(new Date()));
		editor.commit();
		
	}  
	
}