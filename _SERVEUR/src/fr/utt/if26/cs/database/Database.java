package fr.utt.if26.cs.database;

import java.util.HashMap;

import com.mongodb.MongoClient;

public class Database {
	
	private String[] bases = {"users"};
	private String[][] nosqlBases = {{"test", "transactions1"},{"test", "transactions2"}};
	private MongoClient[] mongoBases = new MongoClient[nosqlBases.length];
	//private [] sqlBases = new MongoClient[bases.length];
	
	public Database(){
		this.initBases();
	}
	
	private void initBases(){
		
	}

}
