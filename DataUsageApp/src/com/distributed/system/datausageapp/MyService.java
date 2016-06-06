package com.distributed.system.datausageapp;

// Change by Aneri

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {

	private Handler mHandler = new Handler();
	private long mStartRX = 0;
	private long mStartTX = 0;
	private long prevBytes = 0;
	private long rxBytes = 0, currentBytes = 0, txBytes = 0, totalBytes = 0;
	public String tag_id, tag_id2;
	public ArrayList<NameValuePair> getParameter, getParameter1;
	private static String url = Constants.URL + "/data";
	private static String url2 = Constants.URL + "/cal_limits";
	private static final String TAG_ID = "tag";
	Context context = this;
	public static String mPhoneNumber;
	private static Boolean thresh_excd = false, quota_excd = false;
	private final static String COMMAND_L_ON = "svc data enable\n ";
	private final static String COMMAND_L_OFF = "svc data disable\n ";
	private final static String COMMAND_SU = "su";

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mStartRX = TrafficStats.getMobileRxBytes();
		mStartTX = TrafficStats.getMobileTxBytes();
		TelephonyManager get_number = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		mPhoneNumber = get_number.getLine1Number();

		mHandler.postDelayed(sendData, 1500);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		thresh_excd = false;
		quota_excd = false;

		return START_STICKY;
	}

	private final Runnable sendData = new Runnable() {
		public void run() {
			// long rxBytes=0, currentBytes=0;
			// Date date = Calendar.getInstance().getTime();

			// String dateString = Integer.toString(date);
			// /Toast.makeText(getApplicationContext(), dateString,
			// Toast.LENGTH_LONG).show();
			prevBytes = totalBytes;
			rxBytes = TrafficStats.getMobileRxBytes() - mStartRX;
			txBytes = TrafficStats.getMobileTxBytes() - mStartTX;
			totalBytes = rxBytes + txBytes;

			// prevBytes=rxBytes;
			currentBytes = totalBytes - prevBytes;
			if(currentBytes<0) currentBytes=0;
			
			// prevBytes=rxBytes;
			// rxBytes=rxBytes/1024;
			String tRxBytes = Long.toString(currentBytes);
			Toast.makeText(getApplicationContext(), tRxBytes, Toast.LENGTH_LONG)
					.show();
			// txBytes = TrafficStats.getMobileTxBytes()- mStartTX;
			// String tTxBytes = Long.toString(txBytes);
			// Toast.makeText(getApplicationContext(), tTxBytes,
			// Toast.LENGTH_LONG).show();
			Log.d("mPhoneNumber", String.valueOf(mPhoneNumber));
			getParameter = new ArrayList<NameValuePair>();
			getParameter.add(new BasicNameValuePair("phoneNumber", String
					.valueOf(mPhoneNumber)));
			getParameter.add(new BasicNameValuePair("dataUsage", Long
					.toString(currentBytes)));
			Log.d("In Runnable", "Before execute");
			setConnection(true, context);
			new PutData().execute();

			// Changed by Aneri
			getParameter1 = new ArrayList<NameValuePair>();
			getParameter1.add(new BasicNameValuePair("phone_number", String
					.valueOf(mPhoneNumber)));
			new CalculateLimits().execute();
			// Change ends

			Log.d("Runnable", "After execute");
			mHandler.postDelayed(sendData, 15000);
		}
	};

	// To remove runnable callbacks when service is stopped
	@Override
	public void onDestroy() {
		// Toast.makeText(this, "service onDestroy", Toast.LENGTH_LONG).show();
		mHandler.removeCallbacks(sendData);
	}

	private class PutData extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET,
					getParameter);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					tag_id = jsonObj.getString(TAG_ID);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog

			if (tag_id.equalsIgnoreCase("true")) {
				Log.d("Insert", "Data is inserted successfully");

			} else {
				Log.d("Insert Fail", "Data insertion fail");
			}

			getParameter.clear();

		}

	}

	// Changed by Aneri
	private class CalculateLimits extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url2, ServiceHandler.GET,
					getParameter1);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					tag_id2 = jsonObj.getString(TAG_ID);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			NotificationManager mNotificationManager;
			int SIMPLE_NOTFICATION_ID = 0;

			if (tag_id2.equalsIgnoreCase("threshold") && thresh_excd == false) {
				thresh_excd = true;
				Log.d("Notification", "true");
				Toast.makeText(getApplicationContext(), "NotificationService",
						Toast.LENGTH_LONG).show();
				mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				final Notification notifyDetails = new Notification(
						R.drawable.ic_launcher,
						"You've got a new notification!",
						System.currentTimeMillis());
				notifyDetails.flags |= Notification.FLAG_AUTO_CANCEL;
				Context context = getApplicationContext();
				String contentTitle = "Threshold Limit Exceeded...";
				String contentText = "Click here to turn off the Mobile Data!";
				Intent notifyIntent = new Intent();
				notifyIntent
						.setAction(android.provider.Settings.ACTION_SETTINGS);

				PendingIntent intent = PendingIntent.getActivity(
						getBaseContext(), 0, notifyIntent,
						android.content.Intent.FLAG_ACTIVITY_NEW_TASK);

				notifyDetails.setLatestEventInfo(context, contentTitle,
						contentText, intent);
				mNotificationManager.notify(SIMPLE_NOTFICATION_ID,
						notifyDetails);
			} else if (tag_id2.equalsIgnoreCase("quota") && quota_excd == false) {
				quota_excd = true;
				Log.d("isRooted: ", "> " + isRooted()
						+ canExecuteCommand("/system/xbin/which su")
						+ canExecuteCommand("/system/bin/which su")
						+ canExecuteCommand("which su"));
				setConnection(false, context);
				//setMobileDataEnabled(context,false);
				setMobileDataState(false);
				mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				final Notification notifyDetails = new Notification(
						R.drawable.ic_launcher,
						"You've got a new notification!",
						System.currentTimeMillis());
				notifyDetails.flags |= Notification.FLAG_AUTO_CANCEL;
				Context context = getApplicationContext();
				String contentTitle = "Quota Limit Exceeded...";
				String contentText = "Click here to turn off the Mobile Data!";
				Intent notifyIntent = new Intent();
				notifyIntent
						.setAction(android.provider.Settings.ACTION_SETTINGS);
				PendingIntent intent = PendingIntent.getActivity(
						getBaseContext(), 0, notifyIntent,
						android.content.Intent.FLAG_ACTIVITY_NEW_TASK);

				notifyDetails.setLatestEventInfo(context, contentTitle,
						contentText, intent);
				mNotificationManager.notify(SIMPLE_NOTFICATION_ID,
						notifyDetails);

			} else
				Log.d("Notification", "Limits not exceeded");

			getParameter1.clear();

		}

	}

	public static void setConnection(boolean enable, Context context) {

		String command;
		if (enable)
			command = COMMAND_L_ON;
		else
			command = COMMAND_L_OFF;

		try {
			Process su = Runtime.getRuntime().exec(COMMAND_SU, null, null);
			DataOutputStream outputStream = new DataOutputStream(
					su.getOutputStream());

			outputStream.writeBytes(command);
			outputStream.flush();

			outputStream.writeBytes("exit\n");
			outputStream.flush();
			try {
				su.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isRooted() {

		// get from build info
		String buildTags = android.os.Build.TAGS;
		if (buildTags != null && buildTags.contains("test-keys")) {
			return true;
		}

		// check if /system/app/Superuser.apk is present
		try {
			File file = new File("/system/app/Superuser.apk");
			if (file.exists()) {
				return true;
			}
		} catch (Exception e1) {
			// ignore
		}

		// try executing commands
		return canExecuteCommand("/system/xbin/which su")
				|| canExecuteCommand("/system/bin/which su")
				|| canExecuteCommand("which su");
	}

	// executes a command on the system
	private static boolean canExecuteCommand(String command) {
		boolean executedSuccesfully;
		try {
			Runtime.getRuntime().exec(command);
			executedSuccesfully = true;
		} catch (Exception e) {
			executedSuccesfully = false;
		}

		return executedSuccesfully;
	}

	/*private void setMobileDataEnabled(Context context, boolean enabled) {

		Log.d("Mobile Data Turn Off", "Entered setMobileDataEnabled method");
		ConnectivityManager dataManager;
		dataManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		Method dataMtd;
		try {
			dataMtd = ConnectivityManager.class.getDeclaredMethod(
					"setDataEnabled", boolean.class);
			dataMtd.setAccessible(true);
			dataMtd.invoke(dataManager, false);
		} catch (NoSuchMethodException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

		catch (IllegalAccessException e) {
			// TODO Auto-generated catch block e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block e.printStackTrace();
		}
		try {
			Log.d("Mobile Data Turn Off", "Entered setMobileDataEnabled method");
			final ConnectivityManager conman = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final Class conmanClass = Class
					.forName(conman.getClass().getName());
			final Field iConnectivityManagerField = conmanClass
					.getDeclaredField("mService");
			iConnectivityManagerField.setAccessible(true);
			final Object iConnectivityManager = iConnectivityManagerField
					.get(conman);
			final Class iConnectivityManagerClass = Class
					.forName(iConnectivityManager.getClass().getName());
			final Method setMobileDataEnabledMethod = iConnectivityManagerClass
					.getDeclaredMethod("setDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);
			setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		} catch (IllegalAccessException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block e.printStackTrace();
		} catch (InvocationTargetException e) { // TODO Auto-generated catch
												// block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block e.printStackTrace();
		}
	}*/
	
	public void setMobileDataState(boolean mobileDataEnabled)
	{
	    try
	    {
	        TelephonyManager telephonyService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

	        Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);

	        if (null != setMobileDataEnabledMethod)
	        {
	            setMobileDataEnabledMethod.invoke(telephonyService, mobileDataEnabled);
	        }
	    }
	    catch (Exception ex)
	    {
	        Log.d("Mobile data turn off","Error setting mobile data state" + ex);
	    }
	}
	// Change ends

}
