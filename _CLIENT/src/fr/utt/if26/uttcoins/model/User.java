package fr.utt.if26.uttcoins.model;

import java.util.Calendar;
import java.util.Date;

import fr.utt.if26.uttcoins.utils.ServerHelper;
import android.os.Bundle;
import android.util.Log;

public class User {

	private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_\\.\\-]*@([a-zA-Z0-9_]*\\.[a-z]{1,3})+$";
	private static final String TAG_PATTERN = "[a-zA-Z0-9_\\.\\-]*";
	private static final String TOKEN_PATTERN = "[a-zA-Z0-9]{30}";
	private static User currentUser = null;
	
	private String email, tag, token;
	private int loginTime;
	
	private User(String email, String tag, String token){
		this.setEmail(email);
		this.setTag(tag);
		this.setToken(token);
		this.loginTime = (int) (new Date().getTime() / 1000);
	}
	
	public static User newSession(String email, String tag, String token){
		if(currentUser != null){
			currentUser.setEmail(email);
			currentUser.setEmail(email);
			currentUser.setToken(token);
		}else{
			currentUser = new User(email, tag, token);
		}
		return currentUser;
	}
	
	public Bundle getCurrentSession(){
		int currentTime = (int) (new Date().getTime() / 1000);
		Bundle session = null;
		if(currentUser != null){
			Log.i("USER", "currentUser = " + currentUser.toBundle().toString());
			if(currentTime - currentUser.getLoginTime() < 1800){
				currentUser.setLoginTime(currentTime);
				session = currentUser.toBundle();
			}else{
				Log.i("USER", "user session timedOut : delta time = "+Integer.toString(currentTime - currentUser.getLoginTime()));
			}
		}else{
			
		}
		return session;
	}
	
	public Bundle toBundle(){
		Bundle bundle = new Bundle();
		bundle.putString(ServerHelper.SERVER_EMAIL_KEY, this.getEmail());
		bundle.putString(ServerHelper.SERVER_TAG_KEY, this.getTag());
		bundle.putString(ServerHelper.SERVER_TOKEN_KEY, this.getToken());
		return bundle;
	}

	public int getLoginTime() {
		return this.loginTime;
	}
	
	public void setLoginTime(int time){
		//timeStamp en secondes
		this.loginTime = time;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		if(email.matches(EMAIL_PATTERN)){
			this.email = email;
		}else{
			Log.e("ERROR", "email ("+email+") not matching pattern");
		}
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		if(tag.matches(TAG_PATTERN)){
			this.tag = tag;
		}else{
			Log.e("ERROR", "tag ("+tag+") not matching pattern");
		}
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		if(token.matches(TOKEN_PATTERN)){
			this.token = token;
		}else{
			Log.e("ERROR", "token ("+token+") not matching pattern");
		}
	}
}
