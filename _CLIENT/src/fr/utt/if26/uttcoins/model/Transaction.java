package fr.utt.if26.uttcoins.model;

import org.bson.BasicBSONObject;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import fr.utt.if26.uttcoins.utils.ServerHelper;

public class Transaction {

	public static String TRANSACTION_AMOUNT_KEY = "transaction_amount";
	public static String TRANSACTION_RECEIVER_KEY = "transaction_receiver";
	
	private String id;
	private String receiver;
	private String sender;
	private int amount;
	
	public Transaction(String transaction_id, String receiver, String sender, 
			int transaction_amount){
		this.id = transaction_id;
		this.receiver = receiver;
		this.sender = sender;
		this.setAmount(transaction_amount);
	}
	
	public Transaction(String receiver, int amount){
		this.setAmount(amount);
		this.setReceiver(receiver);
	}

	public String toString(){
		JSONObject representation = new JSONObject();
		try {
			representation.put("id", this.id);
			representation.put("from", this.sender);
			representation.put("to", this.receiver);
			representation.put("balance", this.amount);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return representation.toString();
	}

	public String getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getReceiver(){
		return this.receiver;
	}
	
	public void setReceiver(String receiver){
		if(receiver.matches(User.TAG_PATTERN) 
				&& !receiver.equals(ServerHelper.getSession().getString(ServerHelper.SERVER_TAG_KEY)) ){
			this.receiver = receiver;
		}else{
			Log.e("Transaction", "illegal receiverName ("+receiver+")");
		}
	}
	
	public String getSender(){
		return this.sender;
	}
	
	public void setSender(String sender){
		if(sender.matches(User.TAG_PATTERN) 
				&& sender.equals(ServerHelper.getSession().getString(ServerHelper.SERVER_TAG_KEY)) ){
			this.sender = sender;
		}else{
			Log.e("Transaction", "illegal senderrName ("+sender+")");
		}
	}
	
	public int getAmount() {
		// TODO Auto-generated method stub
		return this.amount;
	}

	public void setAmount(int amount) {
		if(amount >= 0){
			this.amount = amount;
		}else{
			Log.e("TRANSACTION", "amount ("+Integer.toString(amount)+") > 0 ");
		}
	}
}
