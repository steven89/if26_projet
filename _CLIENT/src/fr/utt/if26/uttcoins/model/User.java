package fr.utt.if26.uttcoins.model;

import java.util.Calendar;
import java.util.Date;

import fr.utt.if26.uttcoins.utils.ServerHelper;
import android.os.Bundle;
import android.util.Log;

public class User {

	public static final String EMAIL_PATTERN = "^[a-zA-Z0-9_\\.\\-]*@([a-zA-Z0-9_]*\\.[a-z]{1,3})+$";
	public static final String TAG_PATTERN = "[a-zA-Z0-9_\\.\\-]*";
	public static final String TOKEN_PATTERN = "[a-zA-Z0-9]{30}";
	private static final String NAME_PATTERN = "[a-zA-Z]{2,26}";
	private static User currentUser = null;
	
	private String email, tag, token;
	private int balance;
	private int loginTime;
	
	private User(String email, String tag, String token){
		this.setEmail(email);
		this.setTag(tag);
		this.setToken(token);
		this.setBalance(0);
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
	
	public static Bundle getFormatedData(String name, String firstName, String tag, String email, String password){
		Bundle data = new Bundle();
		if(name.matches(NAME_PATTERN)){
			data.putString(ServerHelper.SERVER_NAME_KEY, name);
		}else{
			//TODO : créer des classes d'exception
			Log.e("USER", "invalid name : "+name);
		}
		if(firstName.matches(NAME_PATTERN)){
			data.putString(ServerHelper.SERVER_FIRST_NAME_KEY, firstName);
		}else{
			Log.e("USER", "invalid first_name : "+name);
		}
		if(tag.matches(TAG_PATTERN)){
			data.putString(ServerHelper.SERVER_TAG_KEY, tag);
		}else{
			Log.e("USER", "invalid tag : "+tag);
		}
		if(email.matches(EMAIL_PATTERN)){
			data.putString(ServerHelper.SERVER_EMAIL_KEY, email);
		}else{
			Log.e("USER", "invalid email : "+email);
		}
		if(password.length() > 0){
			data.putString(ServerHelper.SERVER_PASS_KEY, password);
		}else{
			Log.e("USER", "invalid password : "+password);
		}
		return data;
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
		bundle.putInt(ServerHelper.SERVER_BALANCE_KEY, this.getBalance());
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

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		if(balance >= 0){
			this.balance = balance;
		}
	}
	
	public static void setAccountBalance(int balance){
		if(balance >= 0){
			currentUser.setBalance(balance);
		}
	}
}
