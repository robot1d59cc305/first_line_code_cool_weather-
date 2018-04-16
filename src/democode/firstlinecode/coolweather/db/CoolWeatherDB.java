package democode.firstlinecode.coolweather.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import democode.firstlinecode.coolweather.model.City;
import democode.firstlinecode.coolweather.model.County;
import democode.firstlinecode.coolweather.model.Province;
import democode.firstlinecode.coolweather.util.MyApplication;

/**
 * CoolWeatherDB 通过这个类去访问数据库
 * @author Administrator
 */
public class CoolWeatherDB {

	/**
	 * 数据库名称
	 */
	private final String DB_NAME = "CoolWeather.db";

	/**
	 * 数据库版本
	 */
	private final int DB_VERSION = 1;

	/**
	 * CoolWeatherDB名称
	 */
	private static CoolWeatherDB coolWeatherDB;

	/**
	 * SQLiteDatabase操作工具类
	 */
	private SQLiteDatabase db;

	/**
	 * 私有化构造器,在构造器当中创建数据库以及对sqLiteDatabase等属性进行赋值.
	 */
	private CoolWeatherDB() {
		CoolWeatherOpenHelper coolWeatherOpenHelper = new CoolWeatherOpenHelper(MyApplication.getContext(), DB_NAME,
				null, DB_VERSION);
		db = coolWeatherOpenHelper.getWritableDatabase();
	}

	/**
	 * 通过这个方法返回CoolWeatherDB实例.
	 * @return
	 */
	public synchronized static CoolWeatherDB getCoolWeatherDB() {

		if (coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB();
		}

		return coolWeatherDB;

	}

	/**
	 * 保存从服务器上获取的Pointer类的数据.
	 * @param province
	 */
	public void saveProvince(Province province) {

		if (province != null) {

			ContentValues values = new ContentValues();

			values.put("province_code", province.getProvinceCode());

			values.put("province_name", province.getProvinceName());

			db.insert("Province", null, values);

		}

	}

	/**
	 * 从数据库读取全国所有的省份信息.
	 */
	public List<Province> loadProvince() {

		List<Province> provinceList = new ArrayList<Province>();

		Cursor cursor = db.query("Province", null, null, null, null, null, null);

		if (cursor.moveToFirst()) {

			do {

				Province province = new Province();

				province.setId(cursor.getInt(cursor.getColumnIndex("id")));

				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));

				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));

				provinceList.add(province);

			} while (cursor.moveToNext());

		}

		return provinceList;
	}

	public void saveCity(City city) {
		if (city != null) {
			
			ContentValues contentValues = new ContentValues();
			
			contentValues.put("city_name",city.getCityName());
			
			contentValues.put("city_code",city.getCityCode());
			
			contentValues.put("province_id",city.getProvinceId());
			
			db.insert("City",null,contentValues);
			
		}
	}
	
	public List<City> loadCity(int provinceId) {
		
		List<City> cityList = new ArrayList<City>();
		
		Cursor cursor = db.query("City",null,"province_id=?",new String[]{String.valueOf(provinceId)},null,null,null);
		
		if (cursor.moveToFirst()) {
			
			do {
				
				City city = new City();
				
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				
				city.setProvinceId(provinceId);
				
				cityList.add(city);
				
			} while(cursor.moveToNext());
			
		}
		
		return cityList;
	}
	
	/**
	 * 将County实例存储到数据库.
	 */
	public void saveCounty(County county) {
		
		if (county != null) {
			
			ContentValues contentValues = new ContentValues();
			contentValues.put("county_code",county.getCountyCode());
			contentValues.put("county_name",county.getCountyName());
			contentValues.put("city_Id",county.getCityId());
			db.insert("County",null,contentValues);
			
		}
		
	}
	
	/**
	 * 查询某市下的所有县数据
	 * @param cityId
	 * @return
	 */
	public List<County> loadCounty(int cityId) {
		
		List<County> countyList = new ArrayList<County>();
		
		Cursor cursor = db.query("County",null,"city_id=?",new String[]{String.valueOf(cityId)},null,null,null);
		
		if (cursor.moveToFirst()) {
			
			do {
				
				County county = new County();
				
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				
				county.setCityId(cityId);
				
				countyList.add(county);
				
			} while(cursor.moveToNext());
			
		}
		
		return countyList;
		
	}

}