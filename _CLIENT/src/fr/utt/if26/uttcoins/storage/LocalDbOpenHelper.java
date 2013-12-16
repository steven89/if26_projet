package fr.utt.if26.uttcoins.storage;
import java.util.HashMap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;


public class LocalDbOpenHelper extends SQLiteOpenHelper {
	
	private static final int DB_VERSION = 1;
	
	private static final String baseName = "app_datas.db";
	
	/**
	 * 
	 * "table" : 	[0][0] = nom_champ_1
	 * 				[0][1] = attr_champ_1 ex : "INTEGER PRIMARY KEY AUTOINCREMENT"
	 * 				[1][0] = nom_champ_2
	 * 				[1][1] = attr_champ_2 ex : "TEXT NOT NULL"
	 */
	private static HashMap<String, String[][]> tables;
	
	
	public LocalDbOpenHelper(Context context) {
		super(context, baseName, null, DB_VERSION);
		tables.put("app_datas", new String[][] {
				{"id","INTEGER PRIMARY KEY AUTOINCREMENT"},
				{"field1","TEXT NOT NULL"},
				{"field2","TEXT NOT NULL"}
			}
		);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String request = "";
		int i = 0;
		for(String table : tables.keySet()){
			request = "CREATE TABLE "+table+" (";
			i=0;
			for(String[] champ : tables.get(table)){
				request += (i==0)?" ":", ";
				request += champ[0]+" "+champ[1];
				i++;
			}
			request += ");";
			db.execSQL(request);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}

}
