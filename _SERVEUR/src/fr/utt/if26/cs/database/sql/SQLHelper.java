package fr.utt.if26.cs.database.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

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
	public void insert(BSONObject BSONObject) {
		// TODO Auto-generated method stub
	}

	@Override
	public void insert(HashMap<String, String> map) {
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
		try {
			this.statement.executeUpdate(query);
		} catch (MySQLIntegrityConstraintViolationException e){
			//TODO : entry already exists
			System.out.println(e.getMessage());
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
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
	public ArrayList<BSONObject> find(Object query) {
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
	public ArrayList<BSONObject> find(BSONObject query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(String JSONString) {
		// TODO Auto-generated method stub
	}

	@Override
	public BSONObject findByKey(String key, String value) {
		BSONObject map = new BasicBSONObject();
		String query = "SELECT * FROM "+this.table+" WHERE "+key+"='"+value+"'";
		System.out.println(query);
		try {
			ResultSet result = this.statement.executeQuery(query);
			result.first();
			System.out.println(result.getMetaData().getColumnCount());
			for(int i=1; i<= result.getMetaData().getColumnCount(); i++){
				map.put(result.getMetaData().getColumnLabel(i), result.getString(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}

}
