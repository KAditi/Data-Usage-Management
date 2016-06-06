package com.distributed.system.datausageapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.os.Build;

public class SummaryActivity extends Activity {

	Button show;
	TextView txtName, dataUsage;
	ListView listview;
	Spinner spinMonth;
	ListViewAdapterGraph adapter;
	JSONObject jsonobject, jsonData;
	JSONArray jsonarray, jsonarrayData;
	private static String url = Constants.URL + "/User_Totaldata_List";
	static String FNAME = "first_name";
	static String DATAUSAGE = "data_usage";
	static String PHONE = "phone_number";
	static String MONTH = "monthSelected";
	ArrayList<HashMap<String, String>> arraylist;
	String monthSelected;
	Long totalData, dataMB;
	int dataGet;
	ProgressBar pbSummary;
	static String URLForData = Constants.URL + "/dataSummery";
	// Spinner spinMonth;

	public String mPhoneNumber;
	ArrayList<NameValuePair> getParameter = new ArrayList<NameValuePair>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);
		// addItemsToList();
		/*
		 * spinMonth = (Spinner)findViewById(R.id.spinMonth);
		 * ArrayAdapter<CharSequence> adapter =
		 * ArrayAdapter.createFromResource(this, R.array.months_array,
		 * android.R.layout.simple_spinner_item);
		 * adapter.setDropDownViewResource
		 * (android.R.layout.simple_spinner_dropdown_item);
		 * spinMonth.setAdapter(adapter);
		 * spinMonth.setOnItemSelectedListener(new MonthItemSelectedListener());
		 */

		/* Aditi start */
		TelephonyManager get_number = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);

		mPhoneNumber = get_number.getLine1Number();
		getParameter.add(new BasicNameValuePair("phonenumber", mPhoneNumber));
		/* Aditi end */

		new CalculateData().execute();

	}

	private class CalculateData extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			arraylist = new ArrayList<HashMap<String, String>>();

			/* Aditi start */
			ServiceHandler sh = new ServiceHandler();
			String jsonstring = sh.makeServiceCall(url, ServiceHandler.GET,
					getParameter);

			/*
			 * ServiceHandler sh = new ServiceHandler(); jsonobject =
			 * ServiceHandler.getJSONfromURL(url);
			 */

			/* Aditi end */

			// JSONObject data = ServiceHandler.getJSONfromURL(URLForData);
			String URLData = Constants.URL + "/dataSummery";
			// String dataReceived = sh.makeServiceCall(URLData,
			// ServiceHandler.GET);
			// dataGet = Long.parseLong(dataReceived);
			// Log.d("Data Received",dataReceived);
			jsonData = ServiceHandler.getJSONfromURL(URLData);
			Log.d("Data received", jsonData.toString());

			try {
				/* Aditi start */
				jsonobject = new JSONObject(jsonstring);
				/* Aditi end */

				// Log.d("VAlue at tag",jsonData.get("tag").toString());
				String data = jsonData.get("tag").toString();
				dataGet = Integer.valueOf(data);
				// dataMB = dataGet/1024;
				Log.d("VAlue at tag", Long.toString(dataGet));
				// JSONObject jsonObj = new JSONObject(dataReceived);
				jsonarray = jsonobject.getJSONArray("lst");
				for (int i = 0; i < jsonarray.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					// jsonobject = jsonarray.getJSONObject(i);
					// Object obj = jsonarray.get(0);
					// jsonarray.getString(0);
					String exp = ",";
					String jsonArrayContent[] = jsonarray.getString(i).split(
							exp);
					Log.d("It is json array", jsonarray.getString(i));
					String name = jsonArrayContent[0].substring(2,
							jsonArrayContent[0].length() - 1);
					String phoneno = jsonArrayContent[1].substring(1,
							jsonArrayContent[1].length() - 1);
					String dataUsage = jsonArrayContent[2].substring(0,
							jsonArrayContent[2].length() - 1);
					Log.d("It is my name", name);
					Log.d("It is my phone", phoneno);
					Log.d("It is my data", dataUsage);

					map.put("first_name", name);
					map.put("phone_number", phoneno);
					map.put("data_usage", dataUsage);
					map.put("monthSelected", monthSelected);

					arraylist.add(map);
				}
			} catch (JSONException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			pbSummary = (ProgressBar) findViewById(R.id.pbSummery);
			pbSummary.setBackgroundColor(Color.GREEN);
			pbSummary.setProgress(dataGet);
			TextView txtTotalData=(TextView)findViewById(R.id.txtToData);
            String data= String.valueOf(dataGet);
            String setValue= data+"Bytes";
            txtTotalData.setText(setValue);
			listview = (ListView) findViewById(R.id.list_user_data);
			adapter = new ListViewAdapterGraph(SummaryActivity.this, arraylist);
			listview.setAdapter(adapter);
		}

	}

}
