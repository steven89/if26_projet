package fr.utt.if26.uttcoins.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Transaction {

	public int id;
	public String debiteur;
	public String crediteur;
	public int amount;
	
	public Transaction(int transaction_id, String debiteur_name, String crediteur_name, 
			int transaction_amount){
		this.id = transaction_id;
		this.debiteur = debiteur_name;
		this.crediteur = crediteur_name;
		this.amount = transaction_amount;
	}
		
	public String toString(){
		JSONObject representation = new JSONObject();
		try {
			representation.put("id", this.id);
			representation.put("debiteur", this.debiteur);
			representation.put("crediteur", this.crediteur);
			representation.put("solde", this.amount);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return representation.toString();
	}

	public String getOtherUser() {
		// TODO Rename method for more explicit name
		// add some logic
		return this.crediteur;
	}

	public int getAmount() {
		// TODO Auto-generated method stub
		return this.amount;
	}
}
