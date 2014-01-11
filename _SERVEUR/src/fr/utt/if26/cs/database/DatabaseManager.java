package fr.utt.if26.cs.database;

import java.util.HashMap;

import fr.utt.if26.cs.database.mongo.MongoDatabase;
import fr.utt.if26.cs.database.sql.SQLDatabase;


public class DatabaseManager {
	
	private static DatabaseManager db = null;
	private SQLDatabase sqlDb;
	private MongoDatabase mongoDb;
	private HashMap<String, Database> bases;
	
	public final static int USERS = 0;
	public final static int TRANSACTIONS = 1;
	
	private DatabaseManager(){
		DatabaseManager.db = this;
		this.initBases();
	}
	
	public static DatabaseManager getInstance(){
		return (DatabaseManager.db!=null)?DatabaseManager.db:new DatabaseManager();
	}
	
	private void initBases(){
		this.sqlDb = SQLDatabase.getInstance();
		this.mongoDb = MongoDatabase.getInstance();
		this.bases = new HashMap<>();
		this.bases.put("users", this.sqlDb);
		this.bases.put("transactions", this.mongoDb);
	}
	
	public Database getMongoDatabase(){
		return this.mongoDb;
	}
	
	public Database getSqlDatabase(){
		return this.sqlDb;
	}
	
	public Database getBase(int base){
		switch(base){
		case DatabaseManager.USERS:
			return this.bases.get("users");
		case DatabaseManager.TRANSACTIONS:
			return this.bases.get("transactions");
		default:
			return null;
		}
	}
	
	
}
