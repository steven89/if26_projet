package fr.utt.if26.uttcoins.utils;

import java.util.HashMap;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.bson.BasicBSONObject;

import fr.utt.if26.uttcoins.error.CustomIllegalParametter;
import fr.utt.if26.uttcoins.model.Transaction;
import fr.utt.if26.uttcoins.model.TransactionList;
import fr.utt.if26.uttcoins.model.User;
import fr.utt.if26.uttcoins.server.bson.BasicBSONHttpRequest;
import fr.utt.if26.uttcoins.server.bson.CustomBasicBSONCallback;
import android.os.Bundle;
import android.util.Log;

public class ServerHelper implements CustomBasicBSONCallback{
	
	private static final String SERVER_URL = "http://88.186.76.236/_SERVEUR"; //"http://10.0.2.2:8080/_SERVEUR";
	private static final String TRANSACTION_URL = SERVER_URL + "/Transaction";
	private static final String USER_WALLET_URL = SERVER_URL + "/UserWallet";
	private static final String LOGOUT_URL = SERVER_URL + "/Logout";
	private static final String LOGIN_URL = SERVER_URL + "/Login";
	private static final String SIGN_UP_URL = SERVER_URL + "/User";
	public static final String BSON_REQUEST = "BSON";
	@Deprecated
	public static final String JSON_REQUEST = "JSON";
	public static final String SERVER_ID_KEY = "_id";
	public static final String SERVER_TOKEN_KEY = "token";
	public static final String SERVER_EMAIL_KEY = "email";
	public static final String SERVER_PASS_KEY = "pass";
	public static final String SERVER_TAG_KEY = "tag";
	public static final String SERVER_BALANCE_KEY = "balance";
	public static final String SERVER_TRANSACTIONS_KEY = "transactions";
	public static final String SERVER_RECEIVER_KEY = "to";
	public static final String SERVER_TRANSACTION_RECEIVER_TAG = "to";
	public static final String SERVER_SENDER_KEY = "from";
	public static final String SERVEUR_TRANSACTION_AMOUNT_TAG = "amount";
	public static final String SERVER_NAME_KEY = "nom";
	public static final String SERVER_FIRST_NAME_KEY = "prenom";
	public static final String SERVER_TRANSACTION_AMOUNT_KEY = "amount";

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
	public static final String SIGN_UP_TAG = "sign_up";
	public static final String SYSTEM_USER_TAG = "admin@system";
	
	//TODO encapsuler les params de l'utilisateur dans un singleton user
	private static User user = null;
	public static CustomBasicBSONCallback server_bson_request_callback = getBSONRequestListener();
	
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
	
	public static void logUser(String email, String password, CustomBasicBSONCallback callback){
			BasicBSONHttpRequest request = 
					new BasicBSONHttpRequest(PUT, LOGIN_URL, LOGIN_TAG, (CustomBasicBSONCallback) callback);
			request.putParam(SERVER_EMAIL_KEY, email);
			request.putParam(SERVER_PASS_KEY, password);
			request.execute();
// 		Historiquement : support de requetes JSON
//		if(request_tag == JSON_REQUEST){
//			JsonHttpRequest request = 
//					new JsonHttpRequest(PUT, LOGIN_URL, LOGIN_TAG,(CustomJSONCallback) callback);
//			request.putParam(SERVER_EMAIL_KEY, email);
//			request.putParam(SERVER_PASS_KEY, password);
//			request.execute();
//		}	
	}
	
	public static void logout(CustomBasicBSONCallback callback) {
		//Log.i("USER", "USER SENDING LOGOUT");
		BasicBSONHttpRequest request = 
				new BasicBSONHttpRequest(PUT, LOGOUT_URL, LOGOUT_TAG, (CustomBasicBSONCallback) callback);
		request.putParam(SERVER_TOKEN_KEY, user.getToken());
		request.putParam(SERVER_EMAIL_KEY, user.getEmail());
		request.execute();
		TransactionList.clear();
		user = null;
	}
	
	public static void getUserTransactions(){
		getUserTransactions(server_bson_request_callback);
	}
	
	public static void getUserTransactions(CustomBasicBSONCallback callback){
			BasicBSONHttpRequest request = 
					new BasicBSONHttpRequest(GET, TRANSACTION_URL, GET_TRANSACTION_TAG, (CustomBasicBSONCallback) callback);
			Log.i("SERVER_HELPER", callback.getClass().getCanonicalName());
			request.putParam(SERVER_TOKEN_KEY, user.getToken());
			request.putParam(SERVER_EMAIL_KEY, user.getEmail());
			request.execute();
	}
	
