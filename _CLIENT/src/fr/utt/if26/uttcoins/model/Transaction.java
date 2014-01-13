package fr.utt.if26.uttcoins.model;

import org.bson.BasicBSONObject;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import fr.utt.if26.uttcoins.error.CustomIllegalParametter;
import fr.utt.if26.uttcoins.utils.ServerHelper;

public class Transaction {

	private String id;
	private String receiver;
	private String sender;
	private int amount;
	
	public Transaction(String transaction_id, String receiver, String sender, 
			int transaction_amount){
		this.id = transaction_id;
		this.receiver = receiver;
		this.sender = sender;
		try{
			this.setAmount(transaction_amount);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Transaction(String receiver, int amount){
		try{
			this.setAmount(amount);
			this.setReceiver(receiver);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public String toString(){
		JSONObject representation = new JSONObject();
		try {
			representation.put(ServerHelper.SERVER_ID_KEY, this.id);
			representation.put(ServerHelper.SERVER_SENDER_KEY, this.sender);
			representation.put(ServerHelper.SERVER_RECEIVER_KEY, this.receiver);
			representation.put(ServerHelper.SERVER_BALANCE_KEY, this.amount);
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
	
	public void setReceiver(String receiver) throws CustomIllegalParametter{
		Log.i("Transaction", "set receiver ("+receiver+") with session = "+ServerHelper.getSession().toString());
		if(receiver.matches(User.TAG_PATTERN) 
				&& !receiver.equals(ServerHelper.getSession().getString(ServerHelper.SERVER_TAG_KEY)) ){
			this.receiver = receiver;
		}else{
			Log.e("Transaction", "illegal receiverName ("+receiver+")");
			throw new CustomIllegalParametter();
		}
	}
	
	public String getSender(){
		return this.sender;
	}
	
	public void setSender(String sender) throws CustomIllegalParametter{
		if(sender.matches(User.TAG_PATTERN) 
				&& sender.equals(ServerHelper.getSession().getString(ServerHelper.SERVER_TAG_KEY)) ){
			this.sender = sender;
		}else{
			Log.e("Transaction", "illegal senderrName ("+sender+")");
			throw new CustomIllegalParametter();
		}
	}
	
	public int getAmount() {
		// TODO Auto-generated method stub
		return this.amount;
	}

	public void setAmount(int amount) throws CustomIllegalParametter{
		if(amount >= 0){
			this.amount = amount;
		}else{
			Log.e("TRANSACTION", "amount ("+Integer.toString(amount)+") > 0 ");
			throw new CustomIllegalParametter();
		}
	}
}
