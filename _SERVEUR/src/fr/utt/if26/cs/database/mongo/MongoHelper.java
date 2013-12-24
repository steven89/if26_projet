package fr.utt.if26.cs.database.mongo;

import java.util.ArrayList;
import java.util.HashMap;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

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
	public void insert(BSONObject BSONObject) {
		this.collection.insert((DBObject) BSONObject);
	}

	@Override
	public void insert(String JSONString) {
		BSONObject object = new BasicBSONObject();
		object = (BSONObject) JSON.parse(JSONString);
		this.insert(object);
	}

	@Override
	public boolean remove(BSONObject... BSONObjects) {
		for(BSONObject obj : BSONObjects){
			this.collection.remove((DBObject) obj);
		}
		return false;
	}

	@Override
	public boolean remove(String... JSONStrings) {
		BSONObject[] objects = new BSONObject[JSONStrings.length];
		for(int i=0;i<JSONStrings.length; i++){
			objects[i] = (BSONObject) JSON.parse(JSONStrings[i]);
		}
		return this.remove(objects);
	}

	@Override
	public ArrayList<BSONObject> find(Object query) {
		return find((BSONObject) JSON.parse((String) query));
	}

	
	@Override
	public ArrayList<BSONObject> find(BSONObject query) {
		ArrayList<BSONObject> liste = new ArrayList<BSONObject>();
		DBCursor cursor = this.collection.find((DBObject) query);
		while(cursor.hasNext()){
			liste.add(cursor.next());
		}
		cursor.close();
		return liste;
	}
	
	@Override
	public String getObjectIDKey() {
		// TODO Auto-generated method stub
		return objectIDKey;
	}

	@Override
	public void insert(HashMap<String, String> map) {
		// TODO Auto-generated method stub
	}

	@Override
	public BSONObject findByKey(String key, String value) {
		// TODO Auto-generated method stub
		return null;
	}
}
