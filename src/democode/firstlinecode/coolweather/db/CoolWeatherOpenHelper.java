package democode.firstlinecode.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库DB类,通过这个类创造出数据库.
 * @author Administrator
 *
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	
	/**
	 * 表PROVINCE数据结构,这个表代表所有的省.
	 * 这张表的数据结构对应于 http://www.weather.com.cn/data/list3/city.xml 所返回的数据.
	 */
	private static final String CREATE_PROVINCE = "CREATE TABLE Province("
												+ "id Integer primary key autoincrement,"
												+ "province_code text,"
												+ "province_name text)"; 
	
	/**
	 * 表CITY数据结构,这个表代表所有的市.
	 * 这张表的数据结构对应于 http://www.weather.com.cn/data/list3/city19.xml 所返回的数据.
	 */
	private static final String CREATE_CITY = "CREATE TABLE City("
											+ "id Integer primary key autoincrement,"
											+ "city_code text,"
											+ "city_name text,"
											+ "province_id Integer)";
	
	/**
	 * 表COUNTY数据结构,这个表代表所有的县.
	 * 这张表的数据结构对应于 http://www.weather.com.cn/data/list3/city1904.xml 所返回的数据.
	 */
	private static final String CREATE_COUNTY = "CREATE TABLE County("
												+ "id Integer primary key autoincrement,"
												+ "county_code text,"
												+ "county_name text,"
												+ "city_id  Integer)";
	
	/**
	 * 当得到COUNTY中的任意一条数据时可以获得的结果是这个县的天气,只需要在后面将COUNTY表所返回的结果
	 * http://www.weather.com.cn/data/cityinfo/101190404.html 在这个地址的最后一个进行相应的替换就可以了.
	 * */
	
	public CoolWeatherOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
}