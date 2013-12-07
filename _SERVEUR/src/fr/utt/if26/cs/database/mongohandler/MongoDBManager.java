package fr.utt.if26.cs.database.mongohandler;

import java.util.Iterator;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.MongoClient;

import fr.utt.if26.cs.database.DBManager;

public class MongoDBManager implements DBManager {
	
	protected String objectIDKey = "_id";
	protected static MongoClient client;
	protected static String[] collections;
	
	private static MongoDBManager mongoDBManagerInstance;
	
	private MongoDBManager(){
	}
	
	public MongoDBManager getInstance(){
		return mongoDBManagerInstance = (mongoDBManagerInstance != null) ? mongoDBManagerInstance : new MongoDBManager();
	}
	
	public static void setClient(MongoClient mongoClient){
		client = mongoClient;
	}

	@Override
	public Object[] insert(Object filterQuery, BSONObject... BSONObjects) {
		// TODO Auto-generated method stub
		BSONObject BSONQueryObject = (BasicBSONObject) filterQuery;
		BasicDBList collections = (BasicDBList) BSONQueryObject.get("collections");
		return null;
	}

	@Override
	public Object[] insert(Object filterQuery, String... JSONStrings) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object filterQuery, BSONObject... BSONObjects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Object filterQuery, String... JSONStrings) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<Object> find(Object query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjectIDKey() {
		// TODO Auto-generated method stub
		return objectIDKey;
	}

}
