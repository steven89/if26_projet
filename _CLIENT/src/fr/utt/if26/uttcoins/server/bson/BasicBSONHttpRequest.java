package fr.utt.if26.uttcoins.server.bson;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.bson.BasicBSONCallback;
import org.bson.BasicBSONObject;

import fr.utt.if26.uttcoins.io.BsonHandler;
import fr.utt.if26.uttcoins.server.CustomHttpRequest;
import fr.utt.if26.uttcoins.utils.ErrorHelper;
import fr.utt.if26.uttcoins.utils.ServerHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class BasicBSONHttpRequest extends AsyncTask<String, Integer, BasicBSONObject>
	implements CustomHttpRequest{

	//params for non enclosing entity request (GET, DELETE)
	@Deprecated
	private BasicHttpParams httpParams;
	//params for enclonsing entity request (POST, PUT)
	private BasicBSONObject bsonParams;
	private ArrayList<CustomBasicBSONCallback> bsonCallbacks;
	private String method, url, request_tag;
	private HttpRequestBase request;
	private HttpResponse response;
	private HttpClient client;
	private boolean error;
	private Bundle errorObject;
	
	public BasicBSONHttpRequest(String method, String url, String request_tag, CustomBasicBSONCallback callback){
		this.httpParams = new BasicHttpParams();
		this.bsonParams = new BasicBSONObject();
		this.client = new DefaultHttpClient();
		this.method = method;
		this.url = url;
		this.request_tag = request_tag;
		this.bsonCallbacks = new ArrayList<CustomBasicBSONCallback>();
		this.bsonCallbacks.add(callback);
		this.error = false;
	}
	
	@Override
	protected void onPreExecute (){
		if(this.bsonCallbacks != null){
			for(CustomBasicBSONCallback callback : this.bsonCallbacks){
				callback.beforeCall();
			}
		}
	}
	
	@Override
	protected BasicBSONObject doInBackground(String... args){
		//Log.i("REQUEST", "PREPARING with method = " + method+" and url = "+url);
		//Log.i("REQUEST",  "MedthodClass : " + ServerHelper.REQUEST_MAP.get(method).toString());
		try {
			this.request = ServerHelper.REQUEST_MAP.get(this.method).getConstructor(String.class).newInstance(url);
			this.loadParams();
			//Log.i("REQUEST", "bsonParams = "+this.bsonParams.toString());
			//Log.i("REQUEST", "httpParams = "+this.request.getParams());
			//Log.i("REQUEST", "EXECUTING");
			this.response = client.execute(request);
			//Log.i("REQUEST", "READING");
			return BsonHandler.readResponse(this.response.getEntity().getContent());
		}catch (Exception e) {
			e.printStackTrace();
			this.error = true;
			this.errorObject = ErrorHelper.getErrorObject(e);
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(BasicBSONObject result){
		if(this.bsonCallbacks != null){
			this.clearParams();
			if(!this.error){
				result.put(ServerHelper.RESQUEST_TAG, this.request_tag);
				for(CustomBasicBSONCallback callback : this.bsonCallbacks){
					callback.call(result);
				}
			}else{
				this.errorObject.putString(ServerHelper.RESQUEST_TAG, this.request_tag);
				for(CustomBasicBSONCallback callback : this.bsonCallbacks){
					callback.onError(this.errorObject);
				}
			}
		}
	}
	
	@Override
	public void loadParams() throws UnsupportedEncodingException{
		//if the request is set
		if(this.request!=null){
			//if body request can be set
			if(this.method==ServerHelper.POST || this.method==ServerHelper.PUT){
				//set the body as bson
				HttpEntityEnclosingRequest entityEnclosingRequest = (HttpEntityEnclosingRequest) this.request;
				try {
					String stringBson = BsonHandler.encodeRequestBody(this.bsonParams);
					//Log.i("REQEST", "BSON body = "+stringBson);
					entityEnclosingRequest.setEntity(new StringEntity(stringBson, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					throw e;
				}
				entityEnclosingRequest.setHeader("Content-type", "application/bson");
			//else
			}else{
				//send basic http request
				//this.request.setParams(this.httpParams);
				//Log.i("REQUEST", "params = "+this.bsonParams.toString());
				this.url = this.url + "?" + BsonHandler.encodeRequestBody(this.bsonParams);
				//Log.i("REQUEST", "url set : "+this.url);
				try {
					this.request.setURI(new URI(this.url));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void setParams(Map<String, Object> paramMap){
		this.clearParams();
		this.setBSONParams(paramMap);
	}
	
	@Override
	public void putParam(String key, Object value){
		this.putBSONParam(key, value);
	}
	
	@Override
	public void clearParams(){
		this.httpParams.clear();
		//thx garbage collector <3 (screw you memory)
		this.httpParams = new BasicHttpParams();
		this.bsonParams = new BasicBSONObject();
	}
		
	protected void putBSONParam(String key, Object value){
			this.bsonParams.put(key, value);
	}

	@Deprecated
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
	
	@Deprecated
	protected void setHttpParams(Map<String, Object> paramMap){
		for(String key : paramMap.keySet()){
			this.putHttpParam(key, paramMap.get(key));
		}
	}
	
	@Deprecated
	protected void setHttpParams(HttpParams httpParams){
		this.httpParams = (BasicHttpParams) httpParams;
	}
	
	public void addCallback(CustomBasicBSONCallback callback){
		this.bsonCallbacks.add(callback);
	}
}
