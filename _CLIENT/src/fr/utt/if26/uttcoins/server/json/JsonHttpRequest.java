package fr.utt.if26.uttcoins.server.json;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.bson.BSON;
import org.bson.BasicBSONCallback;
import org.bson.BasicBSONObject;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.util.JSON;
import com.mongodb.util.JSONSerializers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import fr.utt.if26.uttcoins.io.BsonHandler;
import fr.utt.if26.uttcoins.io.JSONHandler;
import fr.utt.if26.uttcoins.server.CustomHttpRequest;
import fr.utt.if26.uttcoins.utils.ErrorHelper;
import fr.utt.if26.uttcoins.utils.ServerHelper;

@Deprecated
public class JsonHttpRequest extends AsyncTask<String, Integer, JSONObject> implements CustomHttpRequest{
	//params for non enclosing entity request (GET, DELETE)
		private BasicHttpParams httpParams;
		//params for enclonsing entity request (POST, PUT)
		private JSONObject jsonParams;
		private CustomJSONCallback jsonCallback;
		private String method, url, request_tag;
		private HttpRequestBase request;
		private HttpResponse response;
		private HttpClient client;
		private boolean error;
		private Bundle errorObject;
			
		public JsonHttpRequest(String method, String url, String request_tag, CustomJSONCallback callback){
			this.httpParams = new BasicHttpParams();
			this.jsonParams = new JSONObject();
			this.client = new DefaultHttpClient();
			this.method = method;
			this.url = url;
			this.request_tag = request_tag;
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
			Log.i("REQUEST",  "MedthodClass : " + ServerHelper.REQUEST_MAP.get(method).toString());
			try {
				this.request = ServerHelper.REQUEST_MAP.get(this.method).getConstructor(String.class).newInstance(url);
				this.loadParams();
				//this.loadJSONParams();
				Log.i("REQUEST", "JsonParams = "+this.jsonParams.toString());
				Log.i("REQUEST", "httpParams = "+this.request.getParams());
				Log.i("REQUEST", "EXECUTING");
				this.response = client.execute(request);
				Log.i("REQUEST", "READING");
				return new JSONObject(BsonHandler.readResponse(this.response.getEntity().getContent()).toString());
				//return JSONHandler.readResponse(this.response.getEntity().getContent());
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
				this.clearParams();
				if(!this.error){
					try {
						result.put(ServerHelper.RESQUEST_TAG, this.request_tag);
						this.jsonCallback.call(result);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{
					this.errorObject.putString(ServerHelper.RESQUEST_TAG, this.request_tag);
					this.jsonCallback.onError(this.errorObject);
				}
			}
		}
		
		@Override
		public void loadParams() {
			//if the request is set
			if(this.request!=null){
				//if body request can be set
				if(this.method == ServerHelper.PUT || this.method == ServerHelper.POST){
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
					this.url = this.url + "?" + this.jsonParams.toString();
					try {
						//ne fonctionne pas en JSON (il faut échaper les "{" et "}"
						this.request.setURI(new URI(this.url));
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
					//this.request.setParams(this.httpParams);
				}
			}
		}
		
		@Override
		public void setParams(Map<String, Object> paramMap){
			this.clearParams();
			if(this.method==ServerHelper.PUT || this.method==ServerHelper.POST){
				this.setJSONParams(paramMap);
			}else{
				this.setHttpParams(paramMap);
			}
		}
		
		@Override
		public void putParam(String key, Object value){
			if(this.method==ServerHelper.PUT || this.method==ServerHelper.POST){
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
			Log.i("PARAMS", "{'"+key+"' : '"+value+"'} inserted in httpparams");
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
		
		@Override
		public void clearParams(){
			this.httpParams.clear();
			//thx garbage collector <3 (screw you memory)
			this.httpParams = new BasicHttpParams();
			this.jsonParams = new JSONObject();
		}
}
