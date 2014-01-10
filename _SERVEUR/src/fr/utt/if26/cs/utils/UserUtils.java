package fr.utt.if26.cs.utils;

import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.database.DatabaseManager;
import fr.utt.if26.cs.exceptions.BeanException;
import fr.utt.if26.cs.model.DataBean;
import fr.utt.if26.cs.model.User;

public class UserUtils {

	public static User getUserFromTag(String tag) throws BeanException{
		Database db = DatabaseManager.getInstance().getBase(DatabaseManager.USERS);
		db.open();
		DataBean bean = db.getBean("tag", tag);
		db.close();
		return (User) bean;
	}
	
}
