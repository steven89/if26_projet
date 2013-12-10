package fr.utt.if26.cs.database;

public class SQLDatabase implements Database {
	
	private static SQLDatabase db=null;
	
	private SQLDatabase(){
		
	}
	
	public SQLDatabase getInstance(){
		return (SQLDatabase.db!=null)?SQLDatabase.db:new SQLDatabase();
	}
}
