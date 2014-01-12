package fr.utt.if26.uttcoins.server.bson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
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
import org.bson.BSON;
import org.bson.BSONDecoder;
import org.bson.BSONObject;
import org.bson.BasicBSONDecoder;
import org.bson.BasicBSONEncoder;
import org.bson.BasicBSONObject;
import org.json.JSONException;
import org.json.JSONObject;

import fr.utt.if26.uttcoins.error.CustomServerException;
import fr.utt.if26.uttcoins.io.BsonHandler;
import fr.utt.if26.uttcoins.utils.ErrorHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class BsonHttpRequest extends AsyncTask<String, Integer, BSONObject> {

	//params for non enclosing entity request (GET, DELETE)
	private BasicHttpParams httpParams;
	//params for enclonsing entity request (POST, PUT)
	private BasicBSONObject bsonParams;
	private BsonCallback bsonCallback;
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
	
	public BsonHttpRequest(String method, String url, BsonCallback callback){
		this.httpParams = new BasicHttpParams();
		this.bsonParams = new BasicBSONObject();
		this.client = new DefaultHttpClient();
		this.method = method;
		this.url = url;
		this.bsonCallback = callback;
		this.error = false;
	}
	
	@Override
	protected void onPreExecute (){
		if(this.bsonCallback != null){
			this.bsonCallback.beforeCall();
		}
	}
	
	@Override
	protected BSONObject doInBackground(String... args){
		Log.i("REQUEST", "PREPARING with method = " + method+" and url = "+url);
		Log.i("REQUEST",  "MedthodClass : " + methodClasses.get(method).toString());
		try {
			this.request = methodClasses.get(this.method).getConstructor(String.class).newInstance(url);
			Log.i("REQUEST", "email = "+this.bsonParams.getString("email")
					+" pass = "+this.bsonParams.getString("pass"));
			//this.loadJSONParams();
			this.loadBSONParams();
			Log.i("REQUEST", "EXECUTING");
			this.response = client.execute(request);
			Log.i("REQUEST", "READING");
			return BsonHandler.readBSONResponse(this.response.getEntity().getContent());
		}catch (Exception e) {
			e.printStackTrace();
			this.error = true;
			this.errorObject = ErrorHelper.getErrorObject(e);
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(BSONObject result){
		if(this.bsonCallback != null){
			this.clearBsonParams();
			if(!this.error){
				this.bsonCallback.call(result);
			}else{
				this.bsonCallback.onError(this.errorObject);
			}
		}
	}
	
	protected void loadBSONParams() throws UnsupportedEncodingException{
		//if the request is set
		if(this.request!=null){
			//if body request can be set
			if(this.method == "PUT" || this.method == "POST"){
				//set the body as json
				HttpEntityEnclosingRequest entityEnclosingRequest = (HttpEntityEnclosingRequest) this.request;
				try {
					entityEnclosingRequest.setEntity(new StringEntity(BsonHandler.encodeBSONObjectToString(this.bsonParams), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					throw e;
				}
				entityEnclosingRequest.setHeader("Content-type", "application/bson");
			//else
			}else{
				//send basic http request
				this.request.setParams(this.httpParams);
			}
		}
	}

	public void setParams(Map<String, Object> paramMap){
		this.clearBsonParams();
		if(this.method=="PUT" || this.method=="POST"){
			this.setBSONParams(paramMap);
		}else{
			this.setHttpParams(paramMap);
		}
	}
	
	public void putParam(String key, Object value){
		if(this.method=="PUT" || this.method=="POST"){
			this.putBSONParam(key, value);
		}else{
			this.putHttpParam(key, value);
		}
	}
		
	protected void putBSONParam(String key, Object value){
			this.bsonParams.put(key, value);
	}

	protected void putHttpParam(String key, Object value){
		this.httpParams.setParameter(key, value);
	}
	
	protected void setBSONParams(Map<String, Object> paramMap){
		for(String key : paramMap.keySet()){
			this.putBSONParam(key, paramMap.get(key));
		}
	}
	
	protected void setBSONParams(BasicBSONObject bsonParams){
		this.bsonParams = bsonParams;
	}
	
	protected void setHttpParams(Map<String, Object> paramMap){
		for(String key : paramMap.keySet()){
			this.putHttpParam(key, paramMap.get(key));
		}
	}
	
	protected void setHttpParams(HttpParams httpParams){
		this.httpParams = (BasicHttpParams) httpParams;
	}
		
	protected void clearBsonParams(){
		this.httpParams.clear();
		//thx garbage collector <3 (screw you memory)
		this.httpParams = new BasicHttpParams();
		this.bsonParams = new BasicBSONObject();
	}
}
