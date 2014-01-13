package fr.utt.if26.cs.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.database.DatabaseManager;
import fr.utt.if26.cs.database.mongo.MongoDatabase;
import fr.utt.if26.cs.exceptions.BeanException;

public abstract class BanManager {
	
	private final static int MAX_LOG = 5;
	private final static int BAN_MINS = 10;
	private final static int BAN_HOURS = 0;
	
	public static boolean canLog(String email){
		try {
			if(BanManager.isBan(email)){
				return false;
			}
			else if(BanManager.getLogTry(email)>=BanManager.MAX_LOG){
				BanManager.ban(email);
				return false;
			}
			else
				return true;
		} catch (BeanException e) {
			return false;
		}
	}
	
	public static void addLogTry(String email) throws BeanException{
		Database db = DatabaseManager.getInstance().getBase(DatabaseManager.TRANSACTIONS);
		db.chooseSet(MongoDatabase.CONNEXIONS);
		DataBean bean = new ConnexionTry(email);
		db.insertBean(bean);
		db.chooseSet(MongoDatabase.TRANSACTIONS);
	}
	
	private static void ban(String email) throws BeanException{
		Database db = DatabaseManager.getInstance().getBase(DatabaseManager.TRANSACTIONS);
		db.chooseSet(MongoDatabase.CONNEXIONS);
		db.removeBeans(new BasicBSONObject("email",email));
		db.chooseSet(MongoDatabase.BAN);
		Date banDate = new Date(
			Calendar.getInstance().getTimeInMillis()+(BAN_HOURS*60*60000)+(BAN_MINS*60000)
		);
		System.out.println(email+" banned till "+banDate);
		db.insertBean(
			new BanLog(email, banDate.getTime()));
		db.chooseSet(MongoDatabase.TRANSACTIONS);
	}
	
	private static boolean isBan(String email) throws BeanException{
		boolean isBan = false;
		Database db = DatabaseManager.getInstance().getBase(DatabaseManager.TRANSACTIONS);
		db.chooseSet(MongoDatabase.BAN);
		BanLog bean = (BanLog) db.getBean("email", email);
		if(bean!=null){
			Date current = new Date();
			Date banDate = new Date(bean.getDate());
			if(current.after(banDate)){
				db.removeBean(bean);
				isBan = false;
			}
			else
				isBan = true;
		}
		else
			isBan = false;
		db.chooseSet(MongoDatabase.TRANSACTIONS);
		return isBan;
	}
	
	private static int getLogTry(String email) throws BeanException{
		Database db = DatabaseManager.getInstance().getBase(DatabaseManager.TRANSACTIONS);
		db.chooseSet(MongoDatabase.CONNEXIONS);
		BSONObject obj = new BasicBSONObject();
		obj.put("email", email);
		ArrayList<DataBean> list = db.findBeans(obj);
		db.chooseSet(MongoDatabase.TRANSACTIONS);
		return list.size();
	}
}
