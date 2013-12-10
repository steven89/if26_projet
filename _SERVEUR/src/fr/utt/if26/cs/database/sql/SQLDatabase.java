package fr.utt.if26.cs.database.sql;

import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.model.Transaction;
import fr.utt.if26.cs.model.User;

public class SQLDatabase extends Database {
	
	private static SQLDatabase db=null;
	
	private SQLDatabase(){
		
	}
	
	public static SQLDatabase getInstance(){
		return (SQLDatabase.db!=null)?SQLDatabase.db:new SQLDatabase();
	}

	@Override
	public boolean insertTransaction(Transaction transaction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}
}
