package fr.utt.if26.cs.database;

import sun.security.jca.GetInstance;
import fr.utt.if26.cs.model.Transaction;

abstract class Database {
	
	private static Database db;
	
	public abstract boolean insertTransaction(Transaction transaction);
	
	public static Database getInstance(){
		return null;
	}
}
