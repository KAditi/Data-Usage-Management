package com.distributed.system.datausageapp;

import java.util.ArrayList;
import java.util.HashMap;
import org.achartengine.ChartFactory;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;



public class ListViewAdapterGraph extends BaseAdapter {
	
	// Declare Variables
			Context context;
			LayoutInflater inflater;
			ArrayList<HashMap<String, String>> data;
			HashMap<String, String> resultp = new HashMap<String, String>();
			private static String url = Constants.URL;
			ArrayList<NameValuePair> getParameter = new ArrayList<NameValuePair>();
			// JSON Node names
				private static final String TAG_ID = "tag";
				Spinner spinMonth;

			String tag_id,phoneNumber,monthSelected;
			ArrayList<HashMap<Integer,Long>> arraylist;
			JSONObject jsonobject;
			JSONArray jsonarray;
			HashMap<Integer, Long> mapTest = new HashMap<Integer, Long>();
			Long[] arrayData;
			
			public ListViewAdapterGraph(Context context,
					ArrayList<HashMap<String, String>> arraylist) {
				this.context = context;
				data = arraylist;
				addItemsToList();
			}
			
			public void addItemsToList()
			{
				//View itemView = inflater.inflate(R.layout.list_item, parent, false);
				
				
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
				final TextView dataUsage;
				ImageButton show;
		        

				inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
				View itemView = inflater.inflate(R.layout.list_item_summary, parent, false);
				// Get the position
				resultp = data.get(position);
		 
				// Locate the TextViews in list_item.xml
				Log.d("My first name id",itemView.findViewById(R.id.txtName).toString());
				first_name = (TextView) itemView.findViewById(R.id.txtName);
				Log.d("My first name str",first_name.toString());
				Log.d("result tp value name",resultp.get(SummaryActivity.FNAME));
				Log.d("result tp value data",resultp.get(SummaryActivity.DATAUSAGE));
				Log.d("result tp value phone",resultp.get(SummaryActivity.PHONE));
				//Log.d("result tp value month",monthSelected);
				dataUsage = (TextView) itemView.findViewById(R.id.totaldata);								
				show = (ImageButton) itemView.findViewById(R.id.btnShow);
				/************************* select months ************************/
				
				spinMonth = (Spinner)itemView.findViewById(R.id.spinMonth);
				ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
				        R.array.months_array, android.R.layout.simple_spinner_item);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				Log.d("Spin month id",itemView.findViewById(R.id.spinMonth).toString());
				spinMonth.setAdapter(adapter);
				spinMonth.setOnItemSelectedListener(new MonthItemSelectedListener());
				//Log.d("result tp value month",monthSelected);
				
		 		// Capture position and set results to the TextViews
				first_name.setText(resultp.get(SummaryActivity.FNAME));
				
				//first_name.setText("Aditi");
				//dataUsage.setText(resultp.get(SummaryActivity.DATAUSAGE));
				String totalD = ","+resultp.get(SummaryActivity.DATAUSAGE)+"Bytes";
                dataUsage.setText(totalD);
				phoneNumber= resultp.get(SummaryActivity.PHONE);
				
				
				show.setOnClickListener(new View.OnClickListener() {			
					@Override		
					public void onClick(View v) {
						// TODO Auto-generated method stub	
						//LineGraph line = new LineGraph();
						//Intent goToGraph = new Intent(context,LineGraph.class);
						getParameter.add(new BasicNameValuePair("phonenumber", resultp.get(SummaryActivity.PHONE)));
						getParameter.add(new BasicNameValuePair("frommonth",monthSelected));
						Log.d("user phone number ", resultp.get(SummaryActivity.PHONE));
						new GetGraphData().execute();
					}
				});
				
				
			    return itemView;
			}
			private class MonthItemSelectedListener implements OnItemSelectedListener{

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos,
						long id) {
					// TODO Auto-generated method stub
					monthSelected = parent.getItemAtPosition(pos).toString();
					Log.d("Month selected",monthSelected);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			}

			
			
			private class LineGraph{
				int m=arraylist.size();
				int x[] = new int[arrayData.length];
				long y[]= new long[100];				
				
				public Intent getIntent(Context context){
				
					TimeSeries series = new TimeSeries("Line");
					for(int i=0;i<arrayData.length;i++ )
					{
						x[i]=i+1;
						
					}
					Log.d("Series",series.toString());
					Log.d("array data length",Integer.toString(arrayData.length));
					Log.d("x length",Integer.toString(x.length));
					//series.add(1, 2);
					for(int j=0;j<arrayData.length;j++)
					{
						//series.add(x[j], arrayData[j]);
						
						series.add(x[j],arrayData[j]);
						Log.d("X[i]",Integer.toString(x[j]));
						//Log.d("arrayData",Long.toString(arrayData[j]));
						
					}
					XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
					dataset.addSeries(series);
					XYMultipleSeriesRenderer mrenderer = new XYMultipleSeriesRenderer();
					XYSeriesRenderer renderer = new XYSeriesRenderer();
					renderer.setColor(Color.BLUE);
					mrenderer.addSeriesRenderer(renderer);
					
					Intent intent = ChartFactory.getLineChartIntent(context, dataset, mrenderer,"Data Usage");
					return intent;
					
				}
			}
			
			
			
			private class GetGraphData extends AsyncTask<Void, Void, String> {

				@Override
				protected String doInBackground(Void... arg0) {
					arraylist = new ArrayList<HashMap<Integer, Long>>();
					String URL = url+"/dateGraph";
					ServiceHandler sh = new ServiceHandler();
					//jsonobject  = sh.getJSONfromURL(url);
					//ServiceHandler sh = new ServiceHandler();
					String jsonString = sh.makeServiceCall(URL, ServiceHandler.GET,getParameter);
					Log.d("I got the string",jsonString);
					
					try 
					{
						if (jsonString != null) {
							jsonobject = new JSONObject(jsonString);
							
						}
						jsonarray = jsonobject.getJSONArray("lst");
						int j=1;
						int size = jsonarray.length();
						arrayData = new Long[size];
						Log.d("J son array length",Integer.toString(jsonarray.length()));
						for (int i = 0; i <jsonarray.length(); i++) 
						{ 				
							HashMap<Integer, Long> map = new HashMap<Integer, Long>();
							jsonobject = jsonarray.getJSONObject(i);
							Log.d("json object of i",Long.toString(jsonobject.getLong(Long.toString(j))));
							//map.put(j, jsonobject.getLong(Long.toString(j)));
							arrayData[i]= jsonobject.getLong(Long.toString(j));
							//arrayData[i]= (long) Math.random();
							j++;
							//Log.d("inserted map",map.get(j).toString());
							//arraylist.add(map);
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
				protected void onPostExecute(String result) {
					super.onPostExecute(result);
					Log.d("msg", "Inside try onPostExecute");
					
						// Navigate to Settings page
						//Intent goToSettings = new Intent(context, SummaryActivity.class);
						LineGraph line = new LineGraph();
						Intent drawGraph = line.getIntent(context);
						context.startActivity(drawGraph);
					
					getParameter.clear();
				}
			}




}
