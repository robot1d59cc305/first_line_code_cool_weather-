package democode.firstlinecode.coolweather.activity;

import com.example.democode.firstlinecode.coolweather.R;

import android.app.Activity;
import android.os.Bundle;
import democode.firstlinecode.coolweather.test.CoolWeatherDBTest;
import democode.firstlinecode.coolweather.test.UtilityTest;

public class MainActivity extends Activity {
		
	protected void onCreate(Bundle bundle) {
		
		super.onCreate(bundle);
		super.setContentView(R.layout.weather_layout);
		
		/*  单独的对数据库业务方法进行测试
			CoolWeatherDBTest coolWeatherDBTest = new CoolWeatherDBTest();
			coolWeatherDBTest.test();
		*/
		
		// UtilityTest
		UtilityTest utilityTest = new UtilityTest();
		utilityTest.test();
	}
	
}