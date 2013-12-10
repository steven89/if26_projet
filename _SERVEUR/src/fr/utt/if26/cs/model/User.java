package fr.utt.if26.cs.model;

import java.util.HashMap;

import org.bson.BSONObject;

import fr.utt.if26.cs.database.DBPersistentObject;
import fr.utt.if26.cs.database.DatabaseHelper;
import fr.utt.if26.cs.utils.Crypt;


public class User implements DBPersistentObject {
	String id=null, email, pass, token;
	int uttCoins;
	HashMap<String, Transaction> transactions;
	
	public User(String id, String email, String pass, int coins){
		this.setId(id);
		this.setEmail(email);
		this.setPass(pass);
		this.setUttCoins(coins);
	}
	
	public User(String email, String pass){
		
	}
	
	public void setId(String id){
		if(this.id!=null)
			this.id = id;
	}
	
	public void setEmail(String email){
		// TODO : regex verification du mail
		this.email = email;
	}
	
	public void setPass(String pass){
		// TODO : saler le pass
		this.pass = Crypt.sha1(pass);
	}
	
	public void setToken(String token){
		this.token = token;
	}
	
	public void setUttCoins(int coins){
		this.uttCoins = coins;
	}
	
	public void setTransactions(HashMap<String, Transaction> t){
		this.transactions = t;
	}
	
	public void addTransaction(Transaction t){
		this.transactions.put(t.getId(), t);
	}
	
	public void removeTransaction(String id){
		this.transactions.remove(id);
	}
	
	public String getId(){
		return this.id;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public String getPass(){
		return this.pass;
	}
	
	public String getToken(){
		return this.token;
	}
	
	public int getUttCoins(){
		return this.uttCoins;
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(DatabaseHelper... DBManagers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sync() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BSONObject getBSONRepresentation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getJSONStringRepresentation() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
