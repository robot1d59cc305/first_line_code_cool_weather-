package democode.firstlinecode.coolweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoUpdateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context,democode.firstlinecode.coolweather.service.AutoUpdateService.class);
		context.startService(i);
	}
	
}