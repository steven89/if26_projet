package fr.utt.if26.uttcoins.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Transaction {

	public int id;
	public String debiteur;
	public String crediteur;
	public float solde;
	
	public Transaction(int transaction_id, String debiteur_name, String crediteur_name, 
			float transaction_solde){
		this.id = transaction_id;
		this.debiteur = debiteur_name;
		this.crediteur = crediteur_name;
		this.solde = transaction_solde;
	}
		
	public String toString(){
		JSONObject representation = new JSONObject();
		try {
			representation.put("id", this.id);
			representation.put("debiteur", this.debiteur);
			representation.put("crediteur", this.crediteur);
			representation.put("solde", this.solde);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return representation.toString();
	}
}
