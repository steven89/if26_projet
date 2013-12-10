package fr.utt.if26.cs.database.mongo;

import java.util.Iterator;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import fr.utt.if26.cs.database.DatabaseHelper;

public class MongoHelper implements DatabaseHelper {
	
	protected String objectIDKey = "_id";
	private MongoClient client;
	private DBCollection collection;
	
	public MongoHelper(MongoClient c, DBCollection coll){
		this.client = c;
		this.collection = coll;
	}
	
	public void setClient(MongoClient c){
		this.client = c;
	}
	
	public void setCollection(DBCollection coll){
		this.collection = coll;
	}
	
	public MongoClient getClient(){
		return this.client;
	}
	
	public DBCollection getCollection(){
		return this.collection;
	}
	
	@Override
	public Object[] insert(BSONObject... BSONObjects) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] insert(String... JSONStrings) {
		// TODO Auto-generated method stub
		return null;
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