package fr.utt.if26.cs.database.mongo;

import java.util.ArrayList;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
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
	public String insert(BSONObject datas) {
		this.collection.insert((DBObject) JSON.parse(datas.toString()));
		return "ok";
	}

	@Override
	public void insert(String datas) {
		BSONObject object = new BasicBSONObject();
		object = (BSONObject) JSON.parse(datas);
		this.insert(object);
	}
	
	@Override
	public ArrayList<BSONObject> find(BSONObject query) {
		ArrayList<BSONObject> liste = new ArrayList<BSONObject>();
		DBCursor cursor = this.collection.find((DBObject) JSON.parse(query.toString()));
		while(cursor.hasNext()){
			liste.add(cursor.next());
		}
		cursor.close();
		return liste;
	}
	
	@Override
	public String getObjectIDKey() {
		return objectIDKey;
	}

	@Override
	public BSONObject findByKey(String key, String value) {
		DBObject obj = new BasicDBObject();
		if(key.equals("id"))
			obj.put("_id", new ObjectId(value));
		else
			obj.put(key, value);
		DBCursor cursor = this.collection.find(obj);
		if(cursor.hasNext())
			return (BSONObject) cursor.next();
		else
			return null;
	}

	@Override
	public void update(BSONObject datas) {
		// TODO Auto-generated method stub
		
	}
}
