package fr.utt.if26.cs.database;

import java.net.UnknownHostException;

import org.bson.BSON;
import org.bson.BSONObject;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;


public class MongoDatabase implements Database {
	
	private static MongoDatabase db=null;
	
	private String base = "test";
	private String[] collections = {"transactions1", "transactions2"};
	private int currentCollection = 0;
	private MongoClient mongoClient;
	private DB mongoDB;
	private DBCollection[] mongoCollections = new DBCollection[collections.length];
	
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
	}
	
	public MongoDatabase getInstance(){
		return (MongoDatabase.db!=null)?MongoDatabase.db:new MongoDatabase();
	}
	
	public BSONObject getItemById(String id){
		
		DBCursor cursor = mongoCollections[currentCollection].find((DBObject) JSON.parse("{id : "+id+"}"));
		while(cursor.hasNext()){
			//out.print(cursor.next());
		}
		cursor.close();
		return null;
	}

}
