package fr.utt.if26.uttcoins.storage;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

public class LocalStorage {
	
	private LocalDbHelper dbHelper;
	
	public LocalStorage(Context context){
		this.dbHelper = new LocalDbHelper(context);
	}
	
	public void setLoginInfos(String email, String token){
		HashMap<String, String> line = new HashMap<String, String>();
		line = dbHelper.getLine(LocalDbHelper.LOGIN_INFOS, null);
		ArrayList<String> datas = new ArrayList<String>();
		datas.add(email);
		datas.add(token);
		if(line!=null){
			dbHelper.updateLine(LocalDbHelper.LOGIN_INFOS, datas);
		}
		else{
			dbHelper.insertLine(datas);
		}
	}
	
	public HashMap<String, String> getLoginInfos(){
		return dbHelper.getLine(LocalDbHelper.LOGIN_INFOS, new String[] {"email","token"});
	}
}
