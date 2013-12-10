package fr.utt.if26.cs.database;

import java.net.UnknownHostException;
import java.util.Set;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class DatabaseManager {
	
	private static DatabaseManager db = null;
	
	
	
	
	private DatabaseManager(){
		this.initBases();
	}
	
	public DatabaseManager getInstance(){
		return (DatabaseManager.db!=null)?DatabaseManager.db:new DatabaseManager();
	}
	
	private void initBases(){
		
	}
	
	

}
