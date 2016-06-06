package com.distributed.system.datausageapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
 
public class ServiceHandler {
 
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
 
    public ServiceHandler() {
 
    }
 
    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, (String) null);
    }
 
    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
   public String makeServiceCall(String url, int method,
            List<NameValuePair> params) {
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
             
            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
 
                httpResponse = httpClient.execute(httpPost);
 
            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
 
                httpResponse = httpClient.execute(httpGet);
 
            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         
        return response;
 
    }
    
    public String makeServiceCall(String url, int method,
            String params) {
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            StringEntity httpEntity = new StringEntity(params);
            HttpResponse httpResponse = null;
             
            // Checking http request method type
            if (method == POST) {
            	httpEntity.setContentType("application/json;charset=UTF-8");
            	httpEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(httpEntity);
                }
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                httpResponse = httpClient.execute(httpPost);
 
            } 
            
            response = EntityUtils.toString(httpResponse.getEntity());
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         
        return response;
 
    }
    
    public static JSONObject getJSONfromURL(String url) {
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;
 
		// Download JSON data from URL
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
 
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
 
		// Convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}
 
		try {
 
			jArray = new JSONObject(result);
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
 
		return jArray;
	}

}