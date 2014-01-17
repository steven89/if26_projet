package fr.utt.if26.cs.utils;

import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.database.DatabaseManager;
import fr.utt.if26.cs.exceptions.BeanException;
import fr.utt.if26.cs.model.DataBean;
import fr.utt.if26.cs.model.User;

/**
 * Utils used to retrieve user from DB quickly
 * @author steven
 */
public class UserUtils {
	
	/**
	 * retrieve an user using its tag
	 * @param tag : user's tag
	 * @return user
	 * @throws BeanException
	 */
	public static User getUserFromTag(String tag) throws BeanException{
		Database db = DatabaseManager.getInstance().getBase(DatabaseManager.USERS);
		DataBean bean = db.getBean("tag", tag);
		return (User) bean;
	}
	
	/**
	 * retrieve an user using its email
	 * @param email : user's email
	 * @return user
	 * @throws BeanException
	 */
	public static User getUserFromEmail(String email) throws BeanException{
		Database db = DatabaseManager.getInstance().getBase(DatabaseManager.USERS);
		DataBean bean = db.getBean("email", email);
		return (User) bean;
	}
}
