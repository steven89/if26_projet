package fr.utt.if26.cs.database;

import fr.utt.if26.cs.database.mongo.MongoDatabase;


public class DatabaseManager {
	
	private static DatabaseManager db = null;
	private SQLDatabase sqlDb;
	private MongoDatabase mongoDb;
	
	private DatabaseManager(){
		this.initBases();
	}
	
	public DatabaseManager getInstance(){
		return (DatabaseManager.db!=null)?DatabaseManager.db:new DatabaseManager();
	}
	
	private void initBases(){
		this.sqlDb = SQLDatabase.getInstance();
		this.mongoDb = MongoDatabase.getInstance();
	}
	
	public Database getMongoDatabase(){
		return this.mongoDb;
	}
	
	public Database getSqlDatabase(){
		return this.sqlDb;
	}
	
	
}
