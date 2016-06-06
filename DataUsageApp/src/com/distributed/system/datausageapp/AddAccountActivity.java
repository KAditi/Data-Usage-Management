package com.distributed.system.datausageapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.AdapterView.OnItemSelectedListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class AddAccountActivity extends Activity implements
		OnSeekBarChangeListener, OnItemSelectedListener {
	EditText etFirstName, etLastName, etEmail, etPhoneNumber;
	Button btnSubmit;
	SeekBar sbQuota, sbThreshold;
	Spinner spTurnOff;
	TextView txtQuota, txtThreshold,txtMsg;
	List<String> spinnerItem = new ArrayList<String>();

	Intent goToSettings;

	public String email = null;
	public String phone_number = null;
	public String first_name = null;
	public String last_name = null;
	public String quota = null;
	public String threshold = null;
	public String data_flag = null;
	
	ArrayList<NameValuePair> getParameter = new ArrayList<NameValuePair>();

	private static String url;
	JSONObject jsonobj = new JSONObject();

	// JSON Node names
	private static final String TAG_ID = "tag";

	String tag_id, quota_val = "0", threshold_val = "0", turnoff_val = "OFF";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_account);
		
		txtQuota = (TextView) findViewById(R.id.txtQuota);
		txtThreshold = (TextView) findViewById(R.id.txtThreshold);
		txtMsg = (TextView) findViewById(R.id.txtMsg);

		etFirstName = (EditText) findViewById(R.id.etFirstName);
		etLastName = (EditText) findViewById(R.id.etLastName);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
		btnSubmit = (Button) findViewById(R.id.btnAddAccount);
		sbQuota = (SeekBar) findViewById(R.id.sbQuota);
		sbThreshold = (SeekBar) findViewById(R.id.sbThreshold);
		spTurnOff = (Spinner) findViewById(R.id.spTurnOff);
		spinnerItem.add("ON");
		spinnerItem.add("OFF");
		
		etFirstName.requestFocus();
		
		sbQuota.setOnSeekBarChangeListener(this);
		sbThreshold.setOnSeekBarChangeListener(this);

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spinnerItem);

		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spTurnOff.setAdapter(dataAdapter);
		spTurnOff.setOnItemSelectedListener(this);

		Intent intent = getIntent();
		Bundle parameters = intent.getExtras();
		if (parameters !=null)
		{
			email = (String)parameters.get("Email");
			phone_number=(String)parameters.get("PhoneNumber");
			first_name=(String)parameters.get("first_name");
			last_name=(String)parameters.get("last_name");
			quota=(String)parameters.get("quota");
			threshold=(String)parameters.get("threshold");
			data_flag=(String)parameters.get("data_flag");
			
			etEmail.setText(email);
			etPhoneNumber.setText(phone_number);
			etFirstName.setText(first_name);
			etLastName.setText(last_name);
			sbQuota.setProgress(Integer.parseInt(quota));
			sbThreshold.setProgress(Integer.parseInt(threshold));
			spTurnOff.setSelection(dataAdapter.getPosition(data_flag));
			
			url = Constants.URL+"/edit_account";
		}
		else url = Constants.URL+"/add_account";
		
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goToSettings = new Intent(AddAccountActivity.this,
						SettingActivity.class);
				btnSubmit.setClickable(false);

				/*
				 * try {
				 * 
				 * jsonobj.put("first_name", etFirstName.getText().toString());
				 * jsonobj.put("last_name", etLastName.getText().toString());
				 * jsonobj.put("email", etEmail.getText().toString());
				 * jsonobj.put("password", ""); jsonobj.put("phone_number",
				 * etPhoneNumber.getText().toString()); jsonobj.put("priority",
				 * "2"); jsonobj.put("quota", "80"); jsonobj.put("threshold",
				 * "70"); jsonobj.put("data_flag", "ON");
				 * jsonobj.put("is_delete", "0"); jsonobj.put("is_valid", "1");
				 * 
				 * } catch (JSONException e) { e.printStackTrace(); }
				 */

				getParameter.add(new BasicNameValuePair("first_name",
						etFirstName.getText().toString()));
				getParameter.add(new BasicNameValuePair("last_name", etLastName
						.getText().toString()));
				getParameter.add(new BasicNameValuePair("email", etEmail
						.getText().toString()));
				
				//change by Aneri 5/21/15
				getParameter.add(new BasicNameValuePair("password", "password"));
				//change ends
				
				getParameter.add(new BasicNameValuePair("phone_number",
						etPhoneNumber.getText().toString()));
				getParameter.add(new BasicNameValuePair("priority", "2"));
				getParameter.add(new BasicNameValuePair("quota", quota_val));
				getParameter.add(new BasicNameValuePair("threshold",
						threshold_val));
				getParameter.add(new BasicNameValuePair("data_flag",
						turnoff_val));
				getParameter.add(new BasicNameValuePair("is_delete", "0"));
				getParameter.add(new BasicNameValuePair("is_valid", "1"));

				// Calling async task to get json
				new AddAccountTask().execute();

			}
		});
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// An item was selected. You can retrieve the selected item using
		// parent.getItemAtPosition(pos)
		turnoff_val = parent.getItemAtPosition(pos).toString();
	}

	public void onNothingSelected(AdapterView<?> parent) {
		turnoff_val = "OFF";
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub

		// change progress text label with current seekbar value
		if (seekBar == sbQuota) {
			txtQuota.setText("Quota value is set to: " + progress + "%");
			quota_val = Integer.toString(progress);

		} else {
			txtThreshold
					.setText("Threshold value is set to: " + progress + "%");
			threshold_val = Integer.toString(progress);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		seekBar.setSecondaryProgress(seekBar.getProgress());
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class AddAccountTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET,
					getParameter);

			Log.d("Parameters: ", "> " + getParameter);
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

			if (tag_id.equalsIgnoreCase("true")) {
				// Navigate to Settings page
				AddAccountActivity.this.startActivity(goToSettings);
			} else {
				// Notify user in case of failure
				txtMsg.setText("Please try again!");
				
				txtQuota.setText("Quota");
				txtThreshold.setText("Threshold");
				etFirstName.setText("");
				etLastName.setText("");
				etEmail.setText("");
				etPhoneNumber.setText("");
				sbQuota.setProgress(0);
				sbThreshold.setProgress(0);
				spTurnOff.setSelection(0);
				etFirstName.requestFocus();
			}
			btnSubmit.setClickable(true);
			getParameter.clear();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_account, menu);
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
}
