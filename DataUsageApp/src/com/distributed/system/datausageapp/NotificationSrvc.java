package com.distributed.system.datausageapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class NotificationSrvc extends Service {
	Context context=this;
	public static String mPhoneNumber;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		TelephonyManager get_number = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);		
		mPhoneNumber = get_number.getLine1Number();
		Toast.makeText(getApplicationContext(), mPhoneNumber, Toast.LENGTH_LONG).show();
		return MyService.START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
