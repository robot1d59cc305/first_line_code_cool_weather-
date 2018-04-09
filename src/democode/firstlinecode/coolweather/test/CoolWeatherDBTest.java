package democode.firstlinecode.coolweather.test;

import democode.firstlinecode.coolweather.db.CoolWeatherDB;
import democode.firstlinecode.coolweather.model.City;
import democode.firstlinecode.coolweather.model.County;
import democode.firstlinecode.coolweather.model.Province;

public class CoolWeatherDBTest {
	
	/**
	 * 
	 */
	private static CoolWeatherDB coolWeatherDB;
	
	public void test() {
		
		getWritableDatabasesTest();
		
		saveProvinceTest();
		
		saveCityTest();
		
		saveCountyTest();
		
		loadProvinceTest();
		
		loadCityTest();
		
		loadCountyTest();
		
	}
	
	/**
	 * 写出数据库文件到databases文件夹中.
	 */
	public void getWritableDatabasesTest() {
		coolWeatherDB = CoolWeatherDB.getCoolWeatherDB();
	}
	
	/**
	 * 保存一条省的数据到数据库表Province中.
	 */
	public void saveProvinceTest() {
		coolWeatherDB.saveProvince(new Province("北京","05"));
	}
	
	/**
	 * 获取从数据库中所有省的信息.
	 */
	public void loadProvinceTest() {
		System.out.println("获取从数据库中所有省的信息-->"+coolWeatherDB.loadProvince());
	}
	
	/**
	 * 保存一条市的数据到数据库表City中.
	 */
	public void saveCityTest() {
		coolWeatherDB.saveCity(new City("朝阳区","0510",05));
	}
	
	/**
	 * 获取从数据库中所有市的信息.
	 */
	public void loadCityTest() {
		System.out.println("获取从数据库中所有市的信息-->"+coolWeatherDB.loadCity(05));
	}
	
	/**
	 * 保存一条县的数据到数据库表County中.
	 */
	public void saveCountyTest() {
		coolWeatherDB.saveCounty(new County("某小区","051003",10));
	}
	
	/**
	 * 获取从数据库中所有县的信息.
	 */
	public void loadCountyTest() {
		System.out.println("获取从数据库中所有县的信息-->"+coolWeatherDB.loadCounty(10));
	}
	
}