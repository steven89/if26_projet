package fr.utt.if26.cs.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;

import fr.utt.if26.cs.database.Database;
import fr.utt.if26.cs.database.DatabaseManager;
import fr.utt.if26.cs.exceptions.BeanException;
import fr.utt.if26.cs.model.DataBean;
import fr.utt.if26.cs.model.Transaction;
import fr.utt.if26.cs.model.User;
import fr.utt.if26.cs.utils.ServletUtils;
import fr.utt.if26.cs.utils.TransactionsUtils;
import fr.utt.if26.cs.utils.UserUtils;


/**
 * Servlet implementation class Transaction
 */
@WebServlet("/Transaction")
public class TransactionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Database dbTransactions = DatabaseManager.getInstance().getBase(DatabaseManager.TRANSACTIONS);
		BasicBSONObject params = ServletUtils.extractRequestData(ServletUtils.GET, request);
		if(params.containsField("id")){
			if(ObjectId.isValid(params.getString("id"))){
				dbTransactions.open();
				Transaction transaction=null;
				try {
					transaction = (Transaction) dbTransactions.getBean("id", params.getString("id"));
				} catch (BeanException e) {
					out.println(e.getMessage());
					e.printStackTrace();
				}
				dbTransactions.close();
				out.println(transaction.getJSONStringRepresentation());
			}
			else{
				out.println("{'error':'invalid_id'}");
			}
		} 
		else {
			if(ServletUtils.checkRequiredFields(new String[]{"tag"}, params)){
				try {
					User userFrom = UserUtils.getUserFromTag(params.getString("tag"));
					ArrayList<DataBean>[] transactions = TransactionsUtils.getUserTransactions(userFrom);
					out.println("{ 'from': [");
					for(DataBean t : transactions[0]){
						out.println("\t"+t.getJSONStringRepresentation()+", ");
					}
					out.println("], 'to': [");
					for(DataBean t : transactions[1]){
						out.println("\t"+t.getJSONStringRepresentation());
					}
					out.println("]}");
				} catch (BeanException e) {
					out.println(e.getMessage());
					e.printStackTrace();
				}
				
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		BasicBSONObject params = ServletUtils.extractRequestData(ServletUtils.POST, request);
		if(ServletUtils.checkRequiredFields(new String[] {"from", "to", "amount"}, params)){
			Transaction transaction=null;
			try {
				transaction = new Transaction(
					params.getInt("amount"), 
					params.getString("from"), 
					params.getString("to")
				);
			} catch (BeanException e1) {
				out.println(e1.getMessage());
				e1.printStackTrace();
			}
			try {
				TransactionsUtils.doTransaction(transaction);
				out.println(transaction.getJSONStringRepresentation());
			} catch (BeanException e) {
				out.println(e.getMessage());
				e.printStackTrace();
			}
			
			
		}
	}
}
