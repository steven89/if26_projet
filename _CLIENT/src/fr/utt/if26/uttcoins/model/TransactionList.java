package fr.utt.if26.uttcoins.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.bson.BasicBSONCallback;

import fr.utt.if26.uttcoins.server.bson.CustomBasicBSONCallback;
import fr.utt.if26.uttcoins.utils.ServerHelper;
import android.util.SparseArray;

public class TransactionList {

	public static ArrayList<Transaction> ITEMS = new ArrayList<Transaction>();
	public static HashMap<String, Transaction> ITEMS_MAP = new HashMap<String, Transaction>();

	static{
		addTransaction(new Transaction("0", "julien@test.com", "Steven", 30));
		addTransaction(new Transaction("0", "steven@test.com", "Thibault", 15));
		addTransaction(new Transaction("0", "Kevin", "julien@test.com", 15));
	}
	
	public static void addTransaction(Transaction item){
		ITEMS.add(item);
		ITEMS_MAP.put(item.getId(), item);
	}

	public static void loadData() {
		ServerHelper.getUserTransactions();
	}
	
	public static void clear(){
		ITEMS.clear();
		ITEMS_MAP.clear();
	}
}
