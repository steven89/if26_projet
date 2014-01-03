package fr.utt.if26.uttcoins.utils;

import android.util.Log;

public class UserHelper {
	private static String user_token;
	
	public static void setSession(String token){
		user_token = token;
	}

	public static String getToken() {
		// TODO Auto-generated method stub
		return user_token;
	}

	public static void logout() {
		Log.i("USER", "USER DISCONNECTED");
		
	}
}
