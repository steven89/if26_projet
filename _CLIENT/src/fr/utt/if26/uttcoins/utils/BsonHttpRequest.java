package fr.utt.if26.uttcoins.utils;

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
import org.bson.BSONDecoder;
import org.bson.BSONObject;
import org.bson.BasicBSONDecoder;
import org.bson.BasicBSONObject;
import org.json.JSONException;
import org.json.JSONObject;

import fr.utt.if26.uttcoins.error.CustomServerException;
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
	private JSONObject jsonParams;
	private BsonCallback bsonCallback;
	private String method, url;
	private HttpRequestBase request;
	private HttpResponse response;
	private HttpClient client;
	private boolean error;
	private Bundle errorObject;
	
	// wildcard utilis� : map v�rouill�e en lecture seule
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
		this.jsonParams = new JSONObject();
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
			Log.i("REQUEST", "email = "+(String)this.jsonParams.get("email")
					+" pass = "+(String)this.jsonParams.get("pass"));
			this.loadParams();
			Log.i("REQUEST", "EXECUTING");
			this.response = client.execute(request);
			Log.i("REQUEST", "READING");
	        //return this.readResponse(this.response.getEntity().getContent());		
			return this.readBSONResponse(this.response.getEntity().getContent());
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
			this.clearParams();
			if(!this.error){
				this.bsonCallback.call(result);
			}else{
				this.bsonCallback.onError(this.errorObject);
			}
		}
	}
	
	protected void loadParams() {
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
		this.clearParams();
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
	
	protected void setHttpParams(Map<String, Object> paramMap){
		for(String key : paramMap.keySet()){
			this.putHttpParam(key, paramMap.get(key));
		}
	}
	
	protected void setJSONParams(JSONObject jsonParams){
		this.jsonParams = jsonParams;
	}
	
	protected void setHttpParams(HttpParams httpParams){
		this.httpParams = (BasicHttpParams) httpParams;
	}
	
	protected void clearParams(){
		this.httpParams.clear();
		//thx garbage collector <3 (screw you memory)
		this.httpParams = new BasicHttpParams();
		//just to be sure ...
		if(this.jsonParams==null)
			this.jsonParams = new JSONObject();
	}
	
	@Deprecated
	protected JSONObject readResponse(InputStream stream) 
			throws CustomServerException, JSONException, IOException{
		String lineRead = "";
		StringBuilder stringResponse = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		JSONObject JsonResponse;
		try {
			while((lineRead = reader.readLine())!=null){
				stringResponse.append(lineRead);
			}
		} catch (IOException e) {
			throw e;
		}
		Log.i("STRING RESPONSE", stringResponse.toString());
		JsonResponse = this.JSONParse(stringResponse.toString());
		if(JsonResponse.has("error")){
			throw ErrorHelper.getCustomServerException(JsonResponse.getString("error"));
		}
		return JsonResponse;
	}
	
	protected BSONObject readBSONResponse(InputStream stream) 
			throws CustomServerException, JSONException, IOException{
		BSONObject bsonResponse;
		String lineRead = "";
		StringBuilder stringResponse = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		try {
			while((lineRead = reader.readLine())!=null){
				stringResponse.append(lineRead);
			}
		} catch (IOException e) {
			throw e;
		}
		bsonResponse = this.decodeBSONResponse(stringResponse.toString());
		if(bsonResponse.containsField("error")){
			throw ErrorHelper.getCustomServerException((String) bsonResponse.get("error"));
		}
		return bsonResponse;
	}
	
	protected BSONObject decodeBSONResponse(String encodedResponse){
		BSONObject bsonResponse;
		String[] chunks = encodedResponse.split("[a-zA-Z]");
		byte[] decodedByteResponse = new byte[chunks.length];
		for(int i = 0; i < chunks.length; i++){
			decodedByteResponse[i] = Byte.valueOf(chunks[i]);
		}
		BSONDecoder decoder = new BasicBSONDecoder();
		bsonResponse = (BasicBSONObject) decoder.readObject(decodedByteResponse);
		return bsonResponse;
	}
	
	protected JSONObject JSONParse(String jsonString){
		JSONObject jsonResponse = new JSONObject();
		try {
			jsonResponse = new JSONObject(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonResponse;
	}
}