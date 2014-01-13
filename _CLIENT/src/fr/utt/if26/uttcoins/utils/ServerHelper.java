package fr.utt.if26.uttcoins.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.bson.BasicBSONCallback;

import fr.utt.if26.uttcoins.model.User;
import fr.utt.if26.uttcoins.server.CustomHttpRequest;
import fr.utt.if26.uttcoins.server.CustomHttpRequestCallback;
import fr.utt.if26.uttcoins.server.bson.BasicBSONHttpRequest;
import fr.utt.if26.uttcoins.server.bson.CustomBasicBSONCallback;
import fr.utt.if26.uttcoins.server.json.CustomJSONCallback;
import fr.utt.if26.uttcoins.server.json.JsonHttpRequest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class ServerHelper {
	
	private static final String SERVER_URL = "http://10.0.2.2:8080/_SERVEUR"; //"http://88.186.76.236/_SERVEUR";
	private static final String TRANSACTION_URL = SERVER_URL + "/Transaction";
	private static final String USER_WALLET_URL = SERVER_URL + "/UserWallet";
	private static final String LOGOUT_URL = SERVER_URL + "/Logout";
	private static final String LOGIN_URL = SERVER_URL + "/Login";
	public static final String BSON_REQUEST = "BSON";
	public static final String JSON_REQUEST = "JSON";
	public static final String SERVER_TOKEN_KEY = "token";
	public static final String SERVER_EMAIL_KEY = "email";
	public static final String SERVER_PASS_KEY = "pass";
	public static final String SERVER_TAG_KEY = "tag";
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String PUT = "PUT";
	public static final String DELETE = "DELETE";
	public static final String RESQUEST_TAG = "request_tag";
	public static final String GET_TRANSACTION_TAG = "get_transaction_tag";
	public static final String POST_TRANSACTION_TAG = "post_transaction_tag";
	public static final String LOGIN_TAG = "login_tag";
	public static final String LOGOUT_TAG = "logout_tag";
	public static final String GET_WALLET_TAG = "get_wallet_tag";
	
	//TODO encapsuler les params de l'utilisateur dans un singleton user
	private static User user = null;
	
	// wildcard utilisé : map vérouillée en lecture seule
	public static HashMap<String, Class<? extends HttpRequestBase>> REQUEST_MAP; 
	
	static{
		if(REQUEST_MAP == null){
			REQUEST_MAP = new HashMap<String, Class<? extends HttpRequestBase>>();
			REQUEST_MAP.put(ServerHelper.GET, HttpGet.class);
			REQUEST_MAP.put(ServerHelper.POST, HttpPost.class);
			REQUEST_MAP.put(ServerHelper.PUT, HttpPut.class);
			REQUEST_MAP.put(ServerHelper.DELETE, HttpDelete.class);
		}
	}
	
	public static void logUser(String email, String password, String request_tag, CustomHttpRequestCallback callback){
		if(request_tag == BSON_REQUEST){
			BasicBSONHttpRequest request = 
					new BasicBSONHttpRequest(PUT, LOGIN_URL, LOGIN_TAG, (CustomBasicBSONCallback) callback);
			request.putParam(SERVER_EMAIL_KEY, email);
			request.putParam(SERVER_PASS_KEY, password);
			request.execute();
		}
		if(request_tag == JSON_REQUEST){
			JsonHttpRequest request = 
					new JsonHttpRequest(PUT, LOGIN_URL, LOGIN_TAG,(CustomJSONCallback) callback);
			request.putParam(SERVER_EMAIL_KEY, email);
			request.putParam(SERVER_PASS_KEY, password);
			request.execute();
		}	
	}
	
	public static void logout(String request_tag, CustomHttpRequestCallback callback) {
		Log.i("USER", "USER SENDING LOGOUT");
		if(request_tag == BSON_REQUEST){
			BasicBSONHttpRequest request = 
					new BasicBSONHttpRequest(PUT, LOGOUT_URL, LOGOUT_TAG, (CustomBasicBSONCallback) callback);
			request.putParam(SERVER_TOKEN_KEY, user.getToken());
			request.putParam(SERVER_EMAIL_KEY, user.getEmail());
			request.execute();
		}
		if(request_tag == JSON_REQUEST){
			JsonHttpRequest request = 
					new JsonHttpRequest(PUT, LOGOUT_URL, LOGOUT_TAG, (CustomJSONCallback) callback);
			request.putParam(SERVER_TOKEN_KEY, user.getToken());
			request.putParam(SERVER_EMAIL_KEY, user.getEmail());
			request.execute();
		}
	}
	
	public static void getUserTransactions(String request_tag, CustomHttpRequestCallback callback){
		//TODO améliorer ça avec un combo héritage/générécité entre AsyncTask et les classes custom qui en hérite dans l'application
		if(request_tag == BSON_REQUEST){
			BasicBSONHttpRequest request = 
					new BasicBSONHttpRequest(GET, TRANSACTION_URL, GET_TRANSACTION_TAG, (CustomBasicBSONCallback) callback);
			request.putParam(SERVER_TOKEN_KEY, user.getToken());
			request.putParam(SERVER_EMAIL_KEY, user.getEmail());
			request.execute();
		}
		if(request_tag == JSON_REQUEST){
			JsonHttpRequest request = 
					new JsonHttpRequest(GET, TRANSACTION_URL, GET_TRANSACTION_TAG, (CustomJSONCallback) callback);
			request.putParam(SERVER_TOKEN_KEY, user.getToken());
			request.putParam(SERVER_EMAIL_KEY, user.getEmail());
			request.execute();
		}		
	}
	
	public static void getUserSolde(String request_tag, CustomHttpRequestCallback callback){
		//TODO améliorer ça avec un combo héritage/générécité entre AsyncTask et les classes custom qui en hérite dans l'application
		if(request_tag == BSON_REQUEST){
			BasicBSONHttpRequest request = 
					new BasicBSONHttpRequest(GET, USER_WALLET_URL, GET_WALLET_TAG, (CustomBasicBSONCallback) callback);
			request.putParam(SERVER_TOKEN_KEY, user.getToken());
			request.putParam(SERVER_EMAIL_KEY, user.getEmail());
			request.execute();
		}
		if(request_tag == JSON_REQUEST){
			JsonHttpRequest request = 
					new JsonHttpRequest(GET, USER_WALLET_URL, GET_WALLET_TAG, (CustomJSONCallback) callback);
			request.putParam(SERVER_TOKEN_KEY, user.getToken());
			request.putParam(SERVER_EMAIL_KEY, user.getEmail());
			request.execute();
		}	
	}
	
	public static void startSession(Bundle session) {
		if(session.containsKey(SERVER_EMAIL_KEY)
				&& session.containsKey(SERVER_TAG_KEY)
				&& session.containsKey(SERVER_TOKEN_KEY)){
			user = User.newSession(session.getString(SERVER_EMAIL_KEY),
					session.getString(SERVER_TAG_KEY),
					session.getString(SERVER_TOKEN_KEY));
		}
	}

	public static Bundle getSession() {		
		return user.getCurrentSession();
	}
}
