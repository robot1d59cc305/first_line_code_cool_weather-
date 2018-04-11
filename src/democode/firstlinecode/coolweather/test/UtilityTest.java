package democode.firstlinecode.coolweather.test;

import democode.firstlinecode.coolweather.db.CoolWeatherDB;
import democode.firstlinecode.coolweather.util.MyApplication;
import democode.firstlinecode.coolweather.util.Utility;

/**
 * 
 * 测试Utility类代码.
 * @author Administrator
 *
 */
public class UtilityTest {
	
	private Utility utility = new Utility();
	
	private CoolWeatherDB coolWeatherDB = CoolWeatherDB.getCoolWeatherDB();
	
	public void test() {
		testHandleProvincesResponse();
		testHandleCitiesResponse();
		testHandleCountiesResponse();
		testHandleWeatherResponse();
	}
	
	/**
	 * 测试添加所有省数据到数据库中
	 */
	public void testHandleProvincesResponse() {
		
		// 传递数据进行测试.
		utility.handleProvincesResponse(coolWeatherDB,"01|北京,02|上海,03|天津,04|重庆,05|黑龙江,06|吉林,07|辽宁,08|内蒙古,09|河北,10|山西,11|\r\n" + 
				"陕西,12|山东,13|新疆,14|西藏,15|青海,16|甘肃,17|宁夏,18|河南,19|江苏,20|湖北,21|浙江,22|安徽,23|福建,24|江西,25|湖南,26|贵州,27|四川,28|广东,29|云南,30|广西,31|海南,32|香港,33|澳\r\n" + 
				"门,34|台湾");
		
	}
	
	public void testHandleCitiesResponse() {
		
		utility.handleCityResponse(coolWeatherDB, "1901|南京 ,1902|无锡 ,1903|镇江 ,1904|苏州 ,1905|南通 ,1906|扬州 ,1907|盐城 ,1908|徐\r\n" + 
				"州,1909|淮安,1910|连云港,1911|常州,1912|泰州,1913|宿迁",19);
		
	}
	
	public void testHandleCountiesResponse() {
		
		utility.handleCountiesResponse(coolWeatherDB,"190404|101190404",190404);
		
	}
	
	public void testHandleWeatherResponse() {
		
		utility.handleWeatherResponse(MyApplication.getContext(), "{\"weatherinfo\":\r\n" + 
				"{\"city\":\"昆山\",\"cityid\":\"101190404\",\"temp1\":\"21℃\",\"temp2\":\"9℃\",\r\n" + 
				"\"weather\":\"多云转小雨\",\"img1\":\"d1.gif\",\"img2\":\"n7.gif\",\"ptime\":\"11:00\"}\r\n" + 
				"}");
		
	}
	
}