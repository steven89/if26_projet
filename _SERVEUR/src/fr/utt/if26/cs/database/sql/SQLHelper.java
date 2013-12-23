package fr.utt.if26.cs.database.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.bson.BSONObject;

import fr.utt.if26.cs.database.DatabaseHelper;

public class SQLHelper implements DatabaseHelper {
	
	Connection connexion = null;
	Statement statement;
	String table;
	
	public SQLHelper(Connection c, String table){
		this.connexion = c;
		this.table = table;
		try {
			this.statement = this.connexion.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object[] insert(BSONObject... BSONObjects) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] insert(HashMap<String, String>... maps) {
		for(HashMap<String, String> map : maps){
			String keys = "";
			String values = "";
			for(String key : map.keySet()){
				if(!key.equals("id")){
					keys += key+", ";
					values += "'"+map.get(key)+"', ";
				}
			}
			keys = keys.substring(0, keys.length()-2);
			values = values.substring(0, values.length()-2);
			String query = "INSERT INTO "+this.table+"("+keys+") VALUES ("+values+") ;";
			System.out.println(query);
			try {
				this.statement.executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean remove(BSONObject... BSONObjects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(String... JSONStrings) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Object> find(Object query) {
		// TODO Auto-generated method stub
		try {
			this.statement.executeQuery("");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getObjectIDKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> find(BSONObject query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] insert(String... JSONStrings) {
		// TODO Auto-generated method stub
		return null;
	}

}
