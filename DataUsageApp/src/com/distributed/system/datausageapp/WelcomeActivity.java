package com.distributed.system.datausageapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.os.Build;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class WelcomeActivity extends Activity implements
		NumberPicker.OnValueChangeListener {

	Button btnsetting;
	Intent goToSettings, gotToSummary, goToMobSet;
	TextView twifi, tdata, tchangepwd, tsetting, tbillcycle;
	ToggleButton tgbutton;
	Switch wifiSwitch;
	
	//Change 5/21/15
	ImageView img_setting, img_cycle;
	View vw_bill, vw_setting;
	//Change ends

	private static String url = Constants.URL + "/bill";
	// JSON Node names
	private static final String TAG_ID = "tag";

	String tag_id, role;
	ArrayList<NameValuePair> getParameter;
	Button bset, bcancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);

		/*
		 * btnsetting = (Button)findViewById(R.id.btn_wc_settings);
		 * btnsetting.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub goToSettings = new
		 * Intent(WelcomeActivity.this,SettingPageActivity.class);
		 * WelcomeActivity.this.startActivity(goToSettings); } });
		 */

		twifi = (TextView) findViewById(R.id.lbl_WIFI);
		tdata = (TextView) findViewById(R.id.lbl_datausage);
		// tchangepwd = (TextView) findViewById(R.id.lbl_changepwd);
		wifiSwitch = (Switch) findViewById(R.id.switch_wifi);
		tsetting = (TextView) findViewById(R.id.lbl_setting);
		
		//Change 5/21/15
		vw_bill = (View) findViewById(R.id.vw_bill);
		vw_setting = (View) findViewById(R.id.vw_setting);
		img_setting = (ImageView) findViewById(R.id.img_setting);
		img_cycle = (ImageView) findViewById(R.id.img_cycle);
		//Change ends

		tsetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goToSettings = new Intent(WelcomeActivity.this,
						SettingActivity.class);
				WelcomeActivity.this.startActivity(goToSettings);
				// Toast.makeText(WelcomeActivity.this, "setting click",
				// Toast.LENGTH_LONG).show();
			}
		});

		tbillcycle = (TextView) findViewById(R.id.lbl_billingcycle);
		tbillcycle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showcycle();
			}
		});

		// Code added by Aneri: 5/21/15
		wifiSwitch
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						goToMobSet = new Intent(
								android.provider.Settings.ACTION_SETTINGS);
						goToMobSet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						WelcomeActivity.this.startActivity(goToMobSet);
					}
				});
		// Code ended

		tdata.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotToSummary = new Intent(WelcomeActivity.this,
						SummaryActivity.class);
				WelcomeActivity.this.startActivity(gotToSummary);
			}
		});

		/*
		 * tgbutton = (ToggleButton) findViewById(R.id.toggleBtn1);
		 * tgbutton.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub if (tgbutton.isChecked()) Toast.makeText(WelcomeActivity.this,
		 * "Toggle ON", Toast.LENGTH_LONG).show(); else
		 * Toast.makeText(WelcomeActivity.this, "Toggle OFF",
		 * Toast.LENGTH_LONG).show(); } });
		 */

		wifiSwitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (wifiSwitch.isChecked())
					Toast.makeText(WelcomeActivity.this, "WiFi - ON",
							Toast.LENGTH_LONG).show();
				else
					Toast.makeText(WelcomeActivity.this, "WiFi - OFF",
							Toast.LENGTH_LONG).show();
			}
		});

		//Change 5/21/15
		Intent intent = getIntent();
		Bundle parameters = intent.getExtras();
		if (parameters != null) {
			role = (String) parameters.get("role");
			Log.d("role: ", "> " + role);

			if(role.equalsIgnoreCase("admin"))
			{
				tsetting.setVisibility(View.VISIBLE);
				tbillcycle.setVisibility(View.VISIBLE);
				img_setting.setVisibility(View.VISIBLE);
				img_cycle.setVisibility(View.VISIBLE);
				vw_bill.setVisibility(View.VISIBLE);
				vw_setting.setVisibility(View.VISIBLE);
			}
			else if (role.equalsIgnoreCase("user")) {
				tsetting.setVisibility(View.GONE);
				tbillcycle.setVisibility(View.GONE);
				img_setting.setVisibility(View.GONE);
				img_cycle.setVisibility(View.GONE);
				vw_bill.setVisibility(View.GONE);
				vw_setting.setVisibility(View.GONE);
			}
		}
		//Change ends

	}

	public void showcycle() {
		final Dialog d = new Dialog(WelcomeActivity.this);
		d.setTitle("Change Billing Cycle");
		d.setContentView(R.layout.billcycle_dialog);
		bset = (Button) d.findViewById(R.id.btn_billcycle_set);
		bcancel = (Button) d.findViewById(R.id.btn_billcycle_cancel);

		final NumberPicker np = (NumberPicker) d
				.findViewById(R.id.numberPick_date);
		np.setMaxValue(31);
		np.setMinValue(1);

		np.setWrapSelectorWheel(true);
		np.setOnValueChangedListener(this);

		getParameter = new ArrayList<NameValuePair>();

		bset.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(WelcomeActivity.this,np.getValue() ,
				// Toast.LENGTH_LONG).show();
				Log.i("set value", String.valueOf(np.getValue()));

				getParameter.add(new BasicNameValuePair("fromday", String
						.valueOf(np.getValue())));

				new BillTask().execute();

				d.dismiss();
			}
		});

		bcancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				d.dismiss();
			}
		});
		d.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldval, int newval) {
		// TODO Auto-generated method stub
		Log.i("value is", "" + newval);
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class BillTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST,
					getParameter);

			Log.d("getParameter: ", "> " + getParameter);
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
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.d("msg", "Inside try onPostExecute");
			getParameter.clear();
		}
	}
}
