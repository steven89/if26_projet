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

/**
 * Manage user ban on login
 * @author steven
 */
public abstract class BanManager {
	
	private final static int MAX_LOG = 5;
	private final static int BAN_MINS = 10;
	private final static int BAN_HOURS = 0;
	
	/**
	 * check whether or not the user is allowed to login
	 * @param email : user email
	 * @return true if user is allowed to login
	 */
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
	
	/**
	 * add a try on user login
	 * @param email : user email
	 * @throws BeanException
	 */
	public static void addLogTry(String email) throws BeanException{
		Database db = DatabaseManager.getInstance().getBase(DatabaseManager.TRANSACTIONS);
		db.chooseSet(MongoDatabase.CONNEXIONS);
		DataBean bean = new ConnexionTry(email);
		db.insertBean(bean);
		db.chooseSet(MongoDatabase.TRANSACTIONS);
	}
	
	/**
	 * ban an user
	 * @param email : user email
	 * @throws BeanException
	 */
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
	
	/**
	 * check whether or not an user is banned
	 * @param email : user email
	 * @return true is user is banned
	 * @throws BeanException
	 */
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
	
	/**
	 * get number of login tries from an user
	 * @param email : user email
	 * @return number of login tries
	 * @throws BeanException
	 */
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
