package fr.utt.if26.cs.database;

import java.util.HashMap;

import fr.utt.if26.cs.database.mongo.MongoDatabase;
import fr.utt.if26.cs.database.sql.SQLDatabase;

/**
 * Manage webservice's databases
 * @author steven
 */
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
	
	/**
	 * Singleton
	 * @return global DBManger instance
	 */
	public static DatabaseManager getInstance(){
		return (DatabaseManager.db!=null)?DatabaseManager.db:new DatabaseManager();
	}
	
	/**
	 * init the webservice's databases
	 */
	private void initBases(){
		this.sqlDb = SQLDatabase.getInstance();
		this.mongoDb = MongoDatabase.getInstance();
		this.bases = new HashMap<>();
		this.bases.put("users", this.sqlDb);
		this.bases.put("transactions", this.mongoDb);
	}
	
	/**
	 * get instance of NoSQL DB : MongoDB
	 * @return global instance of MongoDB
	 */
	public Database getMongoDatabase(){
		return this.mongoDb;
	}
	
	/**
	 * get instance of SQL DB : MySQL
	 * @return global instance of MySQL DB
	 */
	public Database getSqlDatabase(){
		return this.sqlDb;
	}
	
	/**
	 * get instance of one of the DB
	 * @param base : index of the DB {@link DatabaseManager#USERS} or {@link DatabaseManager#TRANSACTIONS}
	 * @return global instance of DB
	 */
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
