package fr.utt.if26.cs.database.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;

import org.bson.BSONObject;
import org.bson.types.ObjectId;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.database.DatabaseHelper;
import fr.utt.if26.cs.exceptions.BeanException;
import fr.utt.if26.cs.model.BanLog;
import fr.utt.if26.cs.model.ConnexionTry;
import fr.utt.if26.cs.model.DataBean;
import fr.utt.if26.cs.model.Transaction;


public class MongoDatabase extends Database {
	
	private static MongoDatabase db=null;
	
	private String base = "test";
	private String[] collections = {"transactions1", "connexions", "bans"};
	private MongoClient mongoClient;
	private DB mongoDB;
	private DBCollection[] mongoCollections = new DBCollection[collections.length];
	private DatabaseHelper helpers[] = new DatabaseHelper[collections.length];
	
	public int currentSet = 0;
	public final static int TRANSACTIONS = 0;
	public final static int CONNEXIONS = 1;
	public final static int BAN = 2;
	
	private MongoDatabase(){
		super();
	}
	
	public static MongoDatabase getInstance(){
		return (MongoDatabase.db!=null)?MongoDatabase.db:new MongoDatabase();
	}
	
	public void chooseSet(int index){
		if(index>=0 && index<collections.length)
			this.currentSet = index;
	}
	
	public BSONObject getItemById(String id){
		
		return null;
	}

	@Override
	public void open() {
		System.out.println("opening connexion on mongo database");
		try {
			mongoClient = new MongoClient();
			mongoDB = mongoClient.getDB(this.base);
			for(int i=0; i<collections.length; i++){
				mongoCollections[i] = mongoDB.getCollection(collections[i]);
				helpers[i] = new MongoHelper(this.mongoClient, mongoCollections[i]);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		System.out.println("closing connexion on mongo database");
		this.mongoClient.close();
		this.mongoCollections = new DBCollection[collections.length];
		this.helpers = new DatabaseHelper[collections.length];
	}

	@Override
	public DataBean getBean(BSONObject datas) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataBean getBean(String key, String value) throws BeanException {
		switch(this.currentSet){
		case MongoDatabase.TRANSACTIONS:
			return this.getTransaction(key, value);
		case MongoDatabase.BAN:
			return this.getBanLog(key, value);
		case MongoDatabase.CONNEXIONS:
			return this.getConnexionTry(key, value);
		default:
			return null;
		}
	}
	
	private DataBean getTransaction(String key, String value) throws BeanException{
		BSONObject datas = helpers[this.currentSet].findByKey(key, value);
		if(datas!=null)
			return new Transaction(
				((ObjectId) datas.get("_id")).toString(),
				Integer.parseInt((String) datas.get("amount")),
				(String) datas.get("from"), 
				(String) datas.get("to"),
				(String) datas.get("date"));
		else
			return null;
	}
	
	private DataBean getBanLog(String key, String value) throws BeanException{
		BSONObject datas = helpers[this.currentSet].findByKey(key, value);
		if(datas!=null)
			return new BanLog(
				(String) datas.get("email"),
				Long.parseLong((String) datas.get("date")));
		else
			return null;
	}
	
	private DataBean getConnexionTry(String key, String value) throws BeanException{
		BSONObject datas = helpers[this.currentSet].findByKey(key, value);
		if(datas!=null)
			return new ConnexionTry(
				(String) datas.get("email"),
				Long.parseLong((String) datas.get("date")));
		else
			return null;
	}

	@Override
	public ArrayList<DataBean> findBeans(BSONObject datas) throws BeanException {
		switch(this.currentSet){
		case MongoDatabase.TRANSACTIONS:
			return this.findTransactions(datas);
		case MongoDatabase.BAN:
			return this.findBanLogs(datas);
		case MongoDatabase.CONNEXIONS:
			return this.findConnexionTries(datas);
		default:
			return new ArrayList<>();
		}
	}
	
	private ArrayList<DataBean> findTransactions(BSONObject datas) throws BeanException{
		ArrayList<BSONObject> dbResult = helpers[this.currentSet].find(datas);
		ArrayList<DataBean> result = new ArrayList<>();
		for(BSONObject obj : dbResult){
			result.add(new Transaction(
				((ObjectId) obj.get("_id")).toString(),
				Integer.parseInt((String) obj.get("amount")),
				(String) obj.get("from"), 
				(String) obj.get("to"),
				(String) obj.get("date"))
			);
		}
		return result;
	}
	
	private ArrayList<DataBean> findBanLogs(BSONObject datas) throws BeanException{
		ArrayList<BSONObject> dbResult = helpers[this.currentSet].find(datas);
		ArrayList<DataBean> result = new ArrayList<>();
		for(BSONObject obj : dbResult){
			result.add(new BanLog(
				(String) obj.get("email"),
				Long.parseLong((String) obj.get("date")))
			);
		}
		return result;
	}
	
	private ArrayList<DataBean> findConnexionTries(BSONObject datas) throws BeanException{
		ArrayList<BSONObject> dbResult = helpers[this.currentSet].find(datas);
		ArrayList<DataBean> result = new ArrayList<>();
		for(BSONObject obj : dbResult){
			result.add(new ConnexionTry(
				(String) obj.get("email"),
				Long.parseLong((String) obj.get("date")))
			);
		}
		return result;
	}
	
	@Override
	public boolean insertBean(DataBean bean) {
		helpers[this.currentSet].insert(bean.toBSON());
		return true;
	}
	
	@Override
	public boolean updateBean(DataBean bean) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeBean(DataBean bean) {
		helpers[this.currentSet].remove(bean.toBSON());
		return true;
	}
	@Override
	public boolean removeBeans(BSONObject datas) {
		helpers[this.currentSet].remove(datas);
		return true;
	}

}
