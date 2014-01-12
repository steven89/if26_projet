package fr.utt.if26.uttcoins.utils;

import java.util.Date;
import java.util.HashMap;

import android.os.AsyncTask;
import android.util.Log;

public class UserHelper {
	
	//TODO encapsuler les params de l'utilisateur dans un singleton user
	private static String user_token;
	private static String user_email;
	private static Date sessionStartAt;
	
	private static HashMap<Class<?>, Class<? extends AsyncTask>> REQUEST_MAP;
	
	
	public static void setSession(String token, String email){
		user_token = token;
		user_email = email;
		sessionStartAt = new Date();
	}
	
	public static <RequestCallbackType> void getUserTransactions(RequestCallbackType callback){
		//REQUEST_MAP.get();
	}

	public static String getToken() {
		// TODO Auto-generated method stub
		return user_token;
	}

	public static void logout() {
		Log.i("USER", "USER DISCONNECTED");
		
	}

	public static int getAccountBalance() {
		// TODO Auto-generated method stub
		return 100;
	}
}
