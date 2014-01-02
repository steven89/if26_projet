package fr.utt.if26.cs.utils;

import java.util.ArrayList;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.database.DatabaseManager;
import fr.utt.if26.cs.model.DataBean;
import fr.utt.if26.cs.model.Transaction;
import fr.utt.if26.cs.model.User;

public class TransactionsUtils {
	
	
	private static int computeTransactions(ArrayList<DataBean> from, ArrayList<DataBean> to){
		int totalFrom=0, totalTo=0;
		for(DataBean t : from)
			totalFrom += ((Transaction) t).getMontant();
		for(DataBean t : to)
			totalTo += ((Transaction) t).getMontant();
		return totalTo-totalFrom;
	}
	
	private static int computeTransactions(ArrayList<DataBean>[] datas){
		return computeTransactions(datas[0], datas[1]);
	}
	
	private static ArrayList<DataBean>[] getUserTransactions(String tag){
		ArrayList<DataBean>[] transactions = new ArrayList[2];
		Database dbTransactions = DatabaseManager.getInstance().getBase(DatabaseManager.TRANSACTIONS);
		BSONObject datas = new BasicBSONObject();
		datas.put("from", tag);
		dbTransactions.open();
		ArrayList<DataBean> transactionsFrom = dbTransactions.findBeans(datas);
		datas = new BasicBSONObject();
		datas.put("to", tag);
		ArrayList<DataBean> transactionsTo = dbTransactions.findBeans(datas);
		dbTransactions.close();
		transactions[0] = transactionsFrom;
		transactions[1] = transactionsTo; 
		return transactions;
	}
	
	public static void applyTransactionsOnUser(DataBean user){
		((User) user).setWallet(
			((User) user).getWallet()+
			TransactionsUtils.computeTransactions(
					TransactionsUtils.getUserTransactions(((User) user).getTag())
			)
		);
	}
}
