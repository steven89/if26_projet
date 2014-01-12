package fr.utt.if26.uttcoins.utils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import fr.utt.if26.uttcoins.io.JSONHandler;

@Deprecated
public class JsonHttpRequest extends AsyncTask<String, Integer, JSONObject> {
	//params for non enclosing entity request (GET, DELETE)
		private BasicHttpParams httpParams;
		//params for enclonsing entity request (POST, PUT)
		private JSONObject jsonParams;
		private JsonCallback jsonCallback;
		private String method, url;
		private HttpRequestBase request;
		private HttpResponse response;
		private HttpClient client;
		private boolean error;
		private Bundle errorObject;
		
		// wildcard utilisé : map vérouillée en lecture seule
		protected static HashMap<String, Class<? extends HttpRequestBase>> methodClasses; 
		
		static{
			if(methodClasses == null){
				methodClasses = new HashMap<String, Class<? extends HttpRequestBase>>();
				methodClasses.put("GET", HttpGet.class);
				methodClasses.put("POST", HttpPost.class);
				methodClasses.put("PUT", HttpPut.class);
				methodClasses.put("DELETE", HttpDelete.class);
			}
		}
		
		public JsonHttpRequest(String method, String url, JsonCallback callback){
			this.httpParams = new BasicHttpParams();
			this.jsonParams = new JSONObject();
			this.client = new DefaultHttpClient();
			this.method = method;
			this.url = url;
			this.jsonCallback = callback;
			this.error = false;
		}
		
		@Override
		protected void onPreExecute (){
			if(this.jsonCallback != null){
				this.jsonCallback.beforeCall();
			}
		}
		
		@Override
		protected JSONObject doInBackground(String... args){
			Log.i("REQUEST", "PREPARING with method = " + method+" and url = "+url);
			Log.i("REQUEST",  "MedthodClass : " + methodClasses.get(method).toString());
			try {
				this.request = methodClasses.get(this.method).getConstructor(String.class).newInstance(url);
				Log.i("REQUEST", "email = "+this.jsonParams.getString("email")
						+" pass = "+this.jsonParams.getString("pass"));
				//this.loadJSONParams();
				this.loadJSONParams();
				Log.i("REQUEST", "EXECUTING");
				this.response = client.execute(request);
				Log.i("REQUEST", "READING");
				return JSONHandler.readResponse(this.response.getEntity().getContent());
			}catch (Exception e) {
				e.printStackTrace();
				this.error = true;
				this.errorObject = ErrorHelper.getErrorObject(e);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(JSONObject result){
			if(this.jsonCallback != null){
				this.clearJsonParams();
				if(!this.error){
					this.jsonCallback.call(result);
				}else{
					this.jsonCallback.onError(this.errorObject);
				}
			}
		}
		
		
		protected void loadJSONParams() {
			//if the request is set
			if(this.request!=null){
				//if body request can be set
				if(this.method == "PUT" || this.method == "POST"){
					//set the body as json
					HttpEntityEnclosingRequest entityEnclosingRequest = (HttpEntityEnclosingRequest) this.request;
					try {
						entityEnclosingRequest.setEntity(new StringEntity(this.jsonParams.toString(), "UTf8"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					entityEnclosingRequest.setHeader("Content-type", "application/json");
				//else
				}else{
					//send basic http request
					this.request.setParams(this.httpParams);
				}
			}
		}
		
		public void setParams(Map<String, Object> paramMap){
			this.clearJsonParams();
			if(this.method=="PUT" || this.method=="POST"){
				this.setJSONParams(paramMap);
			}else{
				this.setHttpParams(paramMap);
			}
		}
		
		public void putParam(String key, Object value){
			if(this.method=="PUT" || this.method=="POST"){
				this.putJSONParam(key, value);
			}else{
				this.putHttpParam(key, value);
			}
		}
		
		
		protected void putJSONParam(String key, Object value){
			try {
				this.jsonParams.put(key, value);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		protected void putHttpParam(String key, Object value){
			this.httpParams.setParameter(key, value);
		}
		
		
		protected void setJSONParams(Map<String, Object> paramMap){
			for(String key : paramMap.keySet()){
				this.putJSONParam(key, paramMap.get(key));
			}
		}
		
		
		protected void setJSONParams(JSONObject jsonParams){
			this.jsonParams = jsonParams;
		}
		
		protected void setHttpParams(Map<String, Object> paramMap){
			for(String key : paramMap.keySet()){
				this.putHttpParam(key, paramMap.get(key));
			}
		}
		
		protected void setHttpParams(HttpParams httpParams){
			this.httpParams = (BasicHttpParams) httpParams;
		}
		
		
		protected void clearJsonParams(){
			this.httpParams.clear();
			//thx garbage collector <3 (screw you memory)
			this.httpParams = new BasicHttpParams();
			this.jsonParams = new JSONObject();
		}
}
