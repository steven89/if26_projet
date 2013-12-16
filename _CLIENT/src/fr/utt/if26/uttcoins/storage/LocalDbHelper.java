package fr.utt.if26.uttcoins.storage;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LocalDbHelper {
	
	private LocalDbOpenHelper dbHelper;
	private SQLiteDatabase db;
	
	public final static int LOGIN_INFOS = 0;
	public final static int EMAIL = 0;
	public final static int TOKEN = 1;
	
	public LocalDbHelper(Context context){
		dbHelper = new LocalDbOpenHelper(context);
	}
	
	public void open(){
		db = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		db.close();
	}
	/**
	 * 
	 * @param lineId : identifiant du tuple ex: {@link LocalDbHelper}{@link #LOGIN_INFOS}
	 * @param colNames : nom des colonnes recupérées ex: {"email","token"}
	 * @return
	 */
	public HashMap<String, String> getLine(int lineId, String[] colNames){
		Cursor c = db.query("app_datas", new String[] {"id", "field1", "field2"}, 
							"id = ?", new String[] {Integer.toString(lineId)}, null, null, null);
		return cursorToHash(c, colNames);
	}
	
	public void updateLine(int lineId, ArrayList<String> datas){
		ContentValues values = new ContentValues();
		values.put("field1", datas.get(0));
		values.put("field2", datas.get(1));
		db.update("apps_datas", values, "id = ?", new String[] {Integer.toString(lineId)});
	}
	
	public void insertLine(ArrayList<String> datas){
		ContentValues values = new ContentValues();
		values.put("field1", datas.get(0));
		values.put("field2", datas.get(1));
		db.insert("app_datas", null, values);
	}
	
	public HashMap<String, String> cursorToHash(Cursor c, String[] colNames){
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		if(colNames==null || colNames.length!=2){
			colNames = new String[2];
			colNames[0] = "field1";
			colNames[1] = "field2";
		}
		HashMap<String, String> map = new HashMap<String, String>();
		//map.put("id", Integer.toString(c.getInt(0)));
		map.put(colNames[0], c.getString(1));
		map.put(colNames[1], c.getString(2));
		return map;
	}
}
