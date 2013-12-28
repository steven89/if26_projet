package fr.utt.if26.uttcoins.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class JsonHttpRequest extends AsyncTask<String, Integer, JSONObject> {

//	private class test{
//		private Map<String, Class<?>> map = new HashMap<String, Class<?>>();
//		
//		public <T> T get(String key){
//			return 
//		}
//	}
	private BasicHttpParams params;
	private JsonCallback jsonCallback;
	// wildcard utilisé : map vérouillée en lecture seule
	private static HashMap<String, Class<? extends HttpRequestBase>> methodClasses; 
	
	public JsonHttpRequest(JsonCallback callback){
		this.params = new BasicHttpParams();
		this.jsonCallback = callback;
		this.initMethodClasses();
	}
	
	private void initMethodClasses(){
		if(methodClasses == null){
			methodClasses = new HashMap<String, Class<? extends HttpRequestBase>>();
			methodClasses.put("GET", HttpGet.class);
			methodClasses.put("POST", HttpPost.class);
			methodClasses.put("PUT", HttpPut.class);
			methodClasses.put("DELETE", HttpDelete.class);
		}
	}
	
	@Override
	protected JSONObject doInBackground(String... args){
		// Juste pour les appels task.execute(method, url) ET task.execute(url)
		String method, url;
		HttpRequestBase request;
		HttpResponse response;
		HttpClient client = new DefaultHttpClient();
		if(args.length < 2){
			method = "GET";
			url = args[0];
		}else{
			method = args[0];
			url = args[1];	
		}
		Log.i("REQUEST", "PREPARING with method = " + method+" and url = "+url);
		Log.i("REQUEST",  "MedthodClass : " + methodClasses.get(method).toString());
		try {
			request = methodClasses.get(method).getConstructor(String.class).newInstance(url);
			request.setParams(this.params);
			Log.i("REQUEST", "EXECUTING");
			response = client.execute(request);
			Log.i("REQUEST", "READING");
	        return this.readResponse(response.getEntity().getContent());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return null;
	}
	
	@Override
	protected void onPostExecute(JSONObject result){
		if(this.jsonCallback != null) 
			this.jsonCallback.call(result);
	}
	
	public void putParam(String key, Object value){
		if(this.params == null)
			this.params = new BasicHttpParams();
		this.params.setParameter(key, value);
	}
	
	public void setParams(Map<String, Object> paramMap){
		this.params.clear();
		for(String key : paramMap.keySet()){
			this.params.setParameter(key, paramMap.get(key));
		}
	}
	
	public void setParams(HttpParams httpParams){
		this.params = (BasicHttpParams) httpParams;
	}
	
	private JSONObject readResponse(InputStream stream){
		String lineRead = "";
		StringBuilder stringResponse = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		try {
			while((lineRead = reader.readLine())!=null){
				stringResponse.append(lineRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.JSONParse(stringResponse.toString());
	}
	
	private JSONObject JSONParse(String jsonString){
		JSONObject jsonResponse = new JSONObject();
		try {
			jsonResponse = new JSONObject(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonResponse;
	}

}
