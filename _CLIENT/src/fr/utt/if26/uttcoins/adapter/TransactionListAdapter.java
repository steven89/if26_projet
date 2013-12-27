package fr.utt.if26.uttcoins.adapter;

import java.util.ArrayList;

import org.json.JSONObject;

import fr.utt.if26.uttcoins.R;
import fr.utt.if26.uttcoins.model.Transaction;
import fr.utt.if26.uttcoins.model.TransactionList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TransactionListAdapter extends ArrayAdapter<Transaction> {

	private Context context;
	private ArrayList<Transaction> items;
	
	private ImageView personIco;
	private TextView personName;
	private TextView credit;
	private TextView debit;
	
	
	public TransactionListAdapter(Context context, ArrayList<Transaction> itemsList) {
		super(context, R.layout.transaction_row, itemsList);
		this.context = context;
		this.items = itemsList;
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
		
		//TODO : un affichage intelligent
		this.personName.setText(transaction.crediteur);
		this.credit.setText(Float.toString(transaction.solde));		
		return rowView;
	}
}
