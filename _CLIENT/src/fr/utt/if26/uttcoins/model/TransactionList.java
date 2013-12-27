package fr.utt.if26.uttcoins.model;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.SparseArray;

public class TransactionList {

	public static ArrayList<Transaction> ITEMS = new ArrayList<Transaction>();
	public static SparseArray<Transaction> ITEMS_MAP = new SparseArray<Transaction>();

	static{
		addTransaction(new Transaction(0, "Julien", "Steven", 30));
		addTransaction(new Transaction(1, "Steven", "Thibault", 15));
		addTransaction(new Transaction(2, "Kevin", "Julien", 15));
	}
	
	public static void loadTransaction(){
		//TODO LOAD TRANSACTION FROM REMOTE DB
	}
	
	public static void addTransaction(Transaction item){
		ITEMS.add(item);
		ITEMS_MAP.put(item.id, item);
	}
}
