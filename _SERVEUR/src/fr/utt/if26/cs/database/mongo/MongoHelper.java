package fr.utt.if26.cs.database.mongo;

import java.util.Iterator;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.MongoClient;

import fr.utt.if26.cs.database.DatabaseHelper;

public class MongoHelper implements DatabaseHelper {
	
	protected String objectIDKey = "_id";
	protected static MongoClient client;
	protected static String[] collections;
	
	private static MongoHelper mongoDBManagerInstance;
	
	private MongoHelper(){
	}
	
	public MongoHelper getInstance(){
		return mongoDBManagerInstance = (mongoDBManagerInstance != null) ? mongoDBManagerInstance : new MongoHelper();
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
