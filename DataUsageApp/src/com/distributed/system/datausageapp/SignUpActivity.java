package com.distributed.system.datausageapp;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import android.widget.TextView;
import android.os.Build;

public class SignUpActivity extends Activity {

	TextView txtfname, txtlname, txtemail, txtpwd, txtphno,lblmsg;
	Button btnsignup;
	
	private static String url = Constants.URL+"/signup";	 

	// JSON Node names
	private static final String TAG_ID = "tag";

	String tag_id;
	Intent goToSettings;

	ArrayList<NameValuePair> getParameter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sign_up);

		txtfname = (TextView) findViewById(R.id.txt_signup_firstname);
		txtlname = (TextView) findViewById(R.id.txt_signup_lastname);
		txtemail = (TextView) findViewById(R.id.txt_signup_email);
		txtpwd = (TextView) findViewById(R.id.txt_signup_pwd);
		txtphno = (TextView) findViewById(R.id.txt_signup_phone);
		btnsignup = (Button) findViewById(R.id.btn_signup);
		lblmsg = (TextView) findViewById(R.id.lblmsg);
		
		getParameter = new ArrayList<NameValuePair>();

		btnsignup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goToSettings = new Intent(SignUpActivity.this,MainActivity.class);

				btnsignup.setClickable(false);
				
				getParameter.add(new BasicNameValuePair("email", txtemail.getText().toString()));
				getParameter.add(new BasicNameValuePair("password", txtpwd.getText().toString()));
				getParameter.add(new BasicNameValuePair("first_name", txtfname.getText().toString()));
				getParameter.add(new BasicNameValuePair("last_name", txtlname.getText().toString()));
				getParameter.add(new BasicNameValuePair("phone_number", txtphno.getText().toString()));

				// Calling async task to get json
				new LoginTask().execute();
			}
		});
		
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class LoginTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST,getParameter);

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
			if (tag_id.equalsIgnoreCase("true")) {
				// Navigate to Settings page
				lblmsg.setText("");
				SignUpActivity.this.startActivity(goToSettings);
			} else {
				// Notify user in case of failed login
				lblmsg.setText("Enter Valid Email / Phone Number.");
			}
			btnsignup.setClickable(true);
			getParameter.clear();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
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
