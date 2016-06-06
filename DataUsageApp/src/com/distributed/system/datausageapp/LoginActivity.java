package com.distributed.system.datausageapp;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends Activity {
	TextView txtEmail, txtPwd, txtMessage;
	Button btnLogin;
	Context context = this;
	private static String url = Constants.URL+"/login";
	//private static String url = "http://10.114.81.168:8383/login";
	 

	// JSON Node names
	private static final String TAG_ID = "tag";
	public String mPhoneNumber;
	String tag_id;
	Intent goToWelcome;

	ArrayList<NameValuePair> getParameter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//Change 5/21/15
		TelephonyManager get_number = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		
		mPhoneNumber = get_number.getLine1Number();
		Log.d("mPhoneNumber: ", "> " + mPhoneNumber);
		//Change ends
		
		txtEmail = (TextView) findViewById(R.id.txtEmail);
		txtPwd = (TextView) findViewById(R.id.txtPwd);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		txtMessage = (TextView) findViewById(R.id.lblMsg);

		getParameter = new ArrayList<NameValuePair>();

		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goToWelcome = new Intent(LoginActivity.this,
						WelcomeActivity.class);

				btnLogin.setClickable(false);
				
				getParameter.add(new BasicNameValuePair("email", txtEmail.getText().toString()));
				getParameter.add(new BasicNameValuePair("password", txtPwd.getText().toString()));
				
				//Change 5/21/15
				getParameter.add(new BasicNameValuePair("phone_number", mPhoneNumber));
				//Change ends

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
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET,getParameter);

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
			
			//Change 5/21/15
			if (tag_id.equalsIgnoreCase("admin")) {
				txtMessage.setText("");
				goToWelcome.putExtra("role",tag_id);
				// Navigate to Settings page
				LoginActivity.this.startActivity(goToWelcome);
			}
			else if (tag_id.equalsIgnoreCase("user")) {
				txtMessage.setText("");
				goToWelcome.putExtra("role",tag_id);
				// Navigate to Settings page
				LoginActivity.this.startActivity(goToWelcome);
			}
			else {
				// Notify user in case of failed login
				txtMessage.setText("Login Failed! Please Try Again!");
				txtEmail.setText("");
				txtPwd.setText("");
				txtEmail.requestFocus();
			}
			
			//Change ends
			
			btnLogin.setClickable(true);
			getParameter.clear();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
