package fr.utt.if26.uttcoins.adapter;

import java.util.ArrayList;

import org.bson.BasicBSONCallback;
import org.bson.BasicBSONObject;
import org.bson.types.BasicBSONList;
import org.json.JSONObject;

import com.mongodb.ServerError;
import com.mongodb.util.JSON;

import fr.utt.if26.uttcoins.R;
import fr.utt.if26.uttcoins.model.Transaction;
import fr.utt.if26.uttcoins.model.TransactionList;
import fr.utt.if26.uttcoins.server.bson.CustomBasicBSONCallback;
import fr.utt.if26.uttcoins.utils.ServerHelper;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TransactionListAdapter extends ArrayAdapter<Transaction> implements CustomBasicBSONCallback{

	private Context context;
	private ArrayList<Transaction> items;
	
	private ImageView personIco;
	private TextView personName;
	private TextView credit;
	private TextView debit;
	
	
	public TransactionListAdapter(Context context) {
		super(context, R.layout.transaction_row);
		this.context = context;
		this.items = new ArrayList<Transaction>();
		this.loadData();
	}
	
	public void loadData(){
		ServerHelper.getUserTransactions(ServerHelper.BSON_REQUEST, this);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) this.context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.transaction_row, parent, false);
		Transaction transaction = items.get(position);
		this.personIco = (ImageView) rowView.findViewById(R.id.person_icon);
		this.personName = (TextView) rowView.findViewById(R.id.person_name);
		this.credit = (TextView) rowView.findViewById(R.id.solde_credit);
		this.debit = (TextView) rowView.findViewById(R.id.solde_debit);
		String nameToDisplay;
		//si le destinataire de la transaction == l'utilisateur
		if(transaction.getReceiver().equals(ServerHelper.getSession().getString(ServerHelper.SERVER_TAG_KEY))){
			nameToDisplay = transaction.getSender();
			this.credit.setText(Integer.toString(transaction.getAmount()));		
		}else{
			nameToDisplay = transaction.getReceiver();
			this.debit.setText(Integer.toString(transaction.getAmount()));		
		}
		//TODO : un affichage intelligent
		this.personName.setText(nameToDisplay);
		return rowView;
	}

	@Override
	public Object call(BasicBSONObject bsonResponse) {
		if(bsonResponse.getString(ServerHelper.RESQUEST_TAG) == ServerHelper.GET_TRANSACTION_TAG){
			Log.i("ADAPTER", "getting result : "+bsonResponse.get(ServerHelper.SERVER_TRANSACTIONS_KEY).toString());
			BasicBSONList bsonList = (BasicBSONList) bsonResponse.get(ServerHelper.SERVER_TRANSACTIONS_KEY);
			this.items.clear();
			for(int i = 0; i < bsonList.size(); i++){
				BasicBSONObject bsonData = (BasicBSONObject) bsonList.get(i);
				if(!bsonData.getString(ServerHelper.SERVER_SENDER_KEY).equals(ServerHelper.SYSTEM_USER_TAG)){
					int transactionAmount = Integer.parseInt(bsonData.getString(ServerHelper.SERVER_TRANSACTION_AMOUNT_KEY));
					Transaction tmpTransaction = new Transaction(
							bsonData.getString(ServerHelper.SERVER_ID_KEY), 
							bsonData.getString(ServerHelper.SERVER_RECEIVER_KEY), 
							bsonData.getString(ServerHelper.SERVER_SENDER_KEY), 
							transactionAmount);
					this.items.add(tmpTransaction); 
				}
			}			
			this.notifyDataSetChanged();
		}
		return null;
	}

	@Override
	public void onError(Bundle errorObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeCall() {
		// TODO Auto-generated method stub
		
	}
}
