package fr.utt.if26.cs.database.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.util.JSON;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import fr.utt.if26.cs.database.DatabaseHelper;

public class SQLHelper implements DatabaseHelper {
	
	Connection connexion = null;
	PreparedStatement statement;
	String table;
	
	public SQLHelper(Connection c, String table){
		this.connexion = c;
		this.table = table;
	}

	@Override
	public String insert(BSONObject datas) {
		String keys = "";
		String values = "";
		String params[] = new String[datas.keySet().size()];
		int i=0;
		for(String key : datas.keySet()){
			if(!key.equals("id")){
				keys += key+", ";
				params[i] = (String) datas.get(key);
				i++;
				values += "?, ";
			}
		}
		keys = keys.substring(0, keys.length()-2);
		values = values.substring(0, values.length()-2);
		String query = "INSERT INTO "+this.table+"("+keys+") VALUES ("+values+") ;";
		try {
			this.statement = connexion.prepareStatement(query);
			for (i=0;i<params.length;i++){
				this.statement.setString(i+1, params[i]);
			}
			this.statement.executeUpdate();
		} catch (MySQLIntegrityConstraintViolationException e){
			return "user already exists ("+e.getMessage()+")";
		}
		catch (SQLException e) {
			return e.getMessage();
		}
		return "ok";
	}
	
	@Override
	public void insert(String query) {
		this.insert((BSONObject) JSON.parse(query));
	}


	@Override
	public String getObjectIDKey() {
		return "id";
	}

	@Override
	public ArrayList<BSONObject> find(BSONObject query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BSONObject findByKey(String key, String value) {
		BSONObject map = new BasicBSONObject();
		String query = "SELECT * FROM "+this.table+" WHERE "+key+"=?";
		try {
			this.statement = connexion.prepareStatement(query);
			this.statement.setString(1, value);
			ResultSet result = this.statement.executeQuery();
			if(result.first())
				for(int i=1; i<= result.getMetaData().getColumnCount(); i++){
					map.put(result.getMetaData().getColumnLabel(i), result.getString(i));
				}
			else return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public void update(BSONObject datas) {
		String id = (String) datas.get(this.getObjectIDKey());
		String query = "UPDATE "+this.table+" SET ";
		String params[] = new String[datas.keySet().size()-1];
		int i=0;
		for(String key : datas.keySet()){
			if(!key.equals(this.getObjectIDKey())){
				params[i] = (String) datas.get(key);
				i++;
				query += key+" = ?, ";
			}
		}
		query = query.substring(0, query.length()-2);
		query += " WHERE "+this.getObjectIDKey()+" = '"+id+"';";
		try {
			this.statement = connexion.prepareStatement(query);
			for(i=0;i<params.length;i++){
				this.statement.setString(i+1, params[i]);
			}
			this.statement.executeUpdate();
		} catch (MySQLIntegrityConstraintViolationException e){
			//TODO : entry already exists
			System.out.println(e.getMessage());
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void remove(BSONObject datas) {
		// TODO Auto-generated method stub
		
	}

}
