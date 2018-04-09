package democode.firstlinecode.coolweather.activity;

import com.example.democode.firstlinecode.coolweather.R;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import democode.firstlinecode.coolweather.db.CoolWeatherOpenHelper;
import democode.firstlinecode.coolweather.test.CoolWeatherDBTest;
import democode.firstlinecode.coolweather.util.MyApplication;

public class MainActivity extends Activity {
		
	protected void onCreate(Bundle bundle) {
		
		super.onCreate(bundle);
		super.setContentView(R.layout.weather_layout);
		
//		CoolWeatherDBTest coolWeatherDBTest = new CoolWeatherDBTest();
//		coolWeatherDBTest.test();
		
		
	}
	
}