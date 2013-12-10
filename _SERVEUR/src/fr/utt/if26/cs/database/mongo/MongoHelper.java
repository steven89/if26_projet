package fr.utt.if26.cs.database.mongo;

import java.util.ArrayList;

import org.bson.BSONObject;

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
	public Object[] insert(BSONObject... BSONObjects) {
		/*BSONObject BSONQueryObject = (BasicBSONObject) filterQuery;
		BasicDBList collections = (BasicDBList) BSONQueryObject.get("collections");*/
		for(BSONObject obj : BSONObjects){
			this.collection.insert((DBObject) obj);
		}
		return null;
	}

	@Override
	public Object[] insert(String... JSONStrings) {
		BSONObject[] objects = new BSONObject[JSONStrings.length];
		for(int i=0;i<JSONStrings.length; i++){
			objects[i] = (BSONObject) JSON.parse(JSONStrings[i]);
		}
		this.insert(objects);
		return null;
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
	public ArrayList<Object> find(Object query) {
		return find((BSONObject) JSON.parse((String) query));
	}

	
	@Override
	public ArrayList<Object> find(BSONObject query) {
		ArrayList<Object> liste = new ArrayList<Object>();
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
}
