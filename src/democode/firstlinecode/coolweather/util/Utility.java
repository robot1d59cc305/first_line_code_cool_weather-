package democode.firstlinecode.coolweather.util;

import democode.firstlinecode.coolweather.db.CoolWeatherDB;

/**
 * 解析服务器返回的数据和代码,并且使用CoolWeatherDB将这些数据存储到数据库中.
 * 主要是服务器返回的数据和代码格式比较特定,因此需要使用到字符串工具类去解析这些数据.然后再将这些数据保存到数据库或者说使用其它的方式进行数据存储,
 * 
 * @author Administrator
 *
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
		
		
		
		return false;
	}

}