package com.distributed.system.datausageapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;


public class MyReceiver extends BroadcastReceiver {


	public boolean wifiConnected=false;
	public boolean mobileConnected=false;
	private Handler mHandler = new Handler();
	private long mStartRX = 0;
	private long mStartTX = 0;
	private long rxBytes=0;
	private long txBytes=0;
	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		
				//Toast.makeText(context,"Network is changed", Toast.LENGTH_LONG).show();
				
				ConnectivityManager connectManager= (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo netInfo = connectManager.getActiveNetworkInfo();
				Intent serviceIntent = new Intent(context, MyService.class);
				//Aneri
				//Intent notificationInt = new Intent(context, NotificationSrvc.class);
				
				if(netInfo==null)
				{
					Toast.makeText(context,"Phone is not connected", Toast.LENGTH_LONG).show();
					context.stopService(serviceIntent);
				}
				
				if(netInfo!=null && netInfo.getType()== ConnectivityManager.TYPE_WIFI)
				{
					wifiConnected=true;
					Toast.makeText(context,"WiFi is connected", Toast.LENGTH_LONG).show();
					context.stopService(serviceIntent);
				}
				if(netInfo!=null && netInfo.getType()== ConnectivityManager.TYPE_MOBILE)
				{
					mobileConnected=true;
					Toast.makeText(context,"Mobile Network is  connected", Toast.LENGTH_LONG).show();
					context.startService(serviceIntent);
					//context.startService(notificationInt);
					
				}
		
	}
	
	

}
