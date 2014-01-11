package fr.utt.if26.cs.utils;

import java.util.ArrayList;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.util.JSON;

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
	
	public static int computeTransactions(ArrayList<DataBean>[] datas){
		return computeTransactions(datas[0], datas[1]);
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<DataBean>[] getUserTransactions(User user) throws BeanException{
		ArrayList<DataBean>[] transactions = new ArrayList[2];
		Database dbTransactions = DatabaseManager.getInstance().getBase(DatabaseManager.TRANSACTIONS);
		BSONObject datas = new BasicBSONObject();
		datas.put("from", user.getEmail());
		ArrayList<DataBean> transactionsFrom = dbTransactions.findBeans(datas);
		datas = new BasicBSONObject();
		datas.put("to", user.getTag());
		ArrayList<DataBean> transactionsTo = dbTransactions.findBeans(datas);
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
		DataBean userTo = dbUsers.getBean("tag", t.getTo());
		// CHECK user from
		boolean userFromOk = (t.getFrom().equals(User.SYS_USER))?true:false;
		if(!userFromOk){
			DataBean userFrom = dbUsers.getBean("email", t.getFrom());
			if(userFrom==null)
				throw new BeanException("invalid debitor");
			applyTransactionsOnUser(userFrom);
			userFromOk = (((User) userFrom).getWallet()>=t.getAmount())?true:false;
		}
		if(userTo!=null)
			if (userFromOk){
				Database db = DatabaseManager.getInstance().getBase(DatabaseManager.TRANSACTIONS);
				db.insertBean(t);
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
	
	public static BSONObject toBSON(ArrayList<DataBean>[] transactions){
		BSONObject obj = new BasicBSONObject();
		obj.put("balance", TransactionsUtils.computeTransactions(transactions));
		ArrayList<BSONObject> objFrom = new ArrayList<>();
		ArrayList<BSONObject> objTo = new ArrayList<>();
		for(DataBean t : transactions[0]){
			objFrom.add((BSONObject) JSON.parse(t.toString()));
		}
		for(DataBean t : transactions[1]){
			objTo.add((BSONObject) JSON.parse(t.toString()));
		}
		obj.put("from", objFrom);
		obj.put("to", objTo);
		return obj;
	}
}
