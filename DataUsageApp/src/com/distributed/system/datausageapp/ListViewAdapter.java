package com.distributed.system.datausageapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewAdapter extends BaseAdapter {
	 
	// Declare Variables
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	HashMap<String, String> resultp = new HashMap<String, String>();
	private static String url;
	ArrayList<NameValuePair> getParameter = new ArrayList<NameValuePair>();
	// JSON Node names
		private static final String TAG_ID = "tag";

	String tag_id;
	
	public ListViewAdapter(Context context,
			ArrayList<HashMap<String, String>> arraylist) {
		this.context = context;
		data = arraylist;		
	}
 
	@Override
	public int getCount() {
		return data.size();
	}
 
	@Override
	public Object getItem(int position) {
		return null;
	}
 
	@Override
	public long getItemId(int position) {
		return 0;
	}
 
	public View getView(final int position, View convertView, ViewGroup parent) {
		// Declare Variables
		final TextView first_name;
		final TextView email;
		final TextView phone_number;
		ImageButton edit;
        ImageButton delete;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View itemView = inflater.inflate(R.layout.list_item, parent, false);
		// Get the position
		resultp = data.get(position);
 
		// Locate the TextViews in list_item.xml
		first_name = (TextView) itemView.findViewById(R.id.textName);
		email = (TextView) itemView.findViewById(R.id.textemail);
		phone_number = (TextView) itemView.findViewById(R.id.textPhno);
		edit = (ImageButton) itemView.findViewById(R.id.imgbtnEdit);
		delete = (ImageButton) itemView.findViewById(R.id.btnDelete);
		
 		// Capture position and set results to the TextViews
		first_name.setText(resultp.get(SettingActivity.FNAME));
		email.setText(resultp.get(SettingActivity.EMAIL));
		phone_number.setText(resultp.get(SettingActivity.PHONE));
		final String last_name=resultp.get(SettingActivity.LNAME);
		final String priority=resultp.get(SettingActivity.PRIORITY);
		final String quota=resultp.get(SettingActivity.QUOTA);
		final String threshold=resultp.get(SettingActivity.THRESHOLD);
		final String data_flag=resultp.get(SettingActivity.DFLAG);
		
		edit.setOnClickListener(new View.OnClickListener() {			
			@Override		
			public void onClick(View v) {
				// TODO Auto-generated method stub			

				//Toast.makeText(context, "Click On Edit "+ phone_number.getText(), Toast.LENGTH_LONG).show();
				Intent goToEditUsers = new Intent(context, AddAccountActivity.class);
				goToEditUsers.putExtra("Email", email.getText());
				goToEditUsers.putExtra("PhoneNumber", phone_number.getText());
				goToEditUsers.putExtra("first_name", first_name.getText());
				goToEditUsers.putExtra("last_name", last_name);
				goToEditUsers.putExtra("quota", quota);
				goToEditUsers.putExtra("threshold", threshold);
				goToEditUsers.putExtra("data_flag", data_flag);
				context.startActivity(goToEditUsers);				
				
			}
		});
		
		delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(context, "Click On Delete " + phone_number.getText(), Toast.LENGTH_LONG).show();
				url = Constants.URL+"/remove_account";
				getParameter.add(new BasicNameValuePair("email", (String) email.getText()));
				getParameter.add(new BasicNameValuePair("phone_number", (String) phone_number.getText()));
				new RemoveAccountTask().execute();

			}
		});

		return itemView;
	}
	
	private class RemoveAccountTask extends AsyncTask<Void, Void, String> {

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
			if (tag_id.equalsIgnoreCase("true")) {
				// Navigate to Settings page
				Intent goToSettings = new Intent(context, SettingActivity.class);
				context.startActivity(goToSettings);
			} else {
				// Notify user in case of failure
				Toast.makeText(context, "Delete Operation Failed!", Toast.LENGTH_LONG).show();
			}
			getParameter.clear();
		}
	}

}