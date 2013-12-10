package fr.utt.if26.cs.database.mongo;

import java.net.UnknownHostException;

import org.bson.BSONObject;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.database.DatabaseHelper;
import fr.utt.if26.cs.model.Transaction;


public class MongoDatabase extends Database {
	
	private static MongoDatabase db=null;
	
	private String base = "test";
	private String[] collections = {"transactions1", "transactions2"};
	private int currentCollection = 0;
	private MongoClient mongoClient;
	private DB mongoDB;
	private DBCollection[] mongoCollections = new DBCollection[collections.length];
	private DatabaseHelper mongoHelper;
	
	private MongoDatabase(){
		try {
			mongoClient = new MongoClient();
			mongoDB = mongoClient.getDB(this.base);
			for(int i=0; i<collections.length; i++){
				mongoCollections[i] = mongoDB.getCollection(collections[i]);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		mongoHelper = new MongoHelper(this.mongoClient, mongoCollections[this.currentCollection]);
	}
	
	public static MongoDatabase getInstance(){
		return (MongoDatabase.db!=null)?MongoDatabase.db:new MongoDatabase();
	}
	
	public void shiftCollection(){
		this.currentCollection++;
		if(this.currentCollection>this.collections.length)
			this.currentCollection=0;
	}
	
	public BSONObject getItemById(String id){
		DBCursor cursor = mongoCollections[currentCollection].find((DBObject) JSON.parse("{id : "+id+"}"));
		while(cursor.hasNext()){
			
		}
		cursor.close();
		return null;
	}

	@Override
	public boolean insertTransaction(Transaction transaction) {
		// TODO Auto-generated method stub
		return false;
	}

}