	public static void postNewTransactions(Transaction newTransaction){
		postNewTransactions(newTransaction, server_bson_request_callback);
	}
	public static void postNewTransactions(Transaction newTransaction, CustomBasicBSONCallback callback){
		if(newTransaction.getAmount() > 0 && newTransaction.getReceiver() != null){
			BasicBSONHttpRequest request = 
					new BasicBSONHttpRequest(POST, TRANSACTION_URL, POST_TRANSACTION_TAG, (CustomBasicBSONCallback) callback);
			request.putParam(SERVER_TOKEN_KEY, user.getToken());
			request.putParam(SERVER_EMAIL_KEY, user.getEmail());
			Log.i("REQUEST", "posting new transaction : "+newTransaction.toString());
			request.putParam(SERVEUR_TRANSACTION_AMOUNT_TAG, newTransaction.getAmount());
			request.putParam(SERVER_TRANSACTION_RECEIVER_TAG, newTransaction.getReceiver());
			request.execute();
		}else{
			callback.onError(ErrorHelper.getErrorObject(new CustomIllegalParametter()));
		}
	}
	
	
	public static void getUserSolde(){
		getUserSolde(server_bson_request_callback);
	}
	public static void getUserSolde(CustomBasicBSONCallback callback){
		BasicBSONHttpRequest request = 
				new BasicBSONHttpRequest(GET, USER_WALLET_URL, GET_WALLET_TAG, (CustomBasicBSONCallback) callback);
		//Log.i("REQUEST", "get balance with params = "+user.toBundle().toString());
		request.putParam(SERVER_TOKEN_KEY, user.getToken());
		request.putParam(SERVER_EMAIL_KEY, user.getEmail());
		if(callback!=server_bson_request_callback){
			request.addCallback(server_bson_request_callback);
		}
		request.execute();
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
		if(user !=null){
			return user.getCurrentSession();
		}else{
			return null;
		}
	}

	public static void signUp(Bundle userData) {
		signUp(userData, server_bson_request_callback);
	}
	public static void signUp(Bundle userData, CustomBasicBSONCallback callback) {
		BasicBSONHttpRequest request = 
				new BasicBSONHttpRequest(POST, SIGN_UP_URL, SIGN_UP_TAG, (CustomBasicBSONCallback) callback);
		//Log.i("REQUEST", "sign up balance with params = "+userData.toString());
		//{"email", "pass", "prenom", "nom", "tag"}
		request.putParam(SERVER_EMAIL_KEY, userData.getString(SERVER_EMAIL_KEY));
		request.putParam(SERVER_PASS_KEY, userData.getString(SERVER_PASS_KEY));
		request.putParam(SERVER_FIRST_NAME_KEY, userData.getString(SERVER_FIRST_NAME_KEY));
		request.putParam(SERVER_NAME_KEY, userData.getString(SERVER_NAME_KEY));
		request.putParam(SERVER_TAG_KEY, userData.getString(SERVER_TAG_KEY));
		request.execute();
	}

	public static String TRANSACTION_AMOUNT_KEY = "transaction_amount";
	public static String TRANSACTION_RECEIVER_KEY = "transaction_receiver";
	
	private ServerHelper(){
	}

	public static CustomBasicBSONCallback getBSONRequestListener(){
		return (CustomBasicBSONCallback)  new ServerHelper();
	}
	
	@Override
	public Object call(BasicBSONObject bsonResponse) {
		if(bsonResponse.getString(RESQUEST_TAG) == POST_TRANSACTION_TAG){
			User.setAccountBalance(Integer.parseInt(bsonResponse.getString(ServerHelper.SERVER_BALANCE_KEY)));
		}
		if(bsonResponse.getString(RESQUEST_TAG) == GET_WALLET_TAG){
			User.setAccountBalance(Integer.parseInt(bsonResponse.getString(ServerHelper.SERVER_BALANCE_KEY)));
		}
		if(bsonResponse.getString(RESQUEST_TAG) == GET_TRANSACTION_TAG){
		}
		return null;
	}

	@Override
	public void onError(Bundle errorObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeCall() {
		// TODO Auto-generated method stub
		
	}
}
