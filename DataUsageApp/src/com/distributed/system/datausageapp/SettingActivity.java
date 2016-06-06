package com.distributed.system.datausageapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SettingActivity extends Activity {
	Button btnAdd;
	TextView txtHeading;
	Intent goToAddAccount;
	private static String url = Constants.URL+"/getUsers";	 
	ArrayList<HashMap<String, String>> arraylist;
	ListView listview;
	ListViewAdapter adapter;
	JSONObject jsonobject;
	JSONArray jsonarray;
	
	static String FNAME = "first_name";
	static String EMAIL = "email";
	static String PHONE = "phone_number";
	static String LNAME = "last_name";
	static String PRIORITY = "priority";
	static String QUOTA = "quota";
	static String THRESHOLD = "threshold";
	static String DFLAG = "data_flag";	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		txtHeading=(TextView) findViewById(R.id.txtHeading);
		
		btnAdd = (Button) findViewById(R.id.btnAdd);
		
		btnAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goToAddAccount = new Intent(SettingActivity.this,
						AddAccountActivity.class);
				SettingActivity.this.startActivity(goToAddAccount);
				
			}
		});
		
		new UserDetailTask().execute();
	}

	// DownloadJSON AsyncTask
		private class UserDetailTask extends AsyncTask<Void, Void, Void> {
			
			@Override
			protected Void doInBackground(Void... params) {
				
				arraylist = new ArrayList<HashMap<String, String>>();
				
				//ServiceHandler sh = new ServiceHandler();
				jsonobject  = ServiceHandler.getJSONfromURL(url);
				
				try 
				{
					jsonarray = jsonobject.getJSONArray("lst");
					for (int i = 0; i < jsonarray.length(); i++) 
					{ 				
						HashMap<String, String> map = new HashMap<String, String>();
						jsonobject = jsonarray.getJSONObject(i);
						 
						map.put("first_name", jsonobject.getString("first_name"));
						map.put("email", jsonobject.getString("email"));
						map.put("phone_number", jsonobject.getString("phone_number"));
						map.put("last_name", jsonobject.getString("last_name"));
						map.put("priority", jsonobject.getString("priority"));
						map.put("quota", jsonobject.getString("quota"));
						map.put("threshold", jsonobject.getString("threshold"));
						map.put("data_flag", jsonobject.getString("data_flag"));
											
						arraylist.add(map);
					}
				}	
				catch (JSONException e) 
				{ 
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void args)  {
				listview = (ListView) findViewById(R.id.list_users);
				adapter = new ListViewAdapter(SettingActivity.this, arraylist);
				listview.setAdapter(adapter);
			}
		}

		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
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
