package fr.utt.if26.cs.utils;

import java.util.ArrayList;

import javax.xml.crypto.Data;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.database.DatabaseManager;
import fr.utt.if26.cs.exceptions.BeanException;
import fr.utt.if26.cs.model.DataBean;
import fr.utt.if26.cs.model.Transaction;
import fr.utt.if26.cs.model.User;

public class TransactionsUtils {
	
	private static final int DEFAULT_WALLET = 50;
	
	
	private static int computeTransactions(ArrayList<DataBean> from, ArrayList<DataBean> to){
		int totalFrom=0, totalTo=0;
		for(DataBean t : from)
			totalFrom += ((Transaction) t).getAmount();
		for(DataBean t : to)
			totalTo += ((Transaction) t).getAmount();
		return totalTo-totalFrom;
	}
	
	private static int computeTransactions(ArrayList<DataBean>[] datas){
		return computeTransactions(datas[0], datas[1]);
	}
	
	public static ArrayList<DataBean>[] getUserTransactions(User user) throws BeanException{
		ArrayList<DataBean>[] transactions = new ArrayList[2];
		Database dbTransactions = DatabaseManager.getInstance().getBase(DatabaseManager.TRANSACTIONS);
		BSONObject datas = new BasicBSONObject();
		datas.put("from", user.getEmail());
		dbTransactions.open();
		ArrayList<DataBean> transactionsFrom = dbTransactions.findBeans(datas);
		datas = new BasicBSONObject();
		datas.put("to", user.getTag());
		ArrayList<DataBean> transactionsTo = dbTransactions.findBeans(datas);
		dbTransactions.close();
		transactions[0] = transactionsFrom;
		transactions[1] = transactionsTo; 
		return transactions;
	}
	
	public static void applyTransactionsOnUser(DataBean user) throws BeanException{
		((User) user).setWallet(
			TransactionsUtils.computeTransactions(
					TransactionsUtils.getUserTransactions((User) user)
			)
		);
	}
	
	/**
	 * save a transaction in DB (check if it can be done)
	 * @param t
	 * @throws BeanException 
	 */
	public static void doTransaction(Transaction t) throws BeanException{
		Database dbUsers = DatabaseManager.getInstance().getBase(DatabaseManager.USERS);
		dbUsers.open();
		DataBean userTo = dbUsers.getBean("tag", t.getTo());
		// CHECK user from
		boolean userFromOk = (t.getFrom().equals(User.SYS_USER))?true:false;
		if(!userFromOk){
			DataBean userFrom = dbUsers.getBean("email", t.getFrom());
			applyTransactionsOnUser(userFrom);
			userFromOk = (((User) userFrom).getWallet()>=t.getAmount())?true:false;
		}
		dbUsers.close();
		if(userTo!=null)
			if (userFromOk){
				Database db = DatabaseManager.getInstance().getBase(DatabaseManager.TRANSACTIONS);
				db.open();
				db.insertBean(t);
				db.close();
			}
			else
				throw new BeanException("invalid debitor amount");
		else
			throw new BeanException("invalid creditor");
	}
	
	public static void doBaseTransaction(String userTag){
		try {
			TransactionsUtils.doTransaction(new Transaction(TransactionsUtils.DEFAULT_WALLET, User.SYS_USER, userTag));
		} catch (BeanException e) {
			e.printStackTrace();
		}
	}
}
