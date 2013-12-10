package fr.utt.if26.cs.database;

import sun.security.jca.GetInstance;
import fr.utt.if26.cs.model.Transaction;
import fr.utt.if26.cs.model.User;

public abstract class Database {
	
	private static Database db;
	
	public abstract boolean insertTransaction(Transaction transaction);
	
	public abstract boolean insertUser(User user);
	public abstract boolean updateUser(User user);
	
	public static Database getInstance(){
		return null;
	}
	
	
}